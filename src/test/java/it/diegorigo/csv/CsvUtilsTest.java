package it.diegorigo.csv;

import it.diegorigo.exceptions.UtilityException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilsTest {

    @Test
    void toColumns() throws URISyntaxException, UtilityException {
        URL resource = getClass().getClassLoader().getResource("testCsv.csv");
        assert resource != null;
        File file = new File(resource.toURI());
        assertTrue(file.exists(), "Il file non esiste nel filesystem!");
        List<CsvColumnData> columns = CsvUtils.toColumns(file);
        System.out.println(columns);
    }
}