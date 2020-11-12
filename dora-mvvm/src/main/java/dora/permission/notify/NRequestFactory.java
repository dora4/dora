package dora.permission.notify;

import dora.permission.source.Source;

public class NRequestFactory implements Notify.PermissionRequestFactory {

    @Override
    public PermissionRequest create(Source source) {
        return new NRequest(source);
    }
}