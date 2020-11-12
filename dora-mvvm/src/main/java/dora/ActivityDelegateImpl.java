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
        Logger.info("%s - onCreate", mActivity.getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        Logger.info("%s - onStart", mActivity.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        Logger.info("%s - onResume", mActivity.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        Logger.info("%s - onPause", mActivity.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        Logger.info("%s - onStop", mActivity.getClass().getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Logger.info("%s - onSaveInstanceState", mActivity.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        Logger.info("%s - onDestroy", mActivity.getClass().getSimpleName());
        this.mActivity = null;
    }
}
