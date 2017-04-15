package es.jmoral.simplecomicreader.fragments.collection;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import es.jmoral.mortadelo.Mortadelo;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.simplecomicreader.database.ComicDBHelper;
import es.jmoral.simplecomicreader.models.Comic;
import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by owniz on 14/04/17.
 */

class CollectionInteractorImpl implements CollectionInteractor {
    private String coverTitle;

    @Override
    public void readSavedComics(@NonNull Context context, OnReadSavedComicsListener onReadSavedComicsListener) {
        ArrayList<Comic> comics = new ArrayList<>();

        Cursor comicCursor = cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getReadableDatabase()).query(Comic.class).getCursor();

        try {
            QueryResultIterable<Comic> itr = cupboard().withCursor(comicCursor).iterate(Comic.class);

            for (Comic comic : itr) {
                comics.add(comic);
            }
        } finally {
            comicCursor.close();
        }

        onReadSavedComicsListener.onComicsReadOk(comics);
    }

    @Override
    public void retrieveComic(@NonNull final Context context, final File file, final OnRetrieveComicListener onRetrieveComicListener) {
        new Mortadelo(new ComicReceivedListener() {
            @Override
            public void onComicReceived(es.jmoral.mortadelo.models.Comic comic) {
                String[] fileName = file.getName().split("\\.");
                String title = "";

                storeImage(context, comic.getPages().get(0));

                for (int i = 0; i < fileName.length - 1; i++) {
                    title += fileName[i] + ((i == fileName.length - 2) ? "" : ".");
                }

                onRetrieveComicListener.onComicReceived(new Comic(context.getFilesDir() + "/covers/" + coverTitle + ".png",
                        file.getAbsolutePath(), System.currentTimeMillis(), title, comic.getPages().size(), 1));
            }

            @Override
            public void onComicFailed(String s) {

            }
        }).obtainComic(file.getAbsolutePath());
    }

    @Override
    public void saveComic(@NonNull Context context, Comic comic, OnSaveComicListener onSaveComicListener) {
        cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).put(comic);
        onSaveComicListener.onSavedComicOk(comic);
    }

    @Override
    public void deleteComic(Comic comic, OnDeleteComicListener onDeleteComicListener) {

    }

    private void storeImage(Context context, Bitmap image) {
        File coversFolder = new File(context.getFilesDir() + "/covers");

        if (!coversFolder.exists())
            coversFolder.mkdirs();

        String currentTime = String.valueOf(System.currentTimeMillis());

        try {
            coverTitle = Base64.encodeToString(currentTime.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        File pictureFile = new File(coversFolder + "/" + coverTitle + ".png");

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (IOException ignored) {}
    }
}
