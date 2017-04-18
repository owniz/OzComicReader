package es.jmoral.simplecomicreader.activities.viewer;

import java.util.ArrayList;

/**
 * Created by owniz on 16/04/17.
 */

class ViewerPresenterImpl implements ViewerPresenter, ViewerInteractor.OnReadComicListener {
    private ViewerView viewerView;
    private ViewerInteractor viewerInteractor;

    public ViewerPresenterImpl(ViewerView viewerView) {
        this.viewerView = viewerView;
        this.viewerInteractor = new ViewerInteractorImpl();
    }

    @Override
    public void readComic(String comicPath) {
        viewerInteractor.readComic(comicPath, this);
    }

    @Override
    public void onDestroy() {
        viewerView = null;
    }

    @Override
    public void onReadComicOK(ArrayList<String> pathImages) {
        viewerView.showComic(pathImages);
    }

    @Override
    public void onReadComicError() {
        // unused
    }
}
