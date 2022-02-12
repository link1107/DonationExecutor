package igorlink;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CharArrayMap {
    private char[] keys;
    private char[][] values;

    private int size;

    public CharArrayMap() {
        this(4);
    }

    public CharArrayMap(int size) {
        keys = new char[size];
        values = new char[size][];
    }

    public void put(char key, char... value) {
        // если следующий индекс не занятый
        if(size < keys.length) {
            keys[size] = key;
            values[size] = value;

            size++;
        } else {
            growArrays();

            keys[size - 1] = key;
            values[size - 1] = value;
        }
    }

    public char @Nullable [] get(char key) {
        int index = binarySearch(keys, size - 1, key);
        if(index < 0) {
            return null;
        }

        return values[index];
    }

    public void sort() {
        quickSort(keys, values, 0, size - 1);
    }

    private void growArrays() {
        int newSize = keys.length + 1;
        char[] newKeys = new char[newSize];
        char[][] newValues = new char[newSize][];

        System.arraycopy(keys, 0, newKeys, 0, keys.length);
        System.arraycopy(values, 0, newValues, 0, keys.length);

        keys = newKeys;
        values = newValues;
        size = newSize;
    }

    private static int binarySearch(char @NotNull [] array, int size, char value) {
        int lo = 0;
        int hi = size - 1;

        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int midVal = array[mid];

            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal > value) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }

    // Типичный quicksort, но сразу два массива
    private static void quickSort(char @NotNull [] keys, char[] @NotNull [] values, int low, int high) {
        if (low < high) {
            int pi = partition(keys, values, low, high);

            quickSort(keys, values, low, pi - 1);
            quickSort(keys, values, pi + 1, high);
        }
    }

    private static void swap(char @NotNull [] keys, char[] @NotNull [] values, int i, int j) {
        {
            char temp = keys[i];
            keys[i] = keys[j];
            keys[j] = temp;
        }
        {
            char[] temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }
    }

    private static int partition(char @NotNull [] keys, char[] @NotNull [] values, int low, int high) {
        int pivot = keys[high];
        int i = (low - 1);

        for(int j = low; j <= high - 1; j++) {
            if (keys[j] < pivot) {
                i++;
                swap(keys, values, i, j);
            }
        }
        swap(keys, values, i + 1, high);
        return (i + 1);
    }
}
