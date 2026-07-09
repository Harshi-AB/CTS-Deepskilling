import java.util.List;

/**
 * ManageEmployeeAnnotation
 * ------------------------
 * Reproduces the classic tutorialspoint.com Hibernate Annotation
 * Configuration example, demonstrating @Entity, @Table, @Id,
 * @GeneratedValue, @Column and how hibernate.cfg.xml still supplies the
 * dialect/driver/connection URL/username/password even though the object-
 * relational mapping itself is now annotation-driven.
 */
public class ManageEmployeeAnnotation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManageEmployeeAnnotation.class);
    private static SessionFactory factory;

    public static void main(String[] args) {
        try {
            AnnotationConfiguration configuration = new AnnotationConfiguration();
            configuration.configure();
            configuration.addAnnotatedClass(Employee.class);
            factory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to load hibernate.cfg.xml / scan @Entity classes: " + e);
        }

        ManageEmployeeAnnotation me = new ManageEmployeeAnnotation();

        Integer empID1 = me.addEmployee("Zara", "Ali", 1000);
        Integer empID2 = me.addEmployee("Daisy", "Das", 5000);
        me.addEmployee("John", "Paul", 10000);

        me.listEmployees();

        me.updateEmployee(empID1, 7000);
        me.deleteEmployee(empID2);

        me.listEmployees();

        factory.close();
    }

    public Integer addEmployee(String firstName, String lastName, int salary) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;
        try {
            tx = session.beginTransaction();
            Employee employee = new Employee(firstName, lastName, salary);
            employeeID = session.save(employee);
            tx.commit();
            LOGGER.info("Employee created with id={}", employeeID);
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.error("addEmployee failed: {}", e.getMessage());
        } finally {
            session.close();
        }
        return employeeID;
    }

    @SuppressWarnings("unchecked")
    public void listEmployees() {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Employee> employees = session.createQuery("FROM Employee").list();
            for (Employee employee : employees) {
                LOGGER.info("Employee: {}", employee);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.error("listEmployees failed: {}", e.getMessage());
        } finally {
            session.close();
        }
    }

    public void updateEmployee(Integer employeeID, int salary) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, employeeID);
            employee.setSalary(salary);
            session.save(employee);
            tx.commit();
            LOGGER.info("Employee id={} updated to salary={}", employeeID, salary);
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.error("updateEmployee failed: {}", e.getMessage());
        } finally {
            session.close();
        }
    }

    public void deleteEmployee(Integer employeeID) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, employeeID);
            session.delete(employee);
            tx.commit();
            LOGGER.info("Employee id={} deleted", employeeID);
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.error("deleteEmployee failed: {}", e.getMessage());
        } finally {
            session.close();
        }
    }
}
