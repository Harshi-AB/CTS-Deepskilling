/**
 * ElectricEngine.java
 *
 * A second concrete Engine implementation, used to show that setter
 * injection allows the dependency to be changed after object
 * construction.
 */
public class ElectricEngine implements Engine {
    @Override
    public String start() {
        return "Electric engine started (silent hum)";
    }
}
