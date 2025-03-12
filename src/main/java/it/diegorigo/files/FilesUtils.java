package it.diegorigo.files;

import it.diegorigo.date.DateUtils;
import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.strings.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FilesUtils {

    private static final List<String> EXCEL_EXTENSIONS = List.of("ods", "xls", "xlsx");

    public static void checkFileExists(String string) throws UtilityException {
        checkFileExists(Path.of(string));
    }

    public static void checkFileExists(Path path) throws UtilityException {
        checkFileExists(path.toFile());
    }

    public static void checkFileExists(File file) throws UtilityException {
        if (!file.exists()) {
            throw new UtilityException("Folder not valid: '" + file.toPath() + "'");
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

    public static String getFileExtension(File file) throws UtilityException {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            throw new UtilityException("The selected path has no extension");
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
                if (counter.get() % 1000 == 0) {
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
                          .forEach(item -> System.out.println(formatFileSize(item.getSize()) + " ----> " + item.getName() + " (" + item.getPath() + ")"));

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

    public static String fileAsString(File file) throws UtilityException {
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            return new String(fileBytes);
        } catch (IOException e) {
            throw new UtilityException("Errore di lettura del file");
        }
    }

    public static String filenameWithoutExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(0, dotIndex);
        } else {
            return fileName;
        }
    }

    public static void openFile(File file) throws UtilityException {
        openFile(file.getAbsolutePath());
    }

    public static void openFolder(Path path) throws UtilityException {
        openFolder(path.toString());
    }

    public static void openFile(String path) throws UtilityException {
        try {
            Runtime.getRuntime().exec("explorer \"" + Paths.get(path) + "\"");
        } catch (IOException e) {
            throw new UtilityException("Error during file opening");
        }
    }

    public static void openFolder(String path) throws UtilityException {
        try {
            Runtime.getRuntime().exec("explorer \"" + Paths.get(path) + "\"");
        } catch (IOException e) {
            throw new UtilityException("Error during folder opening");
        }
    }

    public static boolean isExcel(File file) throws UtilityException {
        return StringUtils.containsIgnoreCase(EXCEL_EXTENSIONS, FilesUtils.getFileExtension(file));
    }

    public static List<File> getFilesWithExtension(Path path,
                                                   String extension) {
        File[] files = path.toFile().listFiles(getFilenameFilter(extension));
        if (files != null) {
            return List.of(files);
        }
        return new ArrayList<>();
    }

    private static FilenameFilter getFilenameFilter(String extension) {
        return (dir, name) -> name.toLowerCase().endsWith(extension);
    }

    public static List<Path> getFoldersList(String path) throws UtilityException {
        return getFoldersList(Paths.get(path));
    }

    public static List<Path> getFoldersList(Path path) throws UtilityException {
        List<Path> folders;
        try (Stream<Path> list = Files.list(path)) {
            folders = list.filter(item -> item.toFile().isDirectory())
                          .map(Path::toAbsolutePath)
                          .toList();
        } catch (IOException e) {
            throw new UtilityException("Error path not found: " + path, e);
        }
        return folders;
    }

    public static List<File> getExcelFiles(Path folderPath) throws UtilityException {
        List<File> files = new ArrayList<>();
        try (Stream<Path> paths = Files.list(folderPath)) {
            paths.map(Path::toFile).forEach(file -> {
                try {
                    if (FilesUtils.isExcel(file)) {
                        files.add(file);
                    }
                } catch (UtilityException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new UtilityException("Error during path reading");
        }
        return files;
    }

    public static void toFile(String content, File file){
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            System.out.println("Text written to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toFile(String content, Path path){
        File file = path.resolve(DateUtils.getTimestampAsString() + "_exported.json").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            System.out.println("Text written to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
