package dora.permission.overlay;

import dora.permission.Boot;
import dora.permission.source.Source;

@Deprecated
public class LRequestFactory implements Boot.OverlayRequestFactory {

    @Override
    public OverlayRequest create(Source source) {
        return new LRequest(source);
    }
}