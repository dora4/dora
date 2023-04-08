package dora.lifecycle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import dora.util.LogUtils;

public class FragmentDelegateImpl implements FragmentDelegate {

    private Fragment mFragment;

    public FragmentDelegateImpl(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void onAttach(Context context) {
        LogUtils.iformat(LogUtils.TAG, "%s - onAttach", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onCreate", mFragment.getClass().getSimpleName());
        // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建时重复利用已经创建的 Fragment。
        // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
        // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
        // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
        mFragment.setRetainInstance(true);
    }

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onCreateView", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onActivityCreate", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        LogUtils.iformat(LogUtils.TAG, "%s - onStart", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        LogUtils.iformat(LogUtils.TAG, "%s - onResume", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        LogUtils.iformat(LogUtils.TAG, "%s - onPause", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        LogUtils.iformat(LogUtils.TAG, "%s - onStop", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtils.iformat(LogUtils.TAG, "%s - onSaveInstanceState", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        LogUtils.iformat(LogUtils.TAG, "%s - onDestroyView", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        LogUtils.iformat(LogUtils.TAG, "%s - onDestroy", mFragment.getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        LogUtils.iformat(LogUtils.TAG, "%s - onDetach", mFragment.getClass().getSimpleName());
        this.mFragment = null;
    }

    /**
     * Return true if the fragment is currently added to its activity.
     */
    @Override
    public boolean isAdded() {
        return mFragment != null && mFragment.isAdded();
    }
}
