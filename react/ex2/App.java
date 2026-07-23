/**
 * App.java
 *
 * Equivalent of App.js in the React hands-on lab.
 * This class is responsible for creating instances of all the
 * "components" (Home, About, Contact) and invoking/rendering them,
 * exactly as App.js imports and calls <Home/>, <About/>, <Contact/>.
 */
public class App {

    // Holding references to each component, similar to how App.js
    // would import each component before using it.
    private final Component home;
    private final Component about;
    private final Component contact;

    /**
     * Constructor initializes all three components.
     */
    public App() {
        this.home = new Home();
        this.about = new About();
        this.contact = new Contact();
    }

    /**
     * Calls (renders) all three components one after another,
     * mirroring the JSX structure:
     *   <Home />
     *   <About />
     *   <Contact />
     */
    public void run() {
        home.render();
        about.render();
        contact.render();
    }
}
