/**
 * EmployeeContactInfo.java
 *
 * Exercise 08 - Creating Projections
 * -----------------------------------------------------
 * A second, differently-shaped closed projection over the SAME Employee
 * entity - demonstrates that one entity can be projected into multiple
 * interfaces depending on what the caller needs.
 */
public interface EmployeeContactInfo {
    String getName();
    String getEmail();
}
