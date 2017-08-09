package es.jmoral.ozreader.fragments.collection;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.ozreader.models.Comic;

/**
 * Created by owniz on 14/04/17.
 */

interface CollectionPresenter {
    void readSavedComics();
    void addComic(@NonNull File file, ComicExtractionUpdateListener comicExtractionUpdateListener);
    void deleteComic(@NonNull Comic comic);
    void deleteComic(@NonNull String comicPath);
    void renameComic(@NonNull Comic comic);
    void deleteOriginalFile(@NonNull String pathFile);
    void exportAsCBZ(@NonNull ArrayList<String> files, @NonNull File cbzFile);
    void onDestroy();
}
