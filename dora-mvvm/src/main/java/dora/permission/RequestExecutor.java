package dora.permission;

/**
 * <p>Request executor.</p>
 */
@Deprecated
public interface RequestExecutor {

    /**
     * Go request permission.
     */
    void execute();

    /**
     * Cancel the operation.
     */
    void cancel();
}