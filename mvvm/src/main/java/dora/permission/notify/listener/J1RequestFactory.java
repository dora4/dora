package dora.permission.notify.listener;

import dora.permission.notify.Notify;
import dora.permission.source.Source;

public class J1RequestFactory implements Notify.ListenerRequestFactory {

    @Override
    public ListenerRequest create(Source source) {
        return new J1Request(source);
    }
}