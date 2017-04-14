package es.jmoral.simplecomicreader.fragments.collection;

/**
 * Created by owniz on 14/04/17.
 */

interface CollectionPresenter {
    void readSavedComics();
    void addComic();
    void deleteComic();
    void onDestroy();
}
