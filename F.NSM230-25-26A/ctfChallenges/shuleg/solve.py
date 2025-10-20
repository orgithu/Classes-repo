from collections import Counter
import sys
import os

# interactive_substitution_solver.py
ciphertext = """
U.Erkjrxufia t
Rnsnn szyfl h
Xuragvv Xunatnv Fblavv haqhe fnvuna ahehhahhq f
Krlg cxjllq fklphj erovrq rl kxyfk xxoqxxg F
Zrara Funetn Abzvavv hetha vu tbivhq G
Qijq vqceej iwjhwe xkhokj ahoaj iwjdwj zwhwejqqz B
Oxo lyv wsxss debcex xedkq {
Zbatbyvva fnvuna beba Q
Fczmgzi,Jiji,Oppgddi opibvgvb vmdpi hpmippy 4
Helqli likff bj yliplk dloef yrixd oxpexxkrra p
Jgturftk,Tur,Athqhhm ftm srdmgdq mttqttc g
Zmf esdaaf mfvss tgdkgf lgajge tmjv mkfmmv v
Mvm jwt uqvqq bczacv vcbio 0
Prqjrollq vdlkdq rurq u
Runkrq,Vhohqjh,Kxkllq rqwv vdlq jroxxg m
Vvsibj cbzmhjjo pij cpmtpo pmpo vvm ebwbbovve 1
Cppxcdi cpncpp ypmnbvg,cjo wvgbvyddi nppmdippy 1
Zgd ysrjss gvkgf zsjyma vsjvsf rsemmv f
Bkb yli jfkff qroprk krqxd _
Egfygdaaf ksazsf gjgf F
Qxuqr pjiajjb phujuibjw cbjbc dwmda qjraqwddm 4
Huh tenger tselmesen huduu heer tsaidmuud t
Spwtqqv jizii piziolaiv vwgv apwdp aizqloccl a
Xkdyy iujwub judyyiud kktqc qcyiwqbj jqbkkt 4
Dmd ank lhmhh stqrtm mtszf f
Ikjckheej owedwj knkj z
Mjcpick iqxkkp jqqtqpf jcnjkkp wwfco pwvci 0
Wpg qpvp cphcpph wjsajc vjas  spkwxhpc vpopg g
Espssq ypyyryl ytjyqyl spr spr qfgjlssb h
Vizsu acfwb ifozrgob vibrww gowvob vcczcwbiir !
Iri fsp qmrmm xyvwyr ryxek !
Jlkdliffk pxfexk lolk }
"""

# start with your partial mapping (if you have one)
# build mapping by aligning the ciphertext above with a target plaintext (the poem).
# Put the poem text in poem.txt (same folder) or paste it into `target_poem` below.

target_poem = None
poem_path = os.path.join(os.path.dirname(__file__), "poem.txt")
if os.path.exists(poem_path):
    with open(poem_path, "r", encoding="utf8") as f:
        target_poem = f.read()
else:
    # Replace the string below with the poem text (from the URL) if you don't want to use poem.txt
    target_poem = """PASTE THE POEM TEXT HERE EXACTLY AS IT SHOULD APPEAR WHEN DECRYPTED"""

# build mapping by pairing characters from ciphertext -> target_poem
mapping = {}
conflicts = {}
minlen = min(len(ciphertext), len(target_poem))
for i in range(minlen):
    c = ciphertext[i]
    p = target_poem[i]
    if c.isalpha():
        if not p.isalpha():
            # target has non-letter where ciphertext has a letter; record conflict but continue
            conflicts.setdefault(c, set()).add(("expected_letter", p))
            continue
        # preserve case: if cipher char is uppercase, map to uppercase plaintext
        mapped = p.upper() if c.isupper() else p.lower()
        if c in mapping and mapping[c] != mapped:
            conflicts.setdefault(c, set()).add((mapping[c], mapped))
        mapping[c] = mapped

# report any leftover characters if lengths differ
if len(ciphertext) != len(target_poem):
    print(f"Warning: ciphertext length ({len(ciphertext)}) and target length ({len(target_poem)}) differ.")
    print("Only the first", minlen, "characters were aligned.")

if conflicts:
    print("Mapping conflicts detected (cipher -> {observed mappings}):")
    for k, v in conflicts.items():
        print(f"  {k} -> {v}")

print(f"Built mapping with {len(mapping)} entries. Save to a file or run the REPL to inspect/adjust.")


def decrypt_substitution(text, mapping):
    out = []
    for ch in text:
        if ch in mapping:
            out.append(mapping[ch])
        else:
            out.append('_' if ch.isalpha() else ch)
    return ''.join(out)

def freq_suggestions(text, top=12):
    letters = [c for c in text.lower() if c.isalpha()]
    cnt = Counter(letters)
    most = [p for p,_ in cnt.most_common(top)]
    return most

def parse_assignments(s):
    # parse things like "h=u a=n"
    s = s.strip()
    if not s:
        return {}
    pairs = s.split()
    out = {}
    for p in pairs:
        if '=' in p:
            left,right = p.split('=',1)
            if len(left)==1 and len(right)==1:
                out[left] = right
    return out

def show_mapping(m):
    items = sorted(m.items(), key=lambda kv: kv[0])
    print("Current mapping (cipher -> plain):")
    for k,v in items:
        print(f"  {k} -> {v}")
    print()

def save_mapping(m, filename):
    with open(filename, 'w', encoding='utf8') as f:
        f.write("mapping = {\n")
        for k,v in sorted(m.items()):
            f.write(f"    {repr(k)}: {repr(v)},\n")
        f.write("}\n")
    print("Saved mapping to", filename)

def repl():
    print("Interactive substitution helper. Commands:")
    print("  <pairs>   e.g.  h=u a=n  (adds/updates mappings)")
    print("  auto      suggest freq-based mapping to 'etaoinshrdlu'")
    print("  show      show mapping")
    print("  save fn   save mapping to file")
    print("  quit      exit\n")
    while True:
        print("\n----- decrypted preview -----\n")
        print(decrypt_substitution(ciphertext, mapping))
        print("\n-----------------------------\n")
        cmd = input("cmd> ").strip()
        if not cmd:
            continue
        if cmd == 'quit':
            break
        if cmd == 'show':
            show_mapping(mapping)
            continue
        if cmd.startswith('save '):
            _,fn = cmd.split(None,1)
            save_mapping(mapping, fn)
            continue
        if cmd == 'auto':
            freq = freq_suggestions(ciphertext, top=12)
            target = list("etaoinshrdlu")
            for i,c in enumerate(freq):
                if c not in mapping and i < len(target):
                    mapping[c] = target[i]
            print("Applied frequency suggestions for:", freq)
            continue
        # assume assignments
        new = parse_assignments(cmd)
        if not new:
            print("Unrecognized command or bad assignment format.")
            continue
        mapping.update(new)
        print("Updated mapping with:", new)

if __name__ == "__main__":
    try:
        repl()
    except KeyboardInterrupt:
        print("\nexited.")
