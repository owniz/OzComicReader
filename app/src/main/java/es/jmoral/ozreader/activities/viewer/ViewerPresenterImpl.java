package es.jmoral.ozreader.activities.viewer;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import es.jmoral.ozreader.models.Comic;

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
    public void readComic(String comicPath, int numPages) {
        viewerInteractor.readComic(comicPath, numPages, this);
    }

    @Override
    public void setCurrentPage(Comic comic) {
        viewerInteractor.setCurrentPage(((AppCompatActivity)viewerView), comic);
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
}
