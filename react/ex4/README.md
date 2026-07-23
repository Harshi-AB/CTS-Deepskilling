# blogapp

React hands-on lab: component life cycle (`componentDidMount`, `componentDidCatch`).

## Folder structure

```
blogapp/
├── package.json
├── public/
│   └── index.html
└── src/
    ├── index.js
    ├── App.js
    ├── Post.js
    └── Posts.js
```

## Setup

```
npm install
```

## Run

```
npm start
```

This starts the dev server (default: http://localhost:3000) and opens the
app in your browser.

## What it does

- `Post.js` – plain model class holding `id`, `title`, `body`.
- `Posts.js` – class component:
  - `state.posts` initialized to `[]` in the constructor
  - `loadPosts()` fetches posts from
    `https://jsonplaceholder.typicode.com/posts` and stores them as
    `Post` instances in state
  - `componentDidMount()` calls `loadPosts()` once the component mounts
  - `render()` displays each post's title (`<h2>`) and body (`<p>`)
  - `componentDidCatch(error, info)` shows an `alert()` if rendering
    throws an error
- `App.js` – renders the `Posts` component.
- `index.js` – mounts `App` into `public/index.html`'s `#root` div.
