package dora.flutter;

import java.util.List;

public abstract class MethodChannelPlugin<E, C, R> implements IMethodChannelPlugin<E> {

    protected abstract List<IMethodInvoker<C, R>> getInvokers();
}
