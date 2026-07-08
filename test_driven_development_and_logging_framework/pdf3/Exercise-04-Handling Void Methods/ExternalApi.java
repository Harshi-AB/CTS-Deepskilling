/**
 * ExternalApi represents a third-party / external service dependency.
 * saveData() is a void method (performs an action, returns nothing),
 * which is what this exercise focuses on.
 */
public interface ExternalApi {

    /**
     * Saves data to the external system. Returns nothing (void method).
     *
     * @param data the data to save
     */
    void saveData(String data);
}
