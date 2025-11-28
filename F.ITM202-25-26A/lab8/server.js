const express = require('express');
const path = require('path');
const app = express();
const port = process.env.PORT || 3000;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

//for serving static files (index.html, students.json)
app.use(express.static(path.join(__dirname)));

//sync GET example
app.get('/sync-data', (req, res) => {
  const q = req.query.q || 'default';
  res.set('Content-Type', 'text/plain');
  res.send(`Received query (sync): ${q}`);
});

//Async POST endpoint that echoes the request body
app.post('/submit', (req, res) => {
  const data = req.body;
  console.log('POST /submit received', data);
  //echo back the received json
  res.json({ status: 'success', message: 'Data received', received: data });
});

app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}/`);
});
