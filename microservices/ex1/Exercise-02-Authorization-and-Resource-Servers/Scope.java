/**
 * Enumerates the OAuth 2.1 scopes recognized by this system.
 * Scopes describe *what* an Access Token is allowed to do,
 * decoupling authentication (who you are) from authorization
 * (what you can access).
 */
public enum Scope {
    READ_PROFILE,
    READ_ORDERS,
    WRITE_ORDERS,
    ADMIN
}
