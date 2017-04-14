package es.jmoral.simplecomicreader.fragments.collection;

import java.util.ArrayList;

import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 14/04/17.
 */

interface CollectionView {
    enum SortOrder {
        SORT_TITLE, SORT_NEWEST, SORT_OLDEST
    }

    void readSavedComics();
    void inflateCards(ArrayList<Comic> comics);
    void addComic();
    void openComic();
    void deleteComic();
    void orderComic(SortOrder sortOrder);
}
