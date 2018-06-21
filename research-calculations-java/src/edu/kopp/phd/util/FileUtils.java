package edu.kopp.phd.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    public static String readFile(String path, Charset encoding) {
        byte[] encoded;

        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new String(encoded, encoding);
    }

    public static void writeFile(String path, String text) {
        try (PrintWriter out = new PrintWriter(path)) {
            out.println(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
