package it.diegorigo.numbers;

import it.diegorigo.math.numbers.BigDecimalUtils;
import it.diegorigo.math.MathUtils;
import it.diegorigo.math.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MathUtilsTest {

    @Test
    void minTest() {
        List<BigDecimal> values = BigDecimalUtils.asBigDecimalList(2,
                                                                   5,
                                                                   -3);
        BigDecimal min = MathUtils.min(values);

        Assertions.assertEquals(new BigDecimal("-3"), min);
    }

    @Test
    void minTestNoValues() {
        List<BigDecimal> values = new ArrayList<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> MathUtils.min(values));
    }

    @Test
    void maxTest() {
        List<BigDecimal> values = BigDecimalUtils.asBigDecimalList(2,
                                                                   5,
                                                                   -3);
        BigDecimal max = MathUtils.max(values);

        Assertions.assertEquals(new BigDecimal("5"), max);
    }

    @Test
    void maxTestNoValues() {
        List<BigDecimal> values = new ArrayList<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> MathUtils.max(values));
    }

    @Test
    void linspaceTest() {
        BigDecimal a = new BigDecimal(0);
        BigDecimal b = new BigDecimal(10);
        int n = 10;

        List<BigDecimal> linspace = MathUtils.linspace(a, b, n);
        Assertions.assertEquals(n + 1, linspace.size());
        linspace.forEach(item -> {
            Assertions.assertNotNull(item);
            Assertions.assertTrue(item.compareTo(a) >= 0);
            Assertions.assertTrue(item.compareTo(b) <= 0);
        });
    }

    @Test
    void sumTest() {
        BigDecimal a = new BigDecimal(3);
        BigDecimal b = new BigDecimal(10);

        BigDecimal sum = MathUtils.sum(a, b);
        Assertions.assertEquals(a.add(b), sum);
    }

    @Test
    void sumMultipleValuesTest() {
        BigDecimal a = new BigDecimal(3);
        BigDecimal b = new BigDecimal(10);
        BigDecimal c = new BigDecimal(6);
        BigDecimal d = new BigDecimal(8);

        BigDecimal sum = MathUtils.sum(a, b, c, d);
        Assertions.assertEquals(a.add(b).add(c).add(d), sum);
    }

    @Test
    void meanTest() {
        BigDecimal a = new BigDecimal(3);
        BigDecimal b = new BigDecimal(10);
        BigDecimal c = new BigDecimal(6);
        BigDecimal d = new BigDecimal(8);

        BigDecimal mean = MathUtils.mean(a, b, c, d);
        Assertions.assertEquals(new BigDecimal("6.75"), mean);
    }

    @Test
    void meanTestWithOneValue() {
        BigDecimal a = new BigDecimal(3);

        BigDecimal mean = MathUtils.mean(a);
        Assertions.assertEquals(a, mean);
    }

    @Test
    void meanTestWithNoValues() {

        Assertions.assertThrows(IllegalArgumentException.class,
                                MathUtils::mean);
    }

    @Test
    void centersTest() {
        List<BigDecimal> values = BigDecimalUtils.asBigDecimalList(3,
                                                                   7,
                                                                   9);

        List<BigDecimal> centers = MathUtils.centers(values);
        Assertions.assertEquals(2, centers.size());
        Assertions.assertEquals(new BigDecimal(5), centers.get(0));
        Assertions.assertEquals(new BigDecimal(8), centers.get(1));

    }

    @Test
    void computeHistogramTest() {
        List<BigDecimal> values = BigDecimalUtils.asBigDecimalList(1,
                                                                   1.3,
                                                                   3.2,
                                                                   4.6,
                                                                   4.7,
                                                                   5);
        List<Point> points = MathUtils.computeHistogram(values, 4);

        Point point1 = points.get(0);
        Assertions.assertEquals(new BigDecimal("1.5"), point1.getX());
        Assertions.assertEquals(new BigDecimal(2), point1.getY());

        Point point2 = points.get(1);
        Assertions.assertEquals(new BigDecimal("2.5"), point2.getX());
        Assertions.assertEquals(new BigDecimal(0), point2.getY());

        Point point3 = points.get(2);
        Assertions.assertEquals(new BigDecimal("3.5"), point3.getX());
        Assertions.assertEquals(new BigDecimal(1), point3.getY());

        Point point4 = points.get(3);
        Assertions.assertEquals(new BigDecimal("4.5"), point4.getX());
        Assertions.assertEquals(new BigDecimal(3), point4.getY());
    }

    @Test
    void computeHistogramTestWithLowN() {
        List<BigDecimal> values = BigDecimalUtils.asBigDecimalList(1);
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> MathUtils.computeHistogram(
                                        values,
                                        1));
    }

    @Test
    void computeHistogramWithDefinedRangeTest() {
        List<BigDecimal> values = BigDecimalUtils.asBigDecimalList(1,
                                                                   1.3,
                                                                   3.2,
                                                                   4.6,
                                                                   4.7,
                                                                   5);
        List<Point> points = MathUtils.computeHistogram(values,
                                                        new BigDecimal(
                                                                1),
                                                        new BigDecimal(
                                                                5),
                                                        4);

        Point point1 = points.get(0);
        Assertions.assertEquals(new BigDecimal("1.5"), point1.getX());
        Assertions.assertEquals(new BigDecimal(2), point1.getY());

        Point point2 = points.get(1);
        Assertions.assertEquals(new BigDecimal("2.5"), point2.getX());
        Assertions.assertEquals(new BigDecimal(0), point2.getY());

        Point point3 = points.get(2);
        Assertions.assertEquals(new BigDecimal("3.5"), point3.getX());
        Assertions.assertEquals(new BigDecimal(1), point3.getY());

        Point point4 = points.get(3);
        Assertions.assertEquals(new BigDecimal("4.5"), point4.getX());
        Assertions.assertEquals(new BigDecimal(3), point4.getY());
    }


}
