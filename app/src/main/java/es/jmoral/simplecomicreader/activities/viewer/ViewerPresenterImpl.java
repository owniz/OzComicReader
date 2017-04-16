package es.jmoral.simplecomicreader.activities.viewer;

/**
 * Created by owniz on 16/04/17.
 */

class ViewerPresenterImpl implements ViewerPresenter, ViewerInteractor.OnComicOpenedListener {
    private ViewerView viewerView;
    private ViewerInteractor viewerInteractor;

    public ViewerPresenterImpl(ViewerView viewerView, ViewerInteractor viewerInteractor) {
        this.viewerView = viewerView;
        this.viewerInteractor = viewerInteractor;
    }

    @Override
    public void openComic() {

    }

    @Override
    public void onDestroy() {
        viewerView = null;
    }

    @Override
    public void onComicOponenedOK() {

    }

    @Override
    public void onComicOponenedEror() {

    }
}
