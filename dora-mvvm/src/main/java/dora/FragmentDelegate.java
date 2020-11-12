package dora;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public interface FragmentDelegate {

    String CACHE_KEY = "FragmentDelegate";

    void onAttach(Context context);

    void onCreate(Bundle savedInstanceState);

    void onCreateView(View view, Bundle savedInstanceState);

    void onActivityCreated(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroyView();

    void onDestroy();

    void onDetach();

    boolean isAdded();
}