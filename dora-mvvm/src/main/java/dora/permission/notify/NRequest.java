package dora.permission.notify;

import dora.permission.RequestExecutor;
import dora.permission.bridge.BridgeRequest;
import dora.permission.bridge.RequestManager;
import dora.permission.source.Source;

class NRequest extends BaseRequest implements RequestExecutor, BridgeRequest.Callback {

    private Source mSource;

    NRequest(Source source) {
        super(source);
        this.mSource = source;
    }

    @Override
    public void start() {
        if (mSource.canNotify()) {
            callbackSucceed();
        } else {
            showRationale(this);
        }
    }

    @Override
    public void execute() {
        BridgeRequest request = new BridgeRequest(mSource);
        request.setType(BridgeRequest.TYPE_APP_DETAILS);
        request.setCallback(this);
        RequestManager.get().add(request);
    }

    @Override
    public void cancel() {
        callbackFailed();
    }

    @Override
    public void onCallback() {
        if (mSource.canNotify()) {
            callbackSucceed();
        } else {
            callbackFailed();
        }
    }
}