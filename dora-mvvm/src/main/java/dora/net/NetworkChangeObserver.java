package dora.net;

import dora.util.NetUtils;

public interface NetworkChangeObserver {

    void onNetworkConnect(NetUtils.ApnType type);

    void onNetworkDisconnect();
}