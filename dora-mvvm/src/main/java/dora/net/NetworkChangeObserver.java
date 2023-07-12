package dora.net;

import androidx.annotation.NonNull;

import dora.util.NetUtils;

public interface NetworkChangeObserver {

    void onNetworkConnect(@NonNull NetUtils.ApnType type);
    void onNetworkDisconnect();
}