package it.diegorigo.strings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringsUtilsTest {

    @Test
    void capitalizeTest(){
        String text = "holiday";
        String capitalizedText = StringsUtils.capitalize(text);

        Assertions.assertEquals("Holiday",capitalizedText);
    }
}
