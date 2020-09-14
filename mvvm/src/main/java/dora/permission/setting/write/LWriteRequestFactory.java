package dora.permission.setting.write;

import dora.permission.setting.Setting;
import dora.permission.source.Source;

public class LWriteRequestFactory implements Setting.SettingRequestFactory {

    @Override
    public WriteRequest create(Source source) {
        return new LWriteRequest(source);
    }
}