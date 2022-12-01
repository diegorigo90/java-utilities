package it.diegorigo.numbers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class BigDecimalUtilsTest {

    @Test
    void sqrtTest(){
        BigDecimal value = BigDecimalUtils.sqrt(BigDecimal.valueOf(2));

        Assertions.assertEquals(Math.sqrt(2), value.doubleValue());
    }

    @Test
    void sqrtNTest(){
        BigDecimal value = BigDecimalUtils.sqrt(BigDecimal.valueOf(2),3);

        Assertions.assertEquals(Math.pow(2,1d/3), value.doubleValue());
    }
}
