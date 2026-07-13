import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Main.java
 *
 * Exercise 02 - Creating Entities
 * ---------------------------------
 * There is no repository yet (that's Exercise 03), so this demo simply
 * proves the entity classes and their annotation metadata are wired up
 * correctly by instantiating them and reading the annotations back via
 * reflection - the same mechanism the ORM engine will use later.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Exercise 02: Creating Entities ===\n");

        Department engineering = new Department("Engineering", "Bangalore");
        Employee emp = new Employee("Harshitha R", "harshitha@example.com",
                new BigDecimal("75000.00"), LocalDate.now(), true, engineering);

        System.out.println("Created entity instance: " + emp);

        System.out.println("\nReflecting over Employee.class annotations:");
        System.out.println("  @Entity present:  " + Employee.class.isAnnotationPresent(Entity.class));
        System.out.println("  @Table name:       " + Employee.class.getAnnotation(Table.class).name());

        for (var field : Employee.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column col = field.getAnnotation(Column.class);
                System.out.printf("  field '%s' -> column '%s' (nullable=%b, unique=%b)%n",
                        field.getName(), col.name(), col.nullable(), col.unique());
            } else if (field.isAnnotationPresent(Id.class)) {
                System.out.printf("  field '%s' -> @Id (PRIMARY KEY)%n", field.getName());
            } else if (field.isAnnotationPresent(ManyToOne.class)) {
                JoinColumn jc = field.getAnnotation(JoinColumn.class);
                System.out.printf("  field '%s' -> @ManyToOne, join column '%s'%n",
                        field.getName(), jc.name());
            }
        }
    }
}
