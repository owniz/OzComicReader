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

        static SortOrder getEnumByString(String a) {
            SortOrder sortOrder = SortOrder.SORT_TITTLE;

            switch (a) {
                case "1":
                    sortOrder = SortOrder.SORT_TITTLE;
                    break;
                case "2":
                    sortOrder = SortOrder.SORT_NEWEST;
                    break;
                case "3":
                    sortOrder = SortOrder.SORT_OLDEST;
                    break;
            }

            return  sortOrder;
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
}
