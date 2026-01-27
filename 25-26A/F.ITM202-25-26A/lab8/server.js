const express = require('express');
const app = express();
const port = 3000;

app.use(express.static('.'));

app.post('/page1', (req, res) => {
    res.sendFile(__dirname + '/page1.html');
});

app.get('/page2', (req, res) => {
    res.sendFile(__dirname + '/page2.html');
});

app.get('/students', (req, res) => {
    res.sendFile(__dirname + '/students.json');
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});