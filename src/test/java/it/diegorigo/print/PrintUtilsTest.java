package it.diegorigo.print;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrintUtilsTest {

    @Test
    void h1Test(){
        Assertions.assertDoesNotThrow(() -> PrintUtils.h1("Test"));
    }
}
