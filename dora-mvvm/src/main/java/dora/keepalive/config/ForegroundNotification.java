package dora.keepalive.config;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ForegroundNotification implements Serializable {

    private String mTitle;
    private String mDescription;
    private int mIconRes;
    private ForegroundNotificationClickListener foregroundNotificationClickListener;

    private ForegroundNotification() {
    }

    public ForegroundNotification(String title, String description, int iconRes, ForegroundNotificationClickListener foregroundNotificationClickListener) {
        this.mTitle = title;
        this.mDescription = description;
        this.mIconRes = iconRes;
        this.foregroundNotificationClickListener = foregroundNotificationClickListener;
    }

    public ForegroundNotification(String title, String description, int iconRes) {
        this.mTitle = title;
        this.mDescription = description;
        this.mIconRes = iconRes;
    }

    public static ForegroundNotification init() {
        return new ForegroundNotification();
    }

    public ForegroundNotification title(@NonNull String title) {
        this.mTitle = title;
        return this;
    }

    public ForegroundNotification description(@NonNull String description) {
        this.mDescription = description;
        return this;
    }

    public ForegroundNotification icon(@NonNull int iconRes) {
        this.mIconRes = iconRes;
        return this;
    }

    public ForegroundNotification foregroundNotificationClickListener(@NonNull ForegroundNotificationClickListener foregroundNotificationClickListener) {
        this.foregroundNotificationClickListener = foregroundNotificationClickListener;
        return this;
    }

    public String getTitle() {
        return mTitle == null ? "" : mTitle;
    }

    public String getDescription() {
        return mDescription == null ? "" : mDescription;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public ForegroundNotificationClickListener getForegroundNotificationClickListener() {
        return foregroundNotificationClickListener;
    }
}
