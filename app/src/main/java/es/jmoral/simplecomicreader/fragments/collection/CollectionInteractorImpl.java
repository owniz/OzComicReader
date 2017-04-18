package es.jmoral.simplecomicreader.fragments.collection;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import es.jmoral.mortadelo.Mortadelo;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.database.ComicDBHelper;
import es.jmoral.simplecomicreader.models.Comic;
import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by owniz on 14/04/17.
 */

class CollectionInteractorImpl implements CollectionInteractor {
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

                if (!storeComic(context, comic.getPages(), comic.getMD5hash(), onRetrieveComicListener))
                    return;

                for (int i = 0; i < fileName.length - 1; i++) {
                    title += fileName[i] + ((i == fileName.length - 2) ? "" : ".");
                }

                onRetrieveComicListener.onComicReceived(new Comic(context.getFilesDir() + "/" + comic.getMD5hash() + "/0.png",
                        context.getFilesDir() + "/" + comic.getMD5hash(), System.currentTimeMillis(), title, comic.getPages().size(), 1));
            }

            @Override
            public void onComicFailed(String s) {
                // unused
            }
        }).obtainComic(file.getAbsolutePath());
    }

    @Override
    public void saveComic(@NonNull Context context, Comic comic, OnSaveComicListener onSaveComicListener) {
        cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).put(comic);
        onSaveComicListener.onSavedComicOk(comic);
    }

    @Override
    public void deleteComic(@NonNull Context context, Comic comic, OnDeleteComicListener onDeleteComicListener) {
        File dir = new File(comic.getFilePath());
        if (dir.isDirectory()) {
            String[] children = dir.list();

            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }

            dir.delete();
        }
        cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).delete(comic);
        onDeleteComicListener.onDeleteOk();
    }

    private boolean storeComic(Context context, ArrayList<Bitmap> images, String folderPath, OnRetrieveComicListener onRetrieveComicListener) {
        File coversFolder = new File(context.getFilesDir() + "/" + folderPath);

        if (!coversFolder.exists()) {
            coversFolder.mkdirs();
        } else {
            onRetrieveComicListener.onComicError(context.getString(R.string.comic_already_added));
            return false;
        }

        for (int i = 0; i < images.size(); i++) {
            File pictureFile = new File(coversFolder + "/" + i + ".png");

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                images.get(i).compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (IOException ioe) {
                return false;
            }
        }
        return true;
    }
}
