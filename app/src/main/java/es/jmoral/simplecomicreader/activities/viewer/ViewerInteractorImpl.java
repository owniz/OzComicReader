package es.jmoral.simplecomicreader.activities.viewer;

import java.util.ArrayList;

/**
 * Created by owniz on 16/04/17.
 */

class ViewerInteractorImpl implements ViewerInteractor {
    @Override
    public void readComic(String comicPath, int numPages, OnReadComicListener onReadComicListener) {
        ArrayList<String> test = new ArrayList<>();

        for (int i = 0; i < numPages; i++) {
            test.add(comicPath + "/" + i + ".png");
        }

        onReadComicListener.onReadComicOK(test);
    }
}
