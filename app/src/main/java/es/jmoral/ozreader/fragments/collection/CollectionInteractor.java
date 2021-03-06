package es.jmoral.ozreader.fragments.collection;

import android.content.Context;
import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.ozreader.models.Comic;
import es.jmoral.ozreader.utils.CreateCBZUtils;

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
    void renameComic(@NonNull Context context, Comic comic);
    void exportAsCBZ(@NonNull ArrayList<String> files, @NonNull File cbzFile, CreateCBZUtils.OnCreatingCBZListener onCreatingCBZListener,
                     CreateCBZUtils.OnCreatedCBZListener onCreatedCBZListener);
    void deleteOriginalFile(@NonNull String pathFile);
}
