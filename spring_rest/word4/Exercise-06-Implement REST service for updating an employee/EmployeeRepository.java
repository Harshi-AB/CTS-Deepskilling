import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * EmployeeRepository.java
 *
 * In-memory data store for Employee resources, keyed by employee id.
 */
public class EmployeeRepository {

    private final Map<Integer, Employee> employees = new LinkedHashMap<>();

    public EmployeeRepository() {
        seedData();
    }

    private void seedData() {
        save(new Employee(1, "Aditi Sharma", "Engineering", 65000));
        save(new Employee(2, "Rahul Verma", "Sales", 52000));
        save(new Employee(3, "Kavya Iyer", "Human Resources", 48000));
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    public Optional<Employee> findById(int id) {
        return Optional.ofNullable(employees.get(id));
    }

    public Employee save(Employee employee) {
        employees.put(employee.getId(), employee);
        return employee;
    }

    public boolean existsById(int id) {
        return employees.containsKey(id);
    }

    public void deleteById(int id) {
        employees.remove(id);
    }
}
