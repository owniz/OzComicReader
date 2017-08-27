package es.jmoral.ozreader.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by owniz on 6/08/17.
 */

public class CreateCBZUtils {
    public static void createCBZ(ArrayList<String> files, File zipFile) {
        int buffer = 4096;

        try {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[buffer];
            for (int i = 0; i < files.size(); i++) {
               FileInputStream fis = new FileInputStream(files.get(i));
               origin = new BufferedInputStream(fis, buffer);
               ZipEntry entry = new ZipEntry(files.get(i).substring(files.get(i).lastIndexOf("/") + 1));
               out.putNextEntry(entry);

               int count;
               while ((count = origin.read(data, 0, buffer)) != -1) {
                  out.write(data, 0, count);
               }

               origin.close();
            }

            out.close();
            dest.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> listFilesForFolder(final File folder) {
        ArrayList<String> files = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory())
                listFilesForFolder(fileEntry);
            else
                files.add(folder.getAbsolutePath() + "/" + fileEntry.getName());
        }
        return files;
    }
}
