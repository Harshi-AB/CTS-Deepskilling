import java.io.Serializable;

/**
 * Repository.java
 *
 * Exercise 03 - Creating Repositories
 * -------------------------------------
 * Root marker interface of the whole repository hierarchy - mirrors
 * org.springframework.data.repository.Repository<T, ID>. It carries no
 * methods of its own; its only job is to let the framework (and the
 * developer) identify "this interface is a Spring-Data-style repository"
 * via a type marker, which the RepositoryFactory in later exercises will
 * detect with java.lang.reflect.Proxy.
 *
 * @param <T>  the entity type managed by the repository
 * @param <ID> the type of the entity's primary key
 */
public interface Repository<T, ID extends Serializable> {
}
