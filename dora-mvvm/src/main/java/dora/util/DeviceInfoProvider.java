package dora.util;

import android.os.Build;

public final class DeviceInfoProvider {

    private DeviceInfoProvider() {
    }

    public static class DeviceInfo {

        private String brand;
        private String model;
        private String manufacturer;
        private String os;
        private String osVersion;
        private int sdkInt;
        private String appVersion;

        public DeviceInfo(String brand,
        String model,
        String manufacturer,
        String os,
        String osVersion,
        int sdkInt,
        String appVersion) {
            this.brand = brand;
            this.model = model;
            this.manufacturer = manufacturer;
            this.os = os;
            this.osVersion = osVersion;
            this.sdkInt = sdkInt;
            this.appVersion = appVersion;
        }

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public String getOs() {
            return os;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public int getSdkInt() {
            return sdkInt;
        }

        public String getAppVersion() {
            return appVersion;
        }
    }

    public static DeviceInfo collect() {
        return new DeviceInfo(
                safe(Build.BRAND),
        safe(Build.MODEL),
        safe(Build.MANUFACTURER),
        "Android",
        safe(Build.VERSION.RELEASE),
        Build.VERSION.SDK_INT,
        ApkUtils.getVersionName()
        );
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
