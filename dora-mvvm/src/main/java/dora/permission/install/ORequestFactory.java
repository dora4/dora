package dora.permission.install;

import dora.permission.Boot;
import dora.permission.source.Source;

@Deprecated
public class ORequestFactory implements Boot.InstallRequestFactory {

    @Override
    public InstallRequest create(Source source) {
        return new ORequest(source);
    }
}