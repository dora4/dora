package dora.permission.install;

import dora.permission.source.Source;

@Deprecated
class NRequest extends BaseRequest {

    NRequest(Source source) {
        super(source);
    }

    @Override
    public void start() {
        callbackSucceed();
        install();
    }
}