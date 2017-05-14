package es.jmoral.ozcomicreader.activities.viewer;

import es.jmoral.ozcomicreader.models.Comic;

/**
 * Created by owniz on 16/04/17.
 */

interface ViewerPresenter {
    void readComic(String pathComic, int numPages);
    void setCurrentPage(Comic comic);
    void onDestroy();
}
