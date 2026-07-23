import React from 'react';
import Posts from './Posts';

// App is the top-level component. It simply hosts the Posts component,
// as required by step 10 of the hands-on lab ("Add the Posts component
// to App component").
function App() {
  return (
    <div className="App">
      <h1>Blog App</h1>
      <Posts />
    </div>
  );
}

export default App;
