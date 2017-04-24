package es.jmoral.simplecomicreader.activities.viewer;

import android.content.Context;
import android.support.annotation.NonNull;

import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 16/04/17.
 */

interface ViewerPresenter {
    void readComic(String pathComic, int numPages);
    void setCurrentPage(Comic comic, int pageOnExits);
    void onDestroy();
}
