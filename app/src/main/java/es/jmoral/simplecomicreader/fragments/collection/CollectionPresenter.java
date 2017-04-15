package es.jmoral.simplecomicreader.fragments.collection;

import java.io.File;

/**
 * Created by owniz on 14/04/17.
 */

interface CollectionPresenter {
    void readSavedComics();
    void addComic(File file);
    void deleteComic();
    void onDestroy();
}
