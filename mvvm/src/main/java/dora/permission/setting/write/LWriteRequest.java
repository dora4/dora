package dora.permission.setting.write;

import dora.permission.source.Source;

public class LWriteRequest extends BaseRequest {

    public LWriteRequest(Source source) {
        super(source);
    }

    @Override
    public void start() {
        callbackSucceed();
    }
}