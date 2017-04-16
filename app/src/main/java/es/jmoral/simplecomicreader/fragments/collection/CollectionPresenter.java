package es.jmoral.simplecomicreader.fragments.collection;

import java.io.File;

import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 14/04/17.
 */

interface CollectionPresenter {
    void readSavedComics();
    void addComic(File file);
    void deleteComic(Comic comic);
    void onDestroy();
}
