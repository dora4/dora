package dora.permission.notify;

import dora.permission.source.Source;

public class ORequestFactory implements Notify.PermissionRequestFactory {

    @Override
    public PermissionRequest create(Source source) {
        return new ORequest(source);
    }
}