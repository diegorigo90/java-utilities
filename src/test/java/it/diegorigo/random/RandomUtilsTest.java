package it.diegorigo.random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomUtilsTest {
    @Test
    void randomIntegersTest() {
        int n = 10;
        int bound = 5;

        List<Integer> list = RandomUtils.randomIntegers(n, bound);
        Assertions.assertEquals(n, list.size());
        list.forEach(i -> {
            Assertions.assertTrue(i >= 0, "Expected positive value");
            Assertions.assertTrue(i < bound,
                                  "Expected value less than " + bound);
            Assertions.assertTrue(i % 1 == 0,
                                  "Expected integer value");
        });
    }

    @Test
    void randomIntegerStreamTest() {
        int n = 10;
        int bound = 5;

        Stream<Integer> integers = RandomUtils.randomIntegerStream(n,
                                                                   bound);
        List<Integer> list = integers.collect(Collectors.toList());
        Assertions.assertEquals(n, list.size());
        list.forEach(i -> {
            Assertions.assertTrue(i >= 0, "Expected positive value");
            Assertions.assertTrue(i < bound,
                                  "Expected value less than " + bound);
            Assertions.assertTrue(i % 1 == 0,
                                  "Expected integer value");
        });
    }

    @Test
    void randomSupplierTest() {
        String value = "test";
        String result = RandomUtils.randomSupplier(() -> value);

        if (result != null) {
            Assertions.assertEquals(value, result);
        }
    }

    @Test
    void randomSupplierMultipleTest() {
        String value = "test";
        int n = 10;

        List<String> result = RandomUtils.randomSupplierList(() -> value,
                                                             n);
        result.forEach(item -> {
            if (item != null) {
                Assertions.assertEquals(value, item);
            }
        });
    }

    @Test
    void randomCoinNegativeProbabilityTest() {
        double p = -1;

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> RandomUtils.randomCoin(p));
    }

    @Test
    void randomCoinGreaterThanOneTest() {
        double p = 5;

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> RandomUtils.randomCoin(p));
    }

    @Test
    void randomCoinTest() {
        double p = 0.02;
        int n = 4000;

        double q = IntStream.range(0, n)
                              .mapToObj(i -> RandomUtils.randomCoin(p))
                              .filter(b -> b)
                              .count();

        double probability = q / n;
        Assertions.assertTrue(probability < p+0.1);
        Assertions.assertTrue(probability > p-0.1);
    }
}
