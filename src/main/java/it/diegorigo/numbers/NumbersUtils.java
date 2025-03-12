package it.diegorigo.numbers;

public class NumbersUtils {
    public static int getIntegerValue(Double doubleValue) {
        try {
            return doubleValue.intValue();
        } catch (Exception e) {
            return -1;
        }
    }
}
