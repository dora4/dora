package dora.permission.bridge;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Deprecated
public class RequestManager {

    private static RequestManager sManager;

    public static RequestManager get() {
        if (sManager == null) {
            synchronized (RequestManager.class) {
                if (sManager == null) {
                    sManager = new RequestManager();
                }
            }
        }
        return sManager;
    }

    private final BlockingQueue<BridgeRequest> mQueue;

    private RequestManager() {
        this.mQueue = new LinkedBlockingQueue<>();

        new RequestExecutor(mQueue).start();
    }

    public void add(BridgeRequest request) {
        mQueue.add(request);
    }
}