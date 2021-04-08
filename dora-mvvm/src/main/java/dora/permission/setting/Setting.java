package dora.permission.setting;

import android.os.Build;
import dora.permission.setting.write.LWriteRequestFactory;
import dora.permission.setting.write.MWriteRequestFactory;
import dora.permission.setting.write.WriteRequest;
import dora.permission.source.Source;

@Deprecated
public class Setting {

    private static final SettingRequestFactory SETTING_REQUEST_FACTORY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SETTING_REQUEST_FACTORY = new MWriteRequestFactory();
        } else {
            SETTING_REQUEST_FACTORY = new LWriteRequestFactory();
        }
    }

    public interface SettingRequestFactory {

        WriteRequest create(Source source);
    }

    private Source mSource;

    public Setting(Source source) {
        this.mSource = source;
    }

    /**
     * Handle write system settings.
     */
    public WriteRequest write() {
        return SETTING_REQUEST_FACTORY.create(mSource);
    }
}