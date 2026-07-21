/**
 * RemoteCall is a small functional interface representing "the thing we
 * want to protect" - i.e. a call to a downstream microservice. This is
 * the plain-Java stand-in for the Supplier/CheckedSupplier that
 * Resilience4j decorators wrap around.
 *
 * @param <T> the type of result returned by the remote call
 */
@FunctionalInterface
public interface RemoteCall<T> {
    T execute() throws RemoteServiceException;
}
