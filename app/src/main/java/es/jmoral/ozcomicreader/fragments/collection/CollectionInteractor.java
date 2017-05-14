package es.jmoral.ozcomicreader.fragments.collection;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.ozcomicreader.models.Comic;

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
        void onComicError(String errorMessage);
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
    void retrieveComic(@NonNull Context context, File file, OnRetrieveComicListener onRetrieveComicListener,
                       final ComicExtractionUpdateListener comicExtractionUpdateListener);
    void saveComic(@NonNull Context context, Comic comic, OnSaveComicListener onSaveComicListener);
    void deleteComic(@NonNull Context context, Comic comic, OnDeleteComicListener onDeleteComicListener);
    void deleteComic(@NonNull Context context, @NonNull String comicPath);
}
