/**
 * Main.java
 *
 * Exercise 03 - Creating Repositories
 * -------------------------------------
 * Demonstrates that the repository *interfaces* compile and form the
 * correct inheritance chain: EmployeeRepository -> JpaRepository ->
 * CrudRepository -> Repository. Actual runtime behaviour (a working
 * implementation you can call .save()/.findById() on) is introduced in
 * Exercise 04.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exercise 03: Creating Repositories ===\n");

        Class<EmployeeRepository> repo = EmployeeRepository.class;
        System.out.println("EmployeeRepository interface hierarchy:");
        for (Class<?> iface : repo.getInterfaces()) {
            System.out.println("  extends " + iface.getSimpleName());
            for (Class<?> superIface : iface.getInterfaces()) {
                System.out.println("    extends " + superIface.getSimpleName());
            }
        }

        System.out.println("\nInherited method contract available on EmployeeRepository:");
        for (var m : CrudRepository.class.getDeclaredMethods()) {
            System.out.println("  " + m.getReturnType().getSimpleName() + " " + m.getName() + "(...)");
        }

        System.out.println("\nDepartmentRepository also compiles against the same generic hierarchy: "
                + DepartmentRepository.class.getSimpleName());
    }
}
