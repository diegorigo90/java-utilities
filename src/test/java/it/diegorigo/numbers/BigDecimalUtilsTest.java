package it.diegorigo.numbers;

import it.diegorigo.math.numbers.BigDecimalUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class BigDecimalUtilsTest {

    @Test
    void sqrtTest(){
        BigDecimal value = BigDecimal.valueOf(2);
        BigDecimal sqrt = BigDecimalUtils.sqrt(value);
        Assertions.assertEquals(Math.sqrt(2), sqrt.doubleValue());
    }

    @Test
    void sqrtNegativeNumberTest(){
        BigDecimal value = BigDecimal.valueOf(-1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> BigDecimalUtils.sqrt(value));
    }

    @Test
    void sqrtNTest(){
        BigDecimal value = BigDecimalUtils.sqrt(BigDecimal.valueOf(2),3);

        Assertions.assertEquals(Math.pow(2,1d/3), value.doubleValue());
    }

    @Test
    void toBigDecimalFromStringTest() {
        List<String> values = Arrays.asList("1", "2", "5");

        List<BigDecimal> result = BigDecimalUtils.toBigDecimal(values);
        Assertions.assertEquals(values.size(), result.size());
        IntStream.range(0, values.size()).forEach(i -> {
            BigDecimal res = result.get(i);
            String exp = values.get(i);
            Assertions.assertNotNull(res);
            Assertions.assertEquals(new BigDecimal(exp), res);
        });
    }

    @Test
    void toBigDecimalFromBigIntegerTest() {
        List<BigInteger> values = Arrays.asList(BigInteger.valueOf(3),BigInteger.valueOf(5));

        List<BigDecimal> result = BigDecimalUtils.toBigDecimal(values);
        Assertions.assertEquals(values.size(), result.size());
        IntStream.range(0, values.size()).forEach(i -> {
            BigDecimal res = result.get(i);
            BigInteger exp = values.get(i);
            Assertions.assertNotNull(res);
            Assertions.assertEquals(new BigDecimal(exp), res);
        });
    }

    @Test
    void toBigDecimalFromIntegerTest() {
        List<Integer> values = Arrays.asList(1, 2, 3);

        List<BigDecimal> result = BigDecimalUtils.toBigDecimal(values);
        Assertions.assertEquals(values.size(), result.size());
        IntStream.range(0, values.size()).forEach(i -> {
            BigDecimal res = result.get(i);
            Integer exp = values.get(i);
            Assertions.assertNotNull(res);
            Assertions.assertEquals(new BigDecimal(exp), res);
        });
    }

    @Test
    void toBigDecimalFromDoubleTest() {
        List<Double> values = Arrays.asList(1.2, 2.31, 3.2);

        List<BigDecimal> result = BigDecimalUtils.toBigDecimal(values);
        Assertions.assertEquals(values.size(), result.size());
        IntStream.range(0, values.size()).forEach(i -> {
            BigDecimal res = result.get(i);
            Double exp = values.get(i);
            Assertions.assertNotNull(res);
            Assertions.assertEquals(new BigDecimal(exp), res);
        });
    }
    @Test
    void toBigDecimalFromBooleanTest() {
        List<Boolean> values = Arrays.asList(true, false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> BigDecimalUtils.toBigDecimal(values));
    }
}
