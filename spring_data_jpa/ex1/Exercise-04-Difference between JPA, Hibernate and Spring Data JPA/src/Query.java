import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Custom re-implementation of org.hibernate.query.Query.
 * Only the .list() operation used by "from Employee" style HQL is
 * implemented - enough to demonstrate session.createQuery(hql).list().
 */
public class Query {

    private final String hql;
    private final EntityMapping mapping;

    Query(String hql, EntityMapping mapping) {
        this.hql = hql;
        this.mapping = mapping;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list() {
        System.out.println("Hibernate HQL [" + hql + "] -> select * from " + mapping.getTableName());
        List<T> results = new ArrayList<>();
        for (Map<String, Object> row : MySQLDatabase.findAll(mapping.getTableName())) {
            results.add((T) SessionEntityMapper.hydrate(mapping, row));
        }
        return results;
    }
}
