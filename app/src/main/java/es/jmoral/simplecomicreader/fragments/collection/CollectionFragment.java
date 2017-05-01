package es.jmoral.simplecomicreader.fragments.collection;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.dmoral.toasty.Toasty;
import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.activities.main.MainActivity;
import es.jmoral.simplecomicreader.activities.viewer.ViewerActivity;
import es.jmoral.simplecomicreader.adapters.ComicAdapter;
import es.jmoral.simplecomicreader.fragments.BaseFragment;
import es.jmoral.simplecomicreader.models.Comic;
import es.jmoral.simplecomicreader.utils.Constants;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionFragment extends BaseFragment implements CollectionView, ComicExtractionUpdateListener {
    @BindView(R.id.recyclerViewComics) RecyclerView recyclerViewComics;

    private CollectionPresenter collectionPresenter;
    private MaterialDialog progressDialog;

    public static Fragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar();
        setHasOptionsMenu(true);
        collectionPresenter = new CollectionPresenterImpl(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main_sort_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_tittle:
                orderComic(SortOrder.SORT_TITTLE);
                return true;
            case R.id.sort_by_newest:
                orderComic(SortOrder.SORT_NEWEST);
                return true;
            case R.id.sort_by_oldest:
                orderComic(SortOrder.SORT_OLDEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_collection, container, savedInstanceState);
    }

    @Override
    protected void setUpViews() {
        recyclerViewComics.setLayoutManager(new GridLayoutManager(getContext(), (getResources().getBoolean(R.bool.landscape) ? 2 : 1)
                + (getResources().getBoolean(R.bool.isTablet) ? 1 : 0)) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                int scrollRange = super.scrollVerticallyBy(dy, recycler, state);
                int overScroll = dy - scrollRange;

                if (overScroll > 0)
                    ((MainActivity) getActivity()).getFAB().hide();
                else if (overScroll < 0)
                    ((MainActivity) getActivity()).getFAB().show();

                return scrollRange;
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteComic(((ComicAdapter) recyclerViewComics.getAdapter()).getComic(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                ((ComicAdapter) recyclerViewComics.getAdapter()).removeComic(viewHolder.getAdapterPosition());
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerViewComics);

        readSavedComics();
    }

    @Override
    protected void setListeners() {
        // unused
    }

    @Override
    public void readSavedComics() {
        collectionPresenter.readSavedComics();
    }

    @Override
    public void inflateCards(ArrayList<Comic> comics) {
        recyclerViewComics.setAdapter(new ComicAdapter(comics, new ComicAdapter.OnComicClickListener() {
            @Override
            public void onComicClicked(Comic comic) {
                openComic(comic);
            }
        }, SortOrder.getEnumByString(Prefs.with(getContext()).read(Constants.KEY_PREFERENCES_SORT, "1"))));
    }

    @Override
    public void addComic(File file) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        progressDialog = new MaterialDialog.Builder(getContext())
                .content(R.string.extracting_comic)
                .progress(false, (int) file.length() / 1024, true) // KiB
                .progressNumberFormat("%1d/%2d KiB")
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .show();
        collectionPresenter.addComic(file, this);
    }

    @Override
    public void updateCards(Comic comic) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        progressDialog.dismiss();
        ((ComicAdapter) recyclerViewComics.getAdapter()).insertComic(comic,
                SortOrder.getEnumByString(Prefs.with(getContext()).read(Constants.KEY_PREFERENCES_SORT)));
    }

    @Override
    public void openComic(Comic comic) {
        Intent intent = new Intent(getContext(), ViewerActivity.class);
        intent.putExtra(Constants.KEY_COMIC, comic);
        startActivityForResult(intent, Constants.REQUEST_CODE_COMIC);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_COMIC && resultCode == RESULT_OK && data != null)
            if (data.hasExtra(Constants.KEY_COMIC))
                ((ComicAdapter) recyclerViewComics.getAdapter()).updateComic((Comic) data.getParcelableExtra(Constants.KEY_COMIC));
    }

    @Override
    public void deleteComic(final Comic comic, final int position) {
        new MaterialDialog.Builder(getContext())
                .content(R.string.delete_comic)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        collectionPresenter.deleteComic(comic);
                        Toasty.success(getContext(), getString(R.string.comic_deleted)).show();
                    }
                })
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ((ComicAdapter) recyclerViewComics.getAdapter()).insertComicAtPosition(comic, position);
                        Toasty.info(getContext(), getString(R.string.deleted_cancelled)).show();
                    }
                })
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        progressDialog.dismiss();
        Toasty.error(getContext(), getString(R.string.archive_corrupted), Toast.LENGTH_LONG).show();
    }

    @Override
    public void orderComic(SortOrder sortOrder) {
        ((ComicAdapter) recyclerViewComics.getAdapter()).orderComic(sortOrder, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        collectionPresenter.onDestroy();
    }

    @Override
    public void onExtractionUpdate(int i) {
        progressDialog.incrementProgress(i - progressDialog.getCurrentProgress());
    }
}
