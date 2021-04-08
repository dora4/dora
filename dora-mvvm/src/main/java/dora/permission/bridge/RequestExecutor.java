package dora.permission.bridge;

import java.util.concurrent.BlockingQueue;

@Deprecated
final class RequestExecutor extends Thread implements Messenger.Callback {

    private final BlockingQueue<BridgeRequest> mQueue;
    private BridgeRequest mRequest;
    private Messenger mMessenger;

    public RequestExecutor(BlockingQueue<BridgeRequest> queue) {
        this.mQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    mRequest = mQueue.take();
                } catch (InterruptedException e) {
                    continue;
                }

                mMessenger = new Messenger(mRequest.getSource().getContext(), this);
                mMessenger.register();
                executeCurrent();

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void executeCurrent() {
        switch (mRequest.getType()) {
            case BridgeRequest.TYPE_APP_DETAILS: {
                PermissionBridgeActivity.requestAppDetails(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_PERMISSION: {
                PermissionBridgeActivity.requestPermission(mRequest.getSource(), mRequest.getPermissions());
                break;
            }
            case BridgeRequest.TYPE_INSTALL: {
                PermissionBridgeActivity.requestInstall(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_OVERLAY: {
                PermissionBridgeActivity.requestOverlay(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_ALERT_WINDOW: {
                PermissionBridgeActivity.requestAlertWindow(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_NOTIFY: {
                PermissionBridgeActivity.requestNotify(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_NOTIFY_LISTENER: {
                PermissionBridgeActivity.requestNotificationListener(mRequest.getSource());
                break;
            }
            case BridgeRequest.TYPE_WRITE_SETTING: {
                PermissionBridgeActivity.requestWriteSetting(mRequest.getSource());
                break;
            }
        }
    }

    @Override
    public void onCallback() {
        synchronized (this) {
            mMessenger.unRegister();
            mRequest.getCallback().onCallback();
            notify();
        }
    }
}