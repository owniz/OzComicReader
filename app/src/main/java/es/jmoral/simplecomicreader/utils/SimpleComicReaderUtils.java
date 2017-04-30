package es.jmoral.simplecomicreader.utils;

/**
 * Created by owniz on 30/04/17.
 */

public class SimpleComicReaderUtils {
    public static boolean isValidFormat(String fileName) {
        return fileName.toLowerCase().endsWith(".bmp")
                || fileName.toLowerCase().endsWith(".jpg")
                || fileName.toLowerCase().endsWith(".jpeg")
                || fileName.toLowerCase().endsWith(".gif")
                || fileName.toLowerCase().endsWith(".tif")
                || fileName.toLowerCase().endsWith(".tiff")
                || fileName.toLowerCase().endsWith(".png")
                || fileName.toLowerCase().endsWith(".raw");
    }
}
