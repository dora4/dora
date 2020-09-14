package dora.permission.runtime;

import dora.permission.source.Source;

public class MRequestFactory implements Runtime.PermissionRequestFactory {

    @Override
    public PermissionRequest create(Source source) {
        return new MRequest(source);
    }
}