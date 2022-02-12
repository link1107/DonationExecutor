package igorlink;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArrayUtils {
    public static boolean contains(char @Nullable [] values, char toFind) {
        if(values == null) {
            return false;
        }

        for (char c : values) {
            if(c == toFind) {
                return true;
            }
        }

        return false;
    }

    public static boolean contains(int @Nullable [] values, int length, int toFind) {
        if(values == null) {
            return false;
        }

        for(int i = 0; i < length; i++) {
            if(values[i] == toFind) {
                return true;
            }
        }

        return false;
    }
}
