/**
 * Component.java
 *
 * This interface simulates the concept of a React "component" in Core Java.
 * In React, every component (class-based) implements a render() method that
 * returns/displays UI/output. Here, every Java class that represents a
 * "page" of the Student Management Portal implements this interface and
 * provides its own render() behavior — mirroring the idea of multiple
 * components being created and then called/rendered together.
 */
public interface Component {

    /**
     * Equivalent of React's render() method.
     * Each concrete component class must implement this method
     * to display its own message.
     */
    void render();
}
