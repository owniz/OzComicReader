package es.jmoral.simplecomicreader.activities.viewer;

import java.util.ArrayList;

/**
 * Created by owniz on 16/04/17.
 */

class ViewerInteractorImpl implements ViewerInteractor {
    @Override
    public void readComic(String comicPath, OnReadComicListener onReadComicListener) {
        ArrayList<String> test = new ArrayList<>();

        test.add(comicPath + "/0.png");
        test.add(comicPath + "/1.png");
        test.add(comicPath + "/2.png");

        onReadComicListener.onReadComicOK(test);
    }
}
