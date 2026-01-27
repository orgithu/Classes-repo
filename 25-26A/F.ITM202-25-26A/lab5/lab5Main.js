// lab5Main.js - Wait for SVG <object> assets to load then start scroll animations
document.addEventListener('DOMContentLoaded', function () {
    const tracks = Array.from(document.querySelectorAll('.scroll-track'));
    const objects = Array.from(document.querySelectorAll('.scroll-track object'));  

    if (objects.length === 0) {
        // no external objects, start immediately
        tracks.forEach(t => t.classList.add('animate'));
        return;
    }

    let remaining = objects.length;
    const startAll = () => tracks.forEach(t => t.classList.add('animate'));

    const onLoaded = (ev) => {
        remaining -= 1;
        if (remaining <= 0) startAll();
    };

    // attach events
    objects.forEach(obj => {
        try {
            obj.addEventListener('load', onLoaded);
            obj.addEventListener('error', onLoaded);
        } catch (e) {
            // some browsers might not support load on object; decrement and rely on timeout
            remaining -= 1;
        }
    });

    // fallback: start after 1500ms even if some objects didn't fire load
    setTimeout(() => {
        startAll();
    }, 1500);
});
