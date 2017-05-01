package es.jmoral.simplecomicreader.fragments.collection;

import java.io.File;
import java.util.ArrayList;

import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 14/04/17.
 */

public interface CollectionView {
    enum SortOrder {
        SORT_TITTLE, SORT_NEWEST, SORT_OLDEST;

        static SortOrder getEnumByString(String enumMode) {
            switch (enumMode) {
                case "1":
                    return SortOrder.SORT_TITTLE;
                case "2":
                    return SortOrder.SORT_NEWEST;
                case "3":
                    return SortOrder.SORT_OLDEST;
                default:
                    return SortOrder.SORT_TITTLE;
            }
        }
    }

    void readSavedComics();
    void inflateCards(ArrayList<Comic> comics);
    void addComic(File file);
    void updateCards(Comic comic);
    void openComic(Comic comic);
    void deleteComic(Comic comic, int position);
    void showErrorMessage(String errorMessage);
    void orderComic(SortOrder sortOrder);
    void dismissDialog();
}
