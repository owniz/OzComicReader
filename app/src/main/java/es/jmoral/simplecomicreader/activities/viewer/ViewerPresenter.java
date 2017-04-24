package es.jmoral.simplecomicreader.activities.viewer;

import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 16/04/17.
 */

interface ViewerPresenter {
    void readComic(String pathComic, int numPages);
    void setCurrentPage(Comic comic);
    void onDestroy();
}
