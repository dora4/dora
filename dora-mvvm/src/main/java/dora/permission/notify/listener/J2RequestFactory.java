package dora.permission.notify.listener;

import dora.permission.notify.Notify;
import dora.permission.source.Source;

@Deprecated
public class J2RequestFactory implements Notify.ListenerRequestFactory {

    @Override
    public ListenerRequest create(Source source) {
        return new J2Request(source);
    }
}