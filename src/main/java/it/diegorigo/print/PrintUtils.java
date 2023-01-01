package it.diegorigo.print;

public class PrintUtils {

    private static final String line = "******************************************************************";
    private static final String newLine = "\n";

    public static void h1(String text){
        System.out.println(newLine + line + newLine + "* " +text + newLine + line);
    }
}
