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
  // very simple beginner-friendly save: read file sync, append, write sync
  const data = req.body;
  console.log('POST /submit received', data);
  const fs = require('fs');
  const filePath = path.join(__dirname, 'students.json');
  let students = [];
  try {
    const content = fs.readFileSync(filePath, 'utf8');
    students = JSON.parse(content);
    if (!Array.isArray(students)) students = [];
  } catch (e) {
    students = [];
  }

  // no validation, just store
  students.push(data);
  try {
    fs.writeFileSync(filePath, JSON.stringify(students, null, 2), 'utf8');
    res.json({ status: 'success', message: 'Data received and saved', received: data });
  } catch (we) {
    console.error('Failed to write students.json', we);
    res.status(500).json({ status: 'error', message: 'Failed to save data' });
  }
});

app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}/`);
});
