package dora;

import android.app.Activity;
import android.os.Bundle;

import dora.log.Logger;

public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity mActivity;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.iformat(Logger.TAG, "%s - onCreate", mActivity.getLocalClassName());
    }

    @Override
    public void onStart() {
        Logger.iformat(Logger.TAG, "%s - onStart", mActivity.getLocalClassName());
    }

    @Override
    public void onResume() {
        Logger.iformat(Logger.TAG, "%s - onResume", mActivity.getLocalClassName());
    }

    @Override
    public void onPause() {
        Logger.iformat(Logger.TAG, "%s - onPause", mActivity.getLocalClassName());
    }

    @Override
    public void onStop() {
        Logger.iformat(Logger.TAG, "%s - onStop", mActivity.getLocalClassName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Logger.iformat(Logger.TAG, "%s - onSaveInstanceState", mActivity.getLocalClassName());
    }

    @Override
    public void onDestroy() {
        Logger.iformat(Logger.TAG, "%s - onDestroy", mActivity.getLocalClassName());
        this.mActivity = null;
    }
}
