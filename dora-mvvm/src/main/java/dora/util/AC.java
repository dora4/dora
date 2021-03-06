package dora.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A tool that can transform arrays and collections.
 */
public final class AC {

    private static AC sInstance;

    private AC() {
    }

    public static AC get() {
        if (sInstance == null) {
            synchronized (AC.class) {
                if (sInstance == null) {
                    sInstance = new AC();
                }
            }
        }
        return sInstance;
    }

    public <T> T[] toA(List<T> lists, T[] newArray) {
        return lists.toArray(newArray);
    }

    /**
     * Converts an array into a collection.
     */
    public <T> List<T> toC(T[] array) {
        return Arrays.asList(array);
    }

    public List<Integer> toC(int[] array) {
        List<Integer> collection = new ArrayList<>();
        for (int i : array) {
            collection.add(i);
        }
        return collection;
    }

    public List<Float> toC(float[] array) {
        List<Float> collection = new ArrayList<>();
        for (float f : array) {
            collection.add(f);
        }
        return collection;
    }

    public List<Double> toC(double[] array) {
        List<Double> collection = new ArrayList<>();
        for (Double d : array) {
            collection.add(d);
        }
        return collection;
    }

    public List<Short> toC(short[] array) {
        List<Short> collection = new ArrayList<>();
        for (Short s : array) {
            collection.add(s);
        }
        return collection;
    }

    public List<Long> toC(long[] array) {
        List<Long> collection = new ArrayList<>();
        for (Long l : array) {
            collection.add(l);
        }
        return collection;
    }

    public List<Byte> toC(byte[] array) {
        List<Byte> collection = new ArrayList<>();
        for (Byte b : array) {
            collection.add(b);
        }
        return collection;
    }

    public List<Boolean> toC(boolean[] array) {
        List<Boolean> collection = new ArrayList<>();
        for (Boolean b : array) {
            collection.add(b);
        }
        return collection;
    }

    public List<Character> toC(char[] array) {
        List<Character> collection = new ArrayList<>();
        for (Character c : array) {
            collection.add(c);
        }
        return collection;
    }
}
