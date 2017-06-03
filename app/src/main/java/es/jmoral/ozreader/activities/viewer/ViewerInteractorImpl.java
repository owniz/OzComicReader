package es.jmoral.ozreader.activities.viewer;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import es.jmoral.ozreader.database.ComicDBHelper;
import es.jmoral.ozreader.models.Comic;
import es.jmoral.ozreader.utils.SimpleComicReaderUtils;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by owniz on 16/04/17.
 */

class ViewerInteractorImpl implements ViewerInteractor {
    @Override
    public void readComic(String comicPath, int numPages, OnReadComicListener onReadComicListener) {
        ArrayList<String> pages = new ArrayList<>();

        File[] comicPages = new File(comicPath).listFiles();

        if (comicPages[0].isDirectory())
            comicPages = new File(comicPages[0].getAbsolutePath()).listFiles();

        for (File page : comicPages) {
            if (SimpleComicReaderUtils.isValidFormat(page.getAbsolutePath()))
                pages.add(page.getAbsolutePath());
        }

        Collections.sort(pages);
        onReadComicListener.onReadComicOk(pages);
    }

    @Override
    public void setCurrentPage(@NonNull Context context, Comic comic) {
        cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).put(comic);
    }
}
