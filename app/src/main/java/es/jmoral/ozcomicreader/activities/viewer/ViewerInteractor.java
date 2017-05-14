package es.jmoral.ozcomicreader.activities.viewer;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import es.jmoral.ozcomicreader.models.Comic;

/**
 * Created by owniz on 16/04/17.
 */

interface ViewerInteractor {
    interface OnReadComicListener {
        void onReadComicOk(ArrayList<String> pathImages);
        void onReadComicError();
    }

    void readComic(String comicPath, int numPages, OnReadComicListener onReadComicListener);
    void setCurrentPage(@NonNull Context context, Comic comic);
}
