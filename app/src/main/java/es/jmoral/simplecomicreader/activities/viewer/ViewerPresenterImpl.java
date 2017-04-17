package es.jmoral.simplecomicreader.activities.viewer;

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
    public void readComic() {
        viewerInteractor.showReadComic();
    }

    @Override
    public void onDestroy() {
        viewerView = null;
    }

    @Override
    public void onReadComicOK() {
        viewerView.showComic();
    }

    @Override
    public void onReadComicEror() {

    }
}
