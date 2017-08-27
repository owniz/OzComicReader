package es.jmoral.ozreader.fragments.collection;

import android.view.Menu;

import java.io.File;
import java.util.ArrayList;

import es.jmoral.ozreader.models.Comic;
import es.jmoral.ozreader.utils.Constants;

/**
 * Created by owniz on 14/04/17.
 */

public interface CollectionView {
    enum SortOrder {
        SORT_TITLE, SORT_NEWEST, SORT_OLDEST;

        static SortOrder getEnumByString(String enumMode) {
            switch (enumMode) {
                case Constants.SORT_BY_TITLE:
                    return SortOrder.SORT_TITLE;
                case Constants.SORT_BY_NEWEST:
                    return SortOrder.SORT_NEWEST;
                case Constants.SORT_BY_OLDEST:
                    return SortOrder.SORT_OLDEST;
                default:
                    return SortOrder.SORT_TITLE;
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
    void showExtractingDialog(int progress);
    void showExportingDialog(Comic comic);
    void dismissDialog();
    void setMenuItemChecked(Menu menu);
    void renameComicTitle(Comic comic, int position);
    void askPermissionDeleteOriginalFile(String pathFile);
}
