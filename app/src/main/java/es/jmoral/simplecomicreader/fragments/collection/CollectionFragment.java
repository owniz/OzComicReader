package es.jmoral.simplecomicreader.fragments.collection;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.adapters.ComicAdapter;
import es.jmoral.simplecomicreader.fragments.BaseFragment;
import es.jmoral.simplecomicreader.models.Comic;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionFragment extends BaseFragment implements CollectionView {
    @BindView(R.id.recyclerViewComics) RecyclerView recyclerViewComics;

    private CollectionPresenter collectionPresenter;

    public static Fragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        collectionPresenter = new CollectionPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_collection, container, savedInstanceState);
    }

    @Override
    protected void setUpViews() {
        recyclerViewComics.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        readSavedComics();
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void readSavedComics() {
        collectionPresenter.readSavedComics();
    }

    @Override
    public void inflateCards(ArrayList<Comic> comics) {
        recyclerViewComics.setAdapter(new ComicAdapter(comics));
    }

    @Override
    public void addComic() {
        collectionPresenter.addComic();
    }

    @Override
    public void openComic() {

    }

    @Override
    public void deleteComic() {
        collectionPresenter.deleteComic();
    }

    @Override
    public void orderComic(SortOrder sortOrder) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        collectionPresenter.onDestroy();
    }
}
