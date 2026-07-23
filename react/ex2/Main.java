/**
 * Main.java
 *
 * Entry point of the program.
 * Equivalent of how "npm start" boots up index.js, which in turn
 * renders <App /> in the React project ("StudentApp").
 */
public class Main {

    public static void main(String[] args) {
        // Create the App (equivalent to ReactDOM.render(<App />, ...))
        App app = new App();

        // Run the application: this will call/render Home, About, Contact
        app.run();
    }
}
