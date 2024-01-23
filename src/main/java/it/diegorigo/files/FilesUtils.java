package it.diegorigo.files;

import it.diegorigo.exceptions.UtilityException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesUtils {


    public static void checkFileExists(String string) throws UtilityException {
        checkFileExists(Path.of(string));
    }

    public static void checkFileExists(Path path) throws UtilityException {
        if (!path.toFile().exists()) {
            throw new UtilityException("Not valid path: '" + path + "'");
        }
    }

    public static void generateFolderIfNotExists(Path path) throws UtilityException {
        if (!path.toFile().exists()) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new UtilityException("There is a problem with folder creation",e);
            }
        }
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            throw new IllegalArgumentException("The selected path has no extension");
        }
    }
}
