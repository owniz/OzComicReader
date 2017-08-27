package es.jmoral.ozreader.fragments.collection;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.ozreader.models.Comic;
import es.jmoral.ozreader.utils.CreateCBZUtils;

/**
 * Created by owniz on 14/04/17.
 */

class CollectionPresenterImpl implements CollectionPresenter, CollectionInteractor.OnReadSavedComicsListener, CollectionInteractor.OnRetrieveComicListener,
        CollectionInteractor.OnSaveComicListener, CollectionInteractor.OnDeleteComicListener {
    private CollectionView collectionView;
    private CollectionInteractor collectionInteractor;

    CollectionPresenterImpl(CollectionView collectionView) {
        this.collectionView = collectionView;
        this.collectionInteractor = new CollectionInteractorImpl();
    }

    @Override
    public void onComicsReadOk(ArrayList<Comic> comics) {
        collectionView.inflateCards(comics);
    }

    @Override
    public void onComicsReadError() {
        // unused
    }

    @Override
    public void readSavedComics() {
        collectionInteractor.readSavedComics(((CollectionFragment) collectionView).getContext(), this);
    }

    @Override
    public void addComic(@NonNull File file, ComicExtractionUpdateListener comicExtractionUpdateListener) {
        collectionInteractor.retrieveComic(((CollectionFragment) collectionView).getContext(), file, this,
                comicExtractionUpdateListener);
    }

    @Override
    public void deleteComic(@NonNull Comic comic) {
        collectionInteractor.deleteComic(((CollectionFragment) collectionView).getContext(), comic, this);
    }

    @Override
    public void deleteComic(@NonNull String comicPath) {
        collectionInteractor.deleteComic(((CollectionFragment) collectionView).getContext(), comicPath);
    }

    @Override
    public void renameComic(@NonNull Comic comic) {
        collectionInteractor.renameComic(((CollectionFragment) collectionView).getContext(), comic);
    }

    @Override
    public void deleteOriginalFile(@NonNull String pathFile) {
        collectionInteractor.deleteOriginalFile(pathFile);
    }

    @Override
    public void exportAsCBZ(@NonNull ArrayList<String> files, @NonNull File cbzFile, CreateCBZUtils.OnCreatingCBZListener onCreatingCBZListener,
                            CreateCBZUtils.OnCreatedCBZListener onCreatedCBZListener) {
        collectionInteractor.exportAsCBZ(files, cbzFile, onCreatingCBZListener, onCreatedCBZListener);
    }

    @Override
    public void onDestroy() {
        collectionView = null;
    }

    @Override
    public void onComicReceived(Comic comic) {
        collectionInteractor.saveComic(((CollectionFragment) collectionView).getContext(), comic, this);
    }

    @Override
    public void onComicError(String errorMessage) {
        collectionView.showErrorMessage(errorMessage);
    }

    @Override
    public void onSavedComicOk(Comic comic) {
        collectionView.updateCards(comic);
    }

    @Override
    public void onSavedComicError() {
        // unused
    }

    @Override
    public void onDeleteOk() {
        // unused
    }

    @Override
    public void onDeleteError() {
        // unused
    }
}
