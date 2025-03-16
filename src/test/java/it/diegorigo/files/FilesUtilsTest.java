package it.diegorigo.files;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

class FilesUtilsTest {

    public static void main(String[] args) throws IOException {
        List<Path> modifiedFiles = FilesUtils.getModifiedFiles(Paths.get(
                                                                       "C:\\Gestionale Aziendale\\cartellaCondivisaSuNas\\preventivi"),
                                                               LocalDateTime.now().minusDays(10));
        modifiedFiles.forEach(System.out::println);
    }

}