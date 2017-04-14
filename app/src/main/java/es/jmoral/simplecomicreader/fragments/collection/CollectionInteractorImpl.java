package es.jmoral.simplecomicreader.fragments.collection;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;

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
    public void retrieveComic(OnRetrieveComicListener onRetrieveComicListener) {
        //onRetrieveComicListener.onComicReceived(comic);
    }

    @Override
    public void saveComic(@NonNull Context context, Comic comic, OnSaveComicListener onSaveComicListener) {
        cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).put(comic);
    }

    @Override
    public void deleteComic(Comic comic, OnDeleteComicListener onDeleteComicListener) {

    }
}
