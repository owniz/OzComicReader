package es.jmoral.simplecomicreader.fragments.collection;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import es.jmoral.mortadelo.Mortadelo;
import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
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
    public void retrieveComic(@NonNull final Context context, final File file, final OnRetrieveComicListener onRetrieveComicListener,
                              final ComicExtractionUpdateListener comicExtractionUpdateListener) {
        new Mortadelo(context, new ComicReceivedListener() {
            @Override
            public void onComicReceived(es.jmoral.mortadelo.models.Comic comic) {
                String[] fileName = file.getName().split("\\.");
                String title = "";

                for (int i = 0; i < fileName.length - 1; i++) {
                    title += fileName[i] + ((i == fileName.length - 2) ? "" : ".");
                }

                onRetrieveComicListener.onComicReceived(new Comic(comic.getPages().get(0),
                        context.getFilesDir() + "/" + comic.getMD5hash(),
                        System.currentTimeMillis(), title, comic.getPages().size(), 1));
            }

            @Override
            public void onComicFailed(String s) {
                // unused
            }
        }, comicExtractionUpdateListener).obtainComic(file.getAbsolutePath());
    }

    @Override
    public void saveComic(@NonNull Context context, Comic comic, OnSaveComicListener onSaveComicListener) {
        Long _id = cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).put(comic);
        comic.set_id(_id);
        onSaveComicListener.onSavedComicOk(comic);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteComic(@NonNull Context context, Comic comic, OnDeleteComicListener onDeleteComicListener) {
        File dir = new File(comic.getFilePath());

        if (dir.isDirectory()) {
            String[] children = dir.list();

            for (String aChildren : children) {
                new File(dir, aChildren).delete();
            }

            dir.delete();
        }

        cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).delete(comic);
        onDeleteComicListener.onDeleteOk();
    }
}
