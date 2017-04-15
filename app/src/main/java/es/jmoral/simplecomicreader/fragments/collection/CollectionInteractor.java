package es.jmoral.simplecomicreader.fragments.collection;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 14/04/17.
 */

interface CollectionInteractor {
    interface OnReadSavedComicsListener {
        void onComicsReadOk(ArrayList<Comic> comics);
        void onComicsReadError();
    }

    interface OnRetrieveComicListener {
        void onComicReceived(Comic comic);
        void onComicError();
    }

    interface OnSaveComicListener {
        void onSavedComicOk(Comic comic);
        void onSavedComicError();
    }

    interface OnDeleteComicListener {
        void onDeleteOk();
        void onDeleteError();
    }

    void readSavedComics(@NonNull Context context, OnReadSavedComicsListener onReadSavedComicsListener);
    void retrieveComic(@NonNull Context context, File file, OnRetrieveComicListener onRetrieveComicListener);
    void saveComic(@NonNull Context context, Comic comic, OnSaveComicListener onSaveComicListener);
    void deleteComic(Comic comic, OnDeleteComicListener onDeleteComicListener);
}
