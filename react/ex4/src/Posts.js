import React from 'react';
import Post from './Post';

// Posts.js
// Class-based component that:
//  - keeps a list of Post objects in state (initialized in the constructor)
//  - loads the posts from a remote API using loadPosts()
//  - calls loadPosts() once the component has mounted (componentDidMount)
//  - renders each post's title and body (render)
//  - reports any rendering error raised by this component via an alert
//    (componentDidCatch)
class Posts extends React.Component {
  constructor(props) {
    super(props);

    // Step 5: initialize the component's state with an empty list of Post
    this.state = {
      posts: []
    };
  }

  // Step 6: fetch the posts from the JSONPlaceholder API and store them
  // in the component's state as an array of Post objects.
  loadPosts() {
    fetch('https://jsonplaceholder.typicode.com/posts')
      .then((response) => response.json())
      .then((data) => {
        const posts = data.map(
          (item) => new Post(item.id, item.title, item.body)
        );
        this.setState({ posts: posts });
      })
      .catch((error) => {
        // Surface network / parsing errors the same way componentDidCatch does
        alert('Error while loading posts: ' + error.message);
      });
  }

  // Step 7: componentDidMount runs once, right after the component is
  // first rendered to the DOM - the correct place to kick off data fetching.
  componentDidMount() {
    this.loadPosts();
  }

  // Step 9: componentDidCatch acts as an error boundary. If any error is
  // thrown while rendering this component (or its children), React calls
  // this method with the error and component-stack info.
  componentDidCatch(error, info) {
    alert('Something went wrong while rendering Posts: ' + error.toString());
  }

  // Step 8: render the title (heading) and body (paragraph) of every post
  render() {
    return (
      <div className="posts">
        {this.state.posts.map((post) => (
          <div key={post.id} className="post">
            <h2>{post.title}</h2>
            <p>{post.body}</p>
          </div>
        ))}
      </div>
    );
  }
}

export default Posts;
