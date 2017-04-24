package es.jmoral.simplecomicreader.activities.viewer;

import java.util.ArrayList;

import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 16/04/17.
 */

interface ViewerView {
    void showComic(ArrayList<String> pathImages);
    void updateComic(Comic comic);
}
