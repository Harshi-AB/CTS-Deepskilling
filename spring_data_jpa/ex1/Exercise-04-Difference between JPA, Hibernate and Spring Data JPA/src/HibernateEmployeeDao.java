/**
 * HibernateEmployeeDao
 * --------------------
 * The "Hibernate" column from Hands-on 4's comparison table, reproduced
 * almost verbatim from the exercise document:
 *
 *   public Integer addEmployee(Employee employee){
 *      Session session = factory.openSession();
 *      Transaction tx = null;
 *      Integer employeeID = null;
 *      try {
 *         tx = session.beginTransaction();
 *         employeeID = (Integer) session.save(employee);
 *         tx.commit();
 *      } catch (HibernateException e) {
 *         if (tx != null) tx.rollback();
 *         e.printStackTrace();
 *      } finally {
 *         session.close();
 *      }
 *      return employeeID;
 *   }
 *
 * Every one of these lines - opening the session, managing the
 * transaction, catching HibernateException, closing the session in a
 * finally block - is exactly what Spring Data JPA's @Transactional +
 * JpaRepository.save() do for you automatically (see EmployeeService).
 */
public class HibernateEmployeeDao {

    private final SessionFactory factory;

    public HibernateEmployeeDao(SessionFactory factory) {
        this.factory = factory;
    }

    /** Method to CREATE an employee in the database. */
    public Integer addEmployee(Employee employee) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;

        try {
            tx = session.beginTransaction();
            employeeID = session.save(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeID;
    }
}
