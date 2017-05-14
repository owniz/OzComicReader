package es.jmoral.ozcomicreader.fragments.collection;

import android.support.annotation.NonNull;

import java.io.File;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.ozcomicreader.models.Comic;

/**
 * Created by owniz on 14/04/17.
 */

interface CollectionPresenter {
    void readSavedComics();
    void addComic(@NonNull File file, ComicExtractionUpdateListener comicExtractionUpdateListener);
    void deleteComic(@NonNull Comic comic);
    void deleteComic(@NonNull String comicPath);
    void onDestroy();
}
