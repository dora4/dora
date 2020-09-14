package dora.permission.overlay;

import dora.permission.Boot;
import dora.permission.source.Source;

public class MRequestFactory implements Boot.OverlayRequestFactory {

    @Override
    public OverlayRequest create(Source source) {
        return new MRequest(source);
    }
}