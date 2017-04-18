package es.jmoral.simplecomicreader.activities.viewer;

import java.util.ArrayList;

/**
 * Created by owniz on 16/04/17.
 */

interface ViewerInteractor {
    interface OnReadComicListener {
        void onReadComicOK(ArrayList<String> pathImages);
        void onReadComicEror();
    }

    void readComic(String comicPath, OnReadComicListener onReadComicListener);
}
