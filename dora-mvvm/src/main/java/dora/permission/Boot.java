package dora.permission;

import android.os.Build;
import dora.permission.install.InstallRequest;
import dora.permission.install.NRequestFactory;
import dora.permission.install.ORequestFactory;
import dora.permission.notify.Notify;
import dora.permission.notify.option.NotifyOption;
import dora.permission.option.Option;
import dora.permission.overlay.LRequestFactory;
import dora.permission.overlay.MRequestFactory;
import dora.permission.overlay.OverlayRequest;
import dora.permission.runtime.Runtime;
import dora.permission.runtime.option.RuntimeOption;
import dora.permission.setting.Setting;
import dora.permission.source.Source;

@Deprecated
public class Boot implements Option {

    private static final InstallRequestFactory INSTALL_REQUEST_FACTORY;
    private static final OverlayRequestFactory OVERLAY_REQUEST_FACTORY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            INSTALL_REQUEST_FACTORY = new ORequestFactory();
        } else {
            INSTALL_REQUEST_FACTORY = new NRequestFactory();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            OVERLAY_REQUEST_FACTORY = new MRequestFactory();
        } else {
            OVERLAY_REQUEST_FACTORY = new LRequestFactory();
        }
    }

    public interface InstallRequestFactory {

        /**
         * Create apk installer request.
         */
        InstallRequest create(Source source);
    }

    public interface OverlayRequestFactory {

        /**
         * Create overlay request.
         */
        OverlayRequest create(Source source);
    }

    private Source mSource;

    public Boot(Source source) {
        this.mSource = source;
    }

    @Override
    public RuntimeOption runtime() {
        return new Runtime(mSource);
    }

    @Override
    public InstallRequest install() {
        return INSTALL_REQUEST_FACTORY.create(mSource);
    }

    @Override
    public OverlayRequest overlay() {
        return OVERLAY_REQUEST_FACTORY.create(mSource);
    }

    @Override
    public NotifyOption notification() {
        return new Notify(mSource);
    }

    @Override
    public Setting setting() {
        return new Setting(mSource);
    }
}