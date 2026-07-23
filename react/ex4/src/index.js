import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

// Create the root React DOM node and render the App component into it
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
