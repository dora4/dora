package dora.permission.checker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import java.util.List;

class LocationFineTest implements PermissionTest {

    private Context mContext;

    LocationFineTest(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean test() throws Throwable {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        boolean gpsProvider = providers.contains(LocationManager.GPS_PROVIDER);
        boolean passiveProvider = providers.contains(LocationManager.PASSIVE_PROVIDER);
        if (gpsProvider || passiveProvider) {
            return true;
        }

        PackageManager packageManager = mContext.getPackageManager();
        boolean gpsHardware = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if (!gpsHardware) {
            return true;
        }

        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}