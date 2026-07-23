/*
 * Exercise: Create a new React Application named "myfirstreact"
 * Original React requirement (App.js):
 *
 *      function App() {
 *        return (
 *          <h1> Welcome the first session of React </h1>
 *        );
 *      }
 *
 * This Core Java program mirrors that single hands-on exercise.
 * The App class below plays the role of the React "App" component:
 * its render() method returns the heading markup/text, exactly like
 * the JSX return statement in App.js. MyFirstReactApp plays the role
 * of index.js / ReactDOM, which mounts (invokes) App and displays
 * the output — simulating what happens when the app runs on
 * localhost:3000.
 *
 * No package declaration, no external libraries — Core Java only.
 */

// Represents the React "App" functional component
class App {

    // Equivalent of the component's return(...) JSX block.
    // Returns the heading content that would be rendered to the page.
    public String render() {
        return "<h1> Welcome the first session of React </h1>";
    }
}

// Equivalent of index.js — mounts the App component and "renders" it
public class MyFirstReactApp {

    public static void main(String[] args) {
        // Instantiate the App component (like <App /> in JSX)
        App app = new App();

        // "Render" the component's output to the page (console here)
        String output = app.render();

        System.out.println("Starting myfirstreact application...");
        System.out.println("Compiled successfully!");
        System.out.println();
        System.out.println("You can now view myfirstreact in the browser.");
        System.out.println("  Local: http://localhost:3000");
        System.out.println();
        System.out.println("Rendered Output:");
        System.out.println(output);
    }
}
