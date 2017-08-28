package es.jmoral.ozreader.utils;

import android.os.AsyncTask;

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
    public interface OnCreatingCBZListener {
        void onCreatingCBZComic(int size);
    }

    public interface OnCreatedCBZListener {
        void onCreatedCBZComic();
        void onCreateCBZFailed();
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

    public static int folderSizeInKiB(String folderName) {
        File folder = new File(folderName);

        long length = 0;
        for (File file : folder.listFiles()) {
            if (file.isFile())
                length+= file.length();
            else
                length+= folderSizeInKiB(file.getName());
        }
        return (int) length / 1024;
    }

    public static void createCBZ(ArrayList<String> files, File zipFile, OnCreatingCBZListener onCreatingCBZListener,
                                 OnCreatedCBZListener onCreatedCBZListener) {
        new CreatingCBZ(files, onCreatingCBZListener, onCreatedCBZListener).execute(zipFile);
    }

    private static class CreatingCBZ extends AsyncTask<File, Integer, Boolean> {
        ArrayList<String> files = new ArrayList<>();
        OnCreatingCBZListener onCreatingCBZListener;
        OnCreatedCBZListener onCreatedCBZListener;
        boolean result = true;

        private CreatingCBZ(ArrayList<String> files, OnCreatingCBZListener onCreatingCBZListener, OnCreatedCBZListener onCreatedCBZListener) {
            this.files = files;
            this.onCreatingCBZListener = onCreatingCBZListener;
            this.onCreatedCBZListener = onCreatedCBZListener;
        }

        @Override
        protected Boolean doInBackground(File... zipFile) {
            int buffer = 4096;

            try {
                BufferedInputStream origin;
                FileOutputStream dest = new FileOutputStream(zipFile[0]);
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

                    publishProgress((int) new File(files.get(i)).length() / 1024);
                    origin.close();
                }

                out.close();
                dest.close();
            } catch (Exception ignored) {
                result = false;
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            onCreatingCBZListener.onCreatingCBZComic(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (result)
                onCreatedCBZListener.onCreatedCBZComic();
            else
                onCreatedCBZListener.onCreateCBZFailed();
        }
    }
}
