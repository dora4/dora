package dora.permission.runtime.setting;

import dora.permission.source.Source;

/**
 * <p>SettingRequest executor.</p>
 */
public class AllRequest implements SettingRequest {

    private Source mSource;

    public AllRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public void start(int requestCode) {
        SettingPage setting = new SettingPage(mSource);
        setting.start(requestCode);
    }
}