package it.diegorigo.math;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MathUtils {

    public static BigDecimal sum(BigDecimal... values) {
        return Arrays.stream(values)
                     .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal min(List<BigDecimal> values) {
        return values.stream()
                     .min(Comparator.naturalOrder())
                     .orElseThrow(() -> new IllegalArgumentException(
                             "It is not possible to compute minimum of selected values"));
    }

    public static BigDecimal max(List<BigDecimal> values) {
        return values.stream()
                     .max(Comparator.naturalOrder())
                     .orElseThrow(() -> new IllegalArgumentException(
                             "It is not possible to compute maximum of selected values"));
    }

    public static BigDecimal mean(BigDecimal... values) {

        int length = values.length;
        if (length < 1) {
            throw new IllegalArgumentException(
                    "Expected at least 1 value");
        }
        return sum(values).divide(new BigDecimal(length));
    }


    public static List<BigDecimal> linspace(BigDecimal min,
                                            BigDecimal max,
                                            int n) {
        BigDecimal dt = (max.subtract(min)).divide(new BigDecimal(n));
        return IntStream.rangeClosed(0, n)
                        .mapToObj(i -> min.add(dt.multiply(new BigDecimal(
                                i))))
                        .collect(Collectors.toList());
    }

    public static List<BigDecimal> centers(List<BigDecimal> ticks) {
        return IntStream.range(0, ticks.size() - 1).mapToObj(i -> {
            BigDecimal start = ticks.get(i);
            BigDecimal end = ticks.get(i + 1);
            return mean(start, end);
        }).collect(Collectors.toList());
    }
    public static List<Point> computeHistogram(List<BigDecimal> values,
                                               BigDecimal start,
                                               BigDecimal end,
                                               int n) {

        if (n < 2) {
            throw new IllegalArgumentException(
                    "Expected n to be an integer greater than 1");
        }

        List<BigDecimal> ticks = linspace(start, end, n);


        return IntStream.range(0, ticks.size() - 1).mapToObj(i -> {
            BigDecimal tickStart = ticks.get(i);
            BigDecimal tickEnd = ticks.get(i + 1);
            BigDecimal x = mean(tickStart, tickEnd);
            BigDecimal y = new BigDecimal(i > 0 ? countPointsInInterval(
                    values,
                    tickStart,
                    tickEnd) : countPointsInIntervalFirstIncluded(
                    values,
                    tickStart,
                    tickEnd));
            return new Point(x, y);
        }).collect(Collectors.toList());
    }

    public static List<Point> computeHistogram(List<BigDecimal> values,
                                               int n) {
        BigDecimal max = max(values);
        BigDecimal min = min(values);

        return computeHistogram(values,min,max, n);
    }

    private static long countPointsInIntervalFirstIncluded(List<BigDecimal> values,
                                                           BigDecimal start,
                                                           BigDecimal end) {
        return values.stream()
                     .filter(item -> item.compareTo(start) >= 0 && item.compareTo(
                             end) <= 0)
                     .count();
    }

    private static long countPointsInInterval(List<BigDecimal> values,
                                              BigDecimal start,
                                              BigDecimal end) {
        return values.stream()
                     .filter(item -> item.compareTo(start) > 0 && item.compareTo(
                             end) <= 0)
                     .count();
    }
}
