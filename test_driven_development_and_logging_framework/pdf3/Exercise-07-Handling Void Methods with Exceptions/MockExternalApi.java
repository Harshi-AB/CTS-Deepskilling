/**
 * MockExternalApi is a hand-written mock implementation of ExternalApi
 * that supports stubbing a void method to THROW AN EXCEPTION, replicating
 * Mockito's:
 *
 *      doThrow(new RuntimeException("Delete failed"))
 *          .when(mockApi).deleteData("123");
 */
public class MockExternalApi implements ExternalApi {

    private RuntimeException exceptionToThrow;
    private boolean deleteDataCalled = false;
    private String lastDeletedId;

    /**
     * Stubs deleteData(...) to throw the given exception whenever it is called.
     * Equivalent of Mockito's doThrow(exception).when(mockApi).deleteData(...)
     *
     * @param exception the exception to throw when deleteData() is called
     */
    public void when_deleteData_thenThrow(RuntimeException exception) {
        this.exceptionToThrow = exception;
    }

    @Override
    public void deleteData(String id) {
        deleteDataCalled = true;
        lastDeletedId = id;

        if (exceptionToThrow != null) {
            throw exceptionToThrow;
        }
    }

    /**
     * Equivalent of Mockito's verify(mockApi).deleteData(id);
     * Verifies deleteData() was actually invoked with the given id,
     * even though it threw an exception.
     *
     * @param expectedId the id deleteData() was expected to be called with
     * @throws AssertionError if the verification fails
     */
    public void verifyDeleteDataCalledWith(String expectedId) {
        if (!deleteDataCalled) {
            throw new AssertionError("Expected deleteData() to be called, but it was never called.");
        }
        if (!expectedId.equals(lastDeletedId)) {
            throw new AssertionError("Expected deleteData() to be called with '" + expectedId
                    + "' but was called with '" + lastDeletedId + "'.");
        }
    }
}
