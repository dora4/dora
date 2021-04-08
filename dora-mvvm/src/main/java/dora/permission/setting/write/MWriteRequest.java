package dora.permission.setting.write;

import dora.permission.RequestExecutor;
import dora.permission.bridge.BridgeRequest;
import dora.permission.bridge.RequestManager;
import dora.permission.source.Source;

@Deprecated
public class MWriteRequest extends BaseRequest implements RequestExecutor, BridgeRequest.Callback {

    private Source mSource;

    public MWriteRequest(Source source) {
        super(source);
        this.mSource = source;
    }

    @Override
    public void start() {
        if (mSource.canWriteSetting()) {
            callbackSucceed();
        } else {
            showRationale(this);
        }
    }

    @Override
    public void execute() {
        BridgeRequest request = new BridgeRequest(mSource);
        request.setType(BridgeRequest.TYPE_WRITE_SETTING);
        request.setCallback(this);
        RequestManager.get().add(request);
    }

    @Override
    public void cancel() {
        callbackFailed();
    }

    @Override
    public void onCallback() {
        if (mSource.canWriteSetting()) {
            callbackSucceed();
        } else {
            callbackFailed();
        }
    }
}