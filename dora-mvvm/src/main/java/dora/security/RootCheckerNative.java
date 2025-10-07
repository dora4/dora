package dora.security;

public class RootCheckerNative {

    private static boolean libraryLoaded = false;

    /**
     * Loads the C/C++ libraries statically
     */
    static {
        try {
            System.loadLibrary("rootChecker");
            libraryLoaded = true;
        } catch (UnsatisfiedLinkError e) {
        }
    }

    public boolean wasNativeLibraryLoaded() {
        return libraryLoaded;
    }

    public native int checkForRoot(Object[] pathArray);

    public native void setLogDebugMessages(boolean logDebugMessages);
}
