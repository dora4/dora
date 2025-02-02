package dora.flutter;

/**
 * @param <C> MethodCall
 * @param <R> MethodChannel.Result
 */
public interface IMethodInvoker<C, R> {

    String getMethodName();

    void invokeNativeMethod(C call, R result);
}
