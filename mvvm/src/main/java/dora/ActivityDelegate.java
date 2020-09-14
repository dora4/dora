package dora;

import android.os.Bundle;

public interface ActivityDelegate {

    String CACHE_KEY = "ActivityDelegate";

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
