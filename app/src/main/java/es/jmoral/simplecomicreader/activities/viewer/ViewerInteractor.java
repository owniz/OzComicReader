package es.jmoral.simplecomicreader.activities.viewer;

import java.util.ArrayList;

/**
 * Created by owniz on 16/04/17.
 */

interface ViewerInteractor {
    interface OnReadComicListener {
        void onReadComicOK(ArrayList<String> pathImages);
        void onReadComicError();
    }

    void readComic(String comicPath, int numPages, OnReadComicListener onReadComicListener);
}
