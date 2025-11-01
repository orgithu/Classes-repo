var leftArea = document.getElementById('left');
var leftCircle = leftArea.children[0];
var rightArea = document.getElementById('right');
var rightCircle = rightArea.children[0];
var vs = document.getElementById("vs");

function init() {
    // initialize numeric labels if empty
    if (isNaN(parseInt(leftCircle.innerText))) leftCircle.innerText = '0';
    if (isNaN(parseInt(rightCircle.innerText))) rightCircle.innerText = '0';
    leftArea.style.flex = parseInt(leftCircle.innerText) || 1;
    rightArea.style.flex = parseInt(rightCircle.innerText) || 1;
    updateVS();
}

function updateVS() {
    var l = parseInt(leftArea.style.flex) || 1;
    var r = parseInt(rightArea.style.flex) || 1;
    // place VS as percentage of left's share
    vs.style.left = (100 * l / (l + r)) + '%';
}

function applyScale(circle, count) {
    // scale grows a bit with count, capped to avoid huge sizes
    var scale = 1 + Math.min(2.5, count * 0.06);
    circle.style.transform = 'translateZ(0) scale(' + scale + ')';
    circle.style.transition = 'transform 180ms ease, font-size 180ms ease';
}

function increment(area, circle) {
    var count = parseInt(circle.innerText) || 0;
    count++;
    circle.innerText = count;
    area.style.flex = count || 1;
    applyScale(circle, count);
    updateVS();
}

// click on area increments its count; clicking directly on the circle also works
leftArea.addEventListener('click', function (e) {
    // avoid double-handling if click bubbles from circle (we still want a single increment)
    increment(leftArea, leftCircle);
});

rightArea.addEventListener('click', function (e) {
    increment(rightArea, rightCircle);
});

// initialize
init();