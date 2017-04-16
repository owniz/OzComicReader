package es.jmoral.simplecomicreader.activities.viewer;

/**
 * Created by owniz on 16/04/17.
 */

interface ViewerInteractor {
    interface OnComicOpenedListener {
        void onComicOponenedOK();
        void onComicOponenedEror();
    }

    void showOpenendComic();
}
