package dora;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import dora.log.Logger;

public class FragmentDelegateImpl implements FragmentDelegate {

    private Fragment mFragment;

    public FragmentDelegateImpl(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public void onAttach(Context context) {
        Logger.info("%s - onAttach", mFragment.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.info("%s - onCreate", mFragment.toString());
        // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建时重复利用已经创建的 Fragment。
        // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
        // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
        // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
        mFragment.setRetainInstance(true);
    }

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        Logger.info("%s - onCreateView", mFragment.toString());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Logger.info("%s - onActivityCreate", mFragment.toString());
    }

    @Override
    public void onStart() {
        Logger.info("%s - onStart", mFragment.toString());
    }

    @Override
    public void onResume() {
        Logger.info("%s - onResume", mFragment.toString());
    }

    @Override
    public void onPause() {
        Logger.info("%s - onPause", mFragment.toString());
    }

    @Override
    public void onStop() {
        Logger.info("%s - onStop", mFragment.toString());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Logger.info("%s - onSaveInstanceState", mFragment.toString());
    }

    @Override
    public void onDestroyView() {
        Logger.info("%s - onDestroyView", mFragment.toString());
    }

    @Override
    public void onDestroy() {
        Logger.info("%s - onDestroy", mFragment.toString());
        this.mFragment = null;
    }

    @Override
    public void onDetach() {
        Logger.info("%s - onDetach", mFragment.toString());
    }

    /**
     * Return true if the fragment is currently added to its activity.
     */
    @Override
    public boolean isAdded() {
        return mFragment != null && mFragment.isAdded();
    }
}
