package es.jmoral.simplecomicreader.activities.viewer;

/**
 * Created by owniz on 16/04/17.
 */

interface ViewerPresenter {
    void readComic(String pathComic);
    void onDestroy();
}
