package dora.permission.install;

import dora.permission.RequestExecutor;
import dora.permission.bridge.BridgeRequest;
import dora.permission.bridge.RequestManager;
import dora.permission.source.Source;

@Deprecated
class ORequest extends BaseRequest implements RequestExecutor, BridgeRequest.Callback {

    private Source mSource;

    ORequest(Source source) {
        super(source);
        this.mSource = source;
    }

    @Override
    public void start() {
        if (mSource.canRequestPackageInstalls()) {
            callbackSucceed();
            install();
        } else {
            showRationale(this);
        }
    }

    @Override
    public void execute() {
        BridgeRequest request = new BridgeRequest(mSource);
        request.setType(BridgeRequest.TYPE_INSTALL);
        request.setCallback(this);
        RequestManager.get().add(request);
    }

    @Override
    public void cancel() {
        callbackFailed();
    }

    @Override
    public void onCallback() {
        if (mSource.canRequestPackageInstalls()) {
            callbackSucceed();
            install();
        } else {
            callbackFailed();
        }
    }
}