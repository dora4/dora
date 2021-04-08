package dora.permission.runtime;

import dora.permission.source.Source;

@Deprecated
public class LRequestFactory implements Runtime.PermissionRequestFactory {

    @Override
    public PermissionRequest create(Source source) {
        return new LRequest(source);
    }
}