package dora.net;

import dora.util.NetworkUtils;

public interface NetworkChangeObserver {

    void onNetworkConnect(NetworkUtils.ApnType type);

    void onNetworkDisconnect();
}