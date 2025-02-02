package dora.flutter;

/**
 * @param <E> FlutterEngine
 */
public interface IMethodChannelPlugin<E> {

    void registerWith(E engine);

    void detach();
}
