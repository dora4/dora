package dora.lifecycle.activity;

import android.app.Activity;
import android.os.Bundle;

import dora.util.LogUtils;

public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity mActivity;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onCreate", mActivity.getLocalClassName());
    }

    @Override
    public void onStart() {
        LogUtils.iformat(LogUtils.TAG, "%s - onStart", mActivity.getLocalClassName());
    }

    @Override
    public void onResume() {
        LogUtils.iformat(LogUtils.TAG, "%s - onResume", mActivity.getLocalClassName());
    }

    @Override
    public void onPause() {
        LogUtils.iformat(LogUtils.TAG, "%s - onPause", mActivity.getLocalClassName());
    }

    @Override
    public void onStop() {
        LogUtils.iformat(LogUtils.TAG, "%s - onStop", mActivity.getLocalClassName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onSaveInstanceState", mActivity.getLocalClassName());
    }

    @Override
    public void onDestroy() {
        LogUtils.iformat(LogUtils.TAG, "%s - onDestroy", mActivity.getLocalClassName());
        this.mActivity = null;
    }
}
