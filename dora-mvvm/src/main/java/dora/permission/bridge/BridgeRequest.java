package dora.permission.bridge;

import dora.permission.source.Source;

public final class BridgeRequest {

    public static final int TYPE_APP_DETAILS = 1;
    public static final int TYPE_PERMISSION = 2;
    public static final int TYPE_INSTALL = 3;
    public static final int TYPE_OVERLAY = 4;
    public static final int TYPE_ALERT_WINDOW = 5;
    public static final int TYPE_NOTIFY = 6;
    public static final int TYPE_NOTIFY_LISTENER = 7;
    public static final int TYPE_WRITE_SETTING = 8;

    private final Source mSource;

    private int mType;
    private Callback mCallback;
    private String[] mPermissions;

    public BridgeRequest(Source source) {
        this.mSource = source;
    }

    public Source getSource() {
        return mSource;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public String[] getPermissions() {
        return mPermissions;
    }

    public void setPermissions(String[] permissions) {
        mPermissions = permissions;
    }

    public interface Callback {

        void onCallback();
    }
}