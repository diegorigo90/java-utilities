package it.diegorigo.files;

import it.diegorigo.exceptions.UtilityException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
                throw new UtilityException("There is a problem with folder creation", e);
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

    public static List<FileInfo> getFilesList(Path path) {
        List<FileInfo> files = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);
        try (Stream<Path> walk = Files.walk(path)) {
            walk.forEach(item -> {
                File file = item.toFile();
                if (file.isFile()) {
                    long fileSize = getFileSize(item);
                    FileInfo info = new FileInfo();
                    info.setName(file.getName());
                    info.setPath(file.getPath());
                    info.setSize(fileSize);
                    files.add(info);
                }
                counter.addAndGet(1);
                if (counter.get() %1000 == 0){
                    System.out.println(counter + " files analized");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Files analyze ended\n");

        return files;
    }

    public static void getFilesSizeSorted(Path path) {
        getFilesList(path).stream()
                          .sorted(Comparator.comparing(FileInfo::getSize,
                                                       Comparator.reverseOrder()))
                          .forEach(item -> System.out.println(formatFileSize(item.getSize()) + " ----> " + item.getName()));

    }

    public static void getTop10FilesSizeSorted(Path path) {
        getFilesList(path).stream()
                          .sorted(Comparator.comparing(FileInfo::getSize,
                                                       Comparator.reverseOrder()))
                          .limit(10)
                          .forEach(item -> System.out.println(formatFileSize(item.getSize()) + " ----> " + item.getName() + " ("+item.getPath() + ")"));

    }

    private static long getFileSize(Path item) {
        try {
            return Files.size(item);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatFileSize(long bytes) {
        final String[] units = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};

        int unitIndex = 0;
        double size = bytes;

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.1f %s", size, units[unitIndex]);
    }

    public static String fileAsString(Path path) throws UtilityException {
        try {
            byte[] fileBytes = Files.readAllBytes(path);
            return new String(fileBytes);
        } catch (IOException e) {
            throw new UtilityException("Errore di lettura del file");
        }
    }

    public static String filenameWithoutExtension(File file){
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(0, dotIndex);
        } else {
            return fileName;
        }
    }

    public static void open(File file) throws UtilityException {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            throw new UtilityException("Non è stato possibile aprire il file selezionato",e);
        }
    }
}
