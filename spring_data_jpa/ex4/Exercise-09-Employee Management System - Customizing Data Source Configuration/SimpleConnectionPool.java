import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * SimpleConnectionPool.java
 *
 * Exercise 09 - Customizing Data Source Configuration
 * -----------------------------------------------------
 * A small hand-rolled connection pool, playing the same role HikariCP
 * plays for a real Spring Boot application: eagerly opens a fixed number
 * of physical connections and hands out reusable proxies instead of
 * making every repository call open/close a brand-new socket to MySQL.
 *
 * Key trick: getConnection() returns a java.lang.reflect.Proxy around the
 * real Connection whose close() call is intercepted and redirected into
 * releaseConnection() instead of actually closing the socket - callers
 * keep writing normal try-with-resources code and never know the
 * difference.
 *
 * Design patterns: Object Pool + Proxy.
 */
public class SimpleConnectionPool {

    private final BlockingQueue<Connection> pool;

    public SimpleConnectionPool(String url, String username, String password, String driver, int size) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC driver not found: " + driver, e);
        }

        this.pool = new LinkedBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            try {
                pool.add(DriverManager.getConnection(url, username, password));
            } catch (SQLException e) {
                throw new RuntimeException("Failed to pre-populate connection pool", e);
            }
        }
    }

    /** Borrows a proxied connection from the pool, waiting up to 5s if the pool is exhausted. */
    public Connection getConnection() {
        try {
            Connection real = pool.poll(5, TimeUnit.SECONDS);
            if (real == null) {
                throw new RuntimeException("Timed out waiting for a free pooled connection");
            }
            return wrapWithPoolAwareProxy(real);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while borrowing connection", e);
        }
    }

    private Connection wrapWithPoolAwareProxy(Connection real) {
        InvocationHandler handler = (proxy, method, args) -> {
            if ("close".equals(method.getName())) {
                pool.offer(real); // return to pool instead of really closing the socket
                return null;
            }
            try {
                return method.invoke(real, args);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        };

        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                handler);
    }

    public int availableConnections() {
        return pool.size();
    }
}
