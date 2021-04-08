package dora.permission.install;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import dora.permission.Action;
import dora.permission.PermissionManager;
import dora.permission.Rationale;
import dora.permission.RequestExecutor;
import dora.permission.source.Source;

import java.io.File;

@Deprecated
abstract class BaseRequest implements InstallRequest {

    private Source mSource;

    private File mFile;
    private Rationale<File> mRationale = new Rationale<File>() {
        @Override
        public void showRationale(Context context, File data, RequestExecutor executor) {
            executor.execute();
        }
    };
    private Action<File> mGranted;
    private Action<File> mDenied;

    BaseRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public final InstallRequest file(File file) {
        this.mFile = file;
        return this;
    }

    @Override
    public final InstallRequest rationale(Rationale<File> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public final InstallRequest onGranted(Action<File> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public final InstallRequest onDenied(Action<File> denied) {
        this.mDenied = denied;
        return this;
    }

    /**
     * Why permissions are required.
     */
    final void showRationale(RequestExecutor executor) {
        mRationale.showRationale(mSource.getContext(), null, executor);
    }

    /**
     * Start the installation.
     */
    final void install() {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = PermissionManager.getFileUri(mSource.getContext(), mFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mSource.startActivity(intent);
    }

    /**
     * Callback acceptance status.
     */
    final void callbackSucceed() {
        if (mGranted != null) {
            mGranted.onAction(mFile);
        }
    }

    /**
     * Callback rejected state.
     */
    final void callbackFailed() {
        if (mDenied != null) {
            mDenied.onAction(mFile);
        }
    }
}