#!/usr/bin/env python3
# This script solves a CTF challenge by brute-forcing MD5 hashes in an arithmetic expression, evaluating it, and posting the result.
#https://chatgpt.com/s/t_68fb70033f8c8191ab7cd0aba06e6cd3
import requests, re, hashlib, time, json, multiprocessing, sys
import ast, operator as op
from functools import partial
from pathlib import Path

BASE_URL = "http://139.162.5.230:10287/"
PAGE_PATH = ""
MAX_INT = 200000
WORKERS = max(1, multiprocessing.cpu_count() - 1)
CACHE_FILE = Path("md5_cache.json")

MD5_RE = re.compile(r'\b[a-fA-F0-9]{32}\b')

def fetch_page():
    url = BASE_URL + PAGE_PATH
    print(f"[+] fetching {url}")
    r = requests.get(url, timeout=10)
    r.raise_for_status()
    return r.text

def find_hashes(html):
    return sorted(set(m.lower() for m in MD5_RE.findall(html)))

def chunked_iterable(n, chunk_size):
    for start in range(0, n, chunk_size):
        yield start, min(n, start + chunk_size)

def worker_find(start, end, targets):
    out = {}
    for i in range(start, end):
        h = hashlib.md5(str(i).encode()).hexdigest()
        if h in targets:
            out[h] = i
    return out

def brute_md5_targets(targets, max_int=MAX_INT, workers=WORKERS):
    targets = set(h.lower() for h in targets)
    found = {}
    if CACHE_FILE.exists():
        try:
            cache = json.loads(CACHE_FILE.read_text())
            for k, v in cache.items():
                if k in targets:
                    found[k] = v
            print(f"[+] loaded {len(found)} from cache")
        except Exception:
            pass

    if found.keys() >= targets:
        return found

    remaining = targets - set(found.keys())
    print(f"[+] need to find {len(remaining)} hashes by brute-force (0..{max_int-1})")

    pool = multiprocessing.Pool(workers)
    chunk = 20000
    tasks = []
    for start, end in chunked_iterable(max_int, chunk):
        tasks.append(pool.apply_async(worker_find, (start, end, remaining)))
    pool.close()

    try:
        for t in tasks:
            res = t.get()
            if res:
                for k, v in res.items():
                    if k not in found and k in remaining:
                        found[k] = v
                        remaining.remove(k)
                        print(f"[+] found {k} -> {v}  (remaining {len(remaining)})")
            if not remaining:
                break
    finally:
        pool.terminate()
        pool.join()

    try:
        existing = {}
        if CACHE_FILE.exists():
            existing = json.loads(CACHE_FILE.read_text())
        existing.update({k: v for k, v in found.items()})
        CACHE_FILE.write_text(json.dumps(existing))
    except Exception as e:
        print("[!] failed to update cache:", e)

    return found

ALLOWED_OPS = {
    ast.Add: op.add,
    ast.Sub: op.sub,
    ast.Mult: op.mul,
    ast.Div: op.floordiv,
    ast.Mod: op.mod,
    ast.Pow: op.pow,
    ast.USub: op.neg,
    ast.UAdd: lambda x: x,
}

def safe_eval(expr):
    def _eval(node):
        if isinstance(node, ast.Expression):
            return _eval(node.body)
        if isinstance(node, ast.Num):
            return node.n
        if isinstance(node, ast.BinOp):
            left = _eval(node.left)
            right = _eval(node.right)
            op_type = type(node.op)
            if op_type in ALLOWED_OPS:
                return ALLOWED_OPS[op_type](left, right)
            raise TypeError(f"Unsupported binary op {op_type}")
        if isinstance(node, ast.UnaryOp):
            op_type = type(node.op)
            if op_type in ALLOWED_OPS:
                return ALLOWED_OPS[op_type](_eval(node.operand))
            raise TypeError(f"Unsupported unary op {op_type}")
        raise TypeError(f"Unsupported AST node {type(node)}")
    node = ast.parse(expr, mode='eval')
    return _eval(node)

def replace_hashes_with_ints(expr, mapping):
    def repl(m):
        h = m.group(0).lower()
        if h in mapping:
            return str(mapping[h])
        else:
            raise KeyError(f"No mapping for hash {h}")
    return MD5_RE.sub(repl, expr)

def extract_expression(html):
    for line in html.splitlines():
        if MD5_RE.search(line) and any(ch in line for ch in "+-*()"):
            return line.strip()
    return "".join(html.splitlines())

def post_answer(value):
    url = BASE_URL.rstrip("/") + "/sum"
    print(f"[+] posting answer to {url}: {value}")
    r = requests.post(url, data={"sum": str(value)}, timeout=10)
    print("[+] response:", r.status_code)
    print(r.text[:400])

def main():
    html = fetch_page()
    expr = extract_expression(html)
    print("[+] expression:", expr)
    hashes = find_hashes(expr)
    print(f"[+] found {len(hashes)} hash tokens")
    if not hashes:
        print("[!] no md5 hashes found, quitting")
        return

    mapping = brute_md5_targets(hashes, max_int=MAX_INT, workers=WORKERS)
    if set(mapping.keys()) != set(hashes):
        print("[!] not all hashes found. Found:", mapping)
        print("You may need to increase MAX_INT and try again.")
        return

    replaced = replace_hashes_with_ints(expr, mapping)
    cleaned = "".join(ch for ch in replaced if ch.isdigit() or ch.isspace() or ch in "+-*/()%")
    print("[+] replaced expr:", cleaned)

    result = safe_eval(cleaned)
    print("[+] result:", result)

    try:
        post_answer(result)
    except Exception as e:
        print("[!] failed to post answer:", e)

if __name__ == "__main__":
    main()
