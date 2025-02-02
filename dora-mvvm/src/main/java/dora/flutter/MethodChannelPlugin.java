package dora.flutter;

import java.util.List;

public abstract class MethodChannelPlugin<E, C, R> implements IMethodChannelPlugin<E> {

    public abstract List<IMethodInvoker<C, R>> getInvokers();
}
