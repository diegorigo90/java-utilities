package it.diegorigo.strings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringsUtilsTest {

    @Test
    void capitalizeTest(){
        String text = "holiday";
        String result = StringUtils.capitalize(text);

        Assertions.assertEquals("Holiday",result);
    }

    @Test
    void toCamelCaseTest(){
        String text = "ciccio bello";
        String result = StringUtils.toCamelCase(text);

        Assertions.assertEquals("CiccioBello",result);
    }

    @Test
    void leftPadTest(){
        String result = StringUtils.leftPad(9,"0",5);

        Assertions.assertEquals("00009",result);
    }
}
