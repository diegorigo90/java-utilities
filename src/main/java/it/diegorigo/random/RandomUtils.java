package it.diegorigo.random;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomUtils {
    private static final Random random = new Random(new Date().getTime());

    public static Stream<Integer> randomIntegerStream(int n,
                                                      int bound) {
        return IntStream.range(0, n).mapToObj(i -> random.nextInt(bound));
    }

    public static Integer randomInteger(int bound) {
        return random.nextInt(bound);
    }

    public static List<Integer> randomIntegers(int n,
                                               int bound) {
        return randomIntegerStream(n, bound).collect(Collectors.toList());
    }

    public static <T> T randomSupplier(Supplier<T> supplier) {
        return random.nextBoolean() ? supplier.get() : null;
    }

    public static <T> List<T> randomSupplierList(Supplier<T> supplier,
                                                 int n) {
        return IntStream.range(0, n)
                        .mapToObj(i -> randomSupplier(supplier))
                        .collect(Collectors.toList());
    }

    public static boolean randomCoin(double probabilityOfSuccess) {
        checkProbability(probabilityOfSuccess);
        return Math.random() < probabilityOfSuccess;
    }

    private static void checkProbability(double probabilityOfSuccess) {
        if (probabilityOfSuccess < 0 || probabilityOfSuccess > 1) {
            throw new IllegalArgumentException("Expected a value between 0 and 1");
        }
    }

    public static <A> A getRandomElement(List<A> list) {
        int size = list.size();
        if (size == 0) {
            return null;
        }
        return list.get(randomInteger(size));
    }
}
