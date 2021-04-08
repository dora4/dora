package dora.permission.notify.listener;

import dora.permission.RequestExecutor;
import dora.permission.source.Source;

@Deprecated
class J1Request extends BaseRequest implements RequestExecutor {

    J1Request(Source source) {
        super(source);
    }

    @Override
    public void start() {
        callbackSucceed();
    }

    @Override
    public void execute() {
        // Nothing.
    }

    @Override
    public void cancel() {
        // Nothing.
    }
}