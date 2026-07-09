import java.util.List;

/**
 * ManageEmployee
 * --------------
 * Reproduces the classic tutorialspoint.com Hibernate XML Configuration
 * example (https://www.tutorialspoint.com/hibernate/hibernate_examples.htm),
 * demonstrating every aspect called out in Hands-on 2's explanation topics:
 *
 *   SessionFactory, Session, Transaction,
 *   beginTransaction(), commit(), rollback(),
 *   session.save(), session.createQuery().list(), session.get(), session.delete()
 */
public class ManageEmployee {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManageEmployee.class);
    private static SessionFactory factory;

    public static void main(String[] args) {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to load hibernate.cfg.xml / Employee.hbm.xml: " + e);
        }

        ManageEmployee me = new ManageEmployee();

        /* Add a few employee records */
        Integer empID1 = me.addEmployee("Zara", "Ali", 1000);
        Integer empID2 = me.addEmployee("Daisy", "Das", 5000);
        Integer empID3 = me.addEmployee("John", "Paul", 10000);

        /* List down employees */
        me.listEmployees();

        /* Update employee's records */
        me.updateEmployee(empID1, 5000);

        /* Delete an employee from the database */
        me.deleteEmployee(empID2);

        /* List down new list of the employees */
        me.listEmployees();

        factory.close();
    }

    /** Method to CREATE an employee in the database. */
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

    /** Method to READ all the employees. */
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

    /** Method to UPDATE salary for an employee. */
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

    /** Method to DELETE an employee from the records. */
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
