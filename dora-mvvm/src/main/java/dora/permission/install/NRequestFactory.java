package dora.permission.install;

import dora.permission.Boot;
import dora.permission.source.Source;

public class NRequestFactory implements Boot.InstallRequestFactory {

    @Override
    public InstallRequest create(Source source) {
        return new NRequest(source);
    }
}