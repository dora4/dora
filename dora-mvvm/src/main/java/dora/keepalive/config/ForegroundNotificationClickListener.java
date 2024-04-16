package dora.keepalive.config;

import android.content.Context;
import android.content.Intent;

public interface ForegroundNotificationClickListener {

    void onForegroundNotificationClick(Context context, Intent intent);
}
