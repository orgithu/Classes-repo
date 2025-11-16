var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
var count = 3
var mouseX, mouseY;
var bubbles = []
var level = 0
var endFrame = 600;
var lost = false
let popSound = new Audio('/F.ITM202-25-26A/lab7/balloon_pop.mp3');
var displayDiv = document.createElement('div');
var restartButton = document.createElement('button');
//restart button
restartButton.innerText = "Game Over! Click to Restart";
restartButton.style.position = 'absolute';
restartButton.style.top = canvas.height / 2 + 'px';
restartButton.style.left = canvas.width / 2 + 'px';
restartButton.style.transform = 'translate(-50%, -50%)';
restartButton.style.fontSize = '20px';
restartButton.style.padding = '10px 20px';
restartButton.style.borderRadius = '10px';
//scoreboard
displayDiv.style.position = 'absolute';
displayDiv.style.top = '10px';
displayDiv.style.borderRadius = '5px';
displayDiv.style.left = '10px';
displayDiv.style.backgroundColor = 'rgba(255, 255, 255, 0.8)';
displayDiv.style.padding = '5px';
document.body.appendChild(displayDiv);
//random float generator
function getRandomFloat(min, max) {
  return Math.random() * (max - min) + min;
}
function moveBubble(bubble) {
    if (bubble.popping) {
        bubble.size *= 0.8;
        if (bubble.size < 0.2) {
            bubble.removed = true;
        }
    }
    if (lost) {
        bubble.removed = true;
        return;
    }
    context.beginPath();
    context.fillStyle = bubble.color;
    context.arc(bubble.x, bubble.y, 20 * bubble.size, 0, 2 * Math.PI);
    if(context.isPointInPath(mouseX, mouseY) && !bubble.popping) {
        bubble.popping = true;
        popSound.play();
        mouseX = null; 
        mouseY = null;
    };
    context.fill();
    context.beginPath();
    //bubble glare
    context.fillStyle = "#FFFFFF";
    context.arc(bubble.x-7, bubble.y-7, 5 * bubble.size, 0, 2 * Math.PI);
    context.fill();
    if (bubble.y <= 20 || bubble.y >= canvas.height - 20) {
        bubble.my *= -1;
    }
    if (bubble.x <= 20 || bubble.x >= canvas.width - 20) {
        bubble.mx *= -1;
    }
    if (!bubble.popping) {
        bubble.x+=bubble.mx;
        bubble.y+=bubble.my;
    }
}
function renderFrame() {
    // Update display
    displayDiv.innerText = 'Level: ' + level + ' | Time: ' + Math.ceil(endFrame / 60) + ' | Bubbles Left: ' + bubbles.length;

    context.fillStyle = "#000066";
    context.fillRect(0, 0, canvas.width, canvas.height);
    for(var i in bubbles) {
        moveBubble(bubbles[i])
    }
    bubbles = bubbles.filter(function (bubble) {return !bubble.removed });
    if (bubbles.length === 0) {
        level++;
        count+=2;
        endFrame = 600;
        createBubbles();
    }
    if (--endFrame === 0) {
        displayDiv.remove();
        document.body.appendChild(restartButton);
        lost = true;
        restartButton.addEventListener('click', function() {
            location.reload();
        });
    }
}
function createBubbles() {
    for(var i = 0; i < count; i++) {
        var bubble = {
            x: 20 + parseInt(Math.random() * (canvas.width - 40)),
            y: 20 + parseInt(Math.random() * (canvas.height - 40)),
            mx: parseInt(6 * Math.random()) - 3,
            my: parseInt(6 * Math.random()) - 3,
            size: getRandomFloat(0.8, 1.5),
            popping: false,
            color: 'rgb('+parseInt(256 * Math.random())+','+parseInt(256 * Math.random())+','+parseInt(256 * Math.random())+')'
        };
        bubbles.push(bubble);
    }
}
setInterval(renderFrame, 16)
canvas.addEventListener('mousedown', function(e) {
    mouseX = e.clientX
    mouseY = e.clientY
});