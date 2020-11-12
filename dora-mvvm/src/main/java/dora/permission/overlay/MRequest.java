package dora.permission.overlay;

import dora.permission.RequestExecutor;
import dora.permission.bridge.BridgeRequest;
import dora.permission.bridge.RequestManager;
import dora.permission.source.Source;

class MRequest extends BaseRequest implements RequestExecutor, BridgeRequest.Callback {

    private Source mSource;

    MRequest(Source source) {
        super(source);
        this.mSource = source;
    }

    @Override
    public void start() {
        if (mSource.canDrawOverlays()) {
            onCallback();
        } else {
            showRationale(this);
        }
    }

    @Override
    public void execute() {
        BridgeRequest request = new BridgeRequest(mSource);
        request.setType(BridgeRequest.TYPE_OVERLAY);
        request.setCallback(this);
        RequestManager.get().add(request);
    }

    @Override
    public void cancel() {
        callbackFailed();
    }

    @Override
    public void onCallback() {
        if (mSource.canDrawOverlays() && tryDisplayDialog(mSource.getContext())) {
            callbackSucceed();
        } else {
            callbackFailed();
        }
    }
}