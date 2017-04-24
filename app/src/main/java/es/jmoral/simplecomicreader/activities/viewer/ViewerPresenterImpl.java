package es.jmoral.simplecomicreader.activities.viewer;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 16/04/17.
 */

class ViewerPresenterImpl implements ViewerPresenter, ViewerInteractor.OnReadComicListener, ViewerInteractor.OnSetCurrentPageListener {
    private ViewerView viewerView;
    private ViewerInteractor viewerInteractor;

    public ViewerPresenterImpl(ViewerView viewerView) {
        this.viewerView = viewerView;
        this.viewerInteractor = new ViewerInteractorImpl();
    }

    @Override
    public void readComic(String comicPath, int numPages) {
        viewerInteractor.readComic(comicPath, numPages, this);
    }

    @Override
    public void setCurrentPage(@NonNull Context context, Comic comic, int pageOnExits) {
        viewerInteractor.setCurrentPage(context, comic, pageOnExits, this);
    }

    @Override
    public void onDestroy() {
        viewerView = null;
    }

    @Override
    public void onReadComicOk(ArrayList<String> pathImages) {
        viewerView.showComic(pathImages);
    }

    @Override
    public void onReadComicError() {
        // unused
    }

    @Override
    public void onSetCurrentPageOk(Comic comic) {
        viewerView.updateComic(comic);
    }

    @Override
    public void onSetCurrentPageError() {
        // unused
    }
}
