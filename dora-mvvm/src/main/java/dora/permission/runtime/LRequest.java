package dora.permission.runtime;

import android.os.AsyncTask;
import android.util.Log;
import dora.permission.Action;
import dora.permission.Rationale;
import dora.permission.checker.PermissionChecker;
import dora.permission.checker.StrictChecker;
import dora.permission.source.Source;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Deprecated
class LRequest implements PermissionRequest {

    private static final PermissionChecker STRICT_CHECKER = new StrictChecker();

    private Source mSource;

    private String[] mPermissions;
    private Action<List<String>> mGranted;
    private Action<List<String>> mDenied;

    LRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public PermissionRequest permission(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    @Override
    public PermissionRequest rationale(Rationale<List<String>> rationale) {
        return this;
    }

    @Override
    public PermissionRequest onGranted(Action<List<String>> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public PermissionRequest onDenied(Action<List<String>> denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public void start() {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                return getDeniedPermissions(STRICT_CHECKER, mSource, mPermissions);
            }

            @Override
            protected void onPostExecute(List<String> deniedList) {
                if (deniedList.isEmpty()) {
                    callbackSucceed();
                } else {
                    callbackFailed(deniedList);
                }
            }
        }.execute();
    }

    /**
     * Callback acceptance status.
     */
    private void callbackSucceed() {
        if (mGranted != null) {
            List<String> permissionList = asList(mPermissions);
            try {
                mGranted.onAction(permissionList);
            } catch (Exception e) {
                Log.e("XPermission", "Please check the onGranted() method body for bugs.", e);
                if (mDenied != null) {
                    mDenied.onAction(permissionList);
                }
            }
        }
    }

    /**
     * Callback rejected state.
     */
    private void callbackFailed(List<String> deniedList) {
        if (mDenied != null) {
            mDenied.onAction(deniedList);
        }
    }

    /**
     * Get denied permissions.
     */
    private static List<String> getDeniedPermissions(PermissionChecker checker, Source source, String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!checker.hasPermission(source.getContext(), permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }
}