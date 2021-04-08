package dora.permission;

@Deprecated
public interface Action<T> {

    /**
     * An action.
     *
     * @param data the data.
     */
    void onAction(T data);
}