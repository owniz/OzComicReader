package es.jmoral.ozreader.fragments.collection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.dmoral.toasty.Toasty;
import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.utils.MD5;
import es.jmoral.ozreader.R;
import es.jmoral.ozreader.activities.main.MainActivity;
import es.jmoral.ozreader.activities.viewer.ViewerActivity;
import es.jmoral.ozreader.adapters.ComicAdapter;
import es.jmoral.ozreader.fragments.BaseFragment;
import es.jmoral.ozreader.models.Comic;
import es.jmoral.ozreader.utils.Constants;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionFragment extends BaseFragment implements CollectionView, ComicExtractionUpdateListener {
    @BindView(R.id.recyclerViewComics) RecyclerView recyclerViewComics;

    private CollectionPresenter collectionPresenter;
    private MaterialDialog progressDialog;
    private boolean overwriting;
    private boolean extractionStarted;
    private String cachedComicPath;
    private String cachedExtractionPath;
    private String originalFilePath;
    private MaterialDialog askForAddComicDialog;
    private ArrayList<String> files = new ArrayList<>();
    private static String nameFilePath = "";

    public static Fragment newInstance() {
        return new CollectionFragment();
    }

    public static Fragment newInstance(String pathFromFile) {
        CollectionFragment collectionFragment = new CollectionFragment();
        Bundle args = new Bundle();
        args.putString(Constants.PATH_FROM_FILE, pathFromFile);
        collectionFragment.setArguments(args);
        return collectionFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @LayoutRes int contentViewId, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, contentViewId, container, savedInstanceState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar();
        setHasOptionsMenu(true);
        collectionPresenter = new CollectionPresenterImpl(this);

        if (getArguments() != null && getArguments().getString(Constants.PATH_FROM_FILE) != null) {
            String nameFilePathTemp = getArguments().getString(Constants.PATH_FROM_FILE);
            if (!nameFilePath.equals(nameFilePathTemp))
                loadComicFromExternalPath();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main_sort_menu, menu);
        setMenuItemChecked(menu);
    }

    @Override
    public void setMenuItemChecked(Menu menu) {
        final String order = Prefs.with(getActivity()).read(Constants.KEY_PREFERENCES_SORT, Constants.SORT_BY_TITLE);

        switch (order) {
            case Constants.SORT_BY_TITLE:
                menu.findItem(R.id.sort_by_tittle).setChecked(true);
                break;
            case Constants.SORT_BY_NEWEST:
                menu.findItem(R.id.sort_by_newest).setChecked(true);
                break;
            case Constants.SORT_BY_OLDEST:
                menu.findItem(R.id.sort_by_oldest).setChecked(true);
                break;
            default:
                menu.findItem(R.id.sort_by_tittle).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_tittle:
                orderComic(SortOrder.SORT_TITLE);
                item.setChecked(true);
                return true;
            case R.id.sort_by_newest:
                orderComic(SortOrder.SORT_NEWEST);
                item.setChecked(true);
                return true;
            case R.id.sort_by_oldest:
                orderComic(SortOrder.SORT_OLDEST);
                item.setChecked(true);
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
        registerForContextMenu(recyclerViewComics);
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
        }, SortOrder.getEnumByString(Prefs.with(getContext()).read(Constants.KEY_PREFERENCES_SORT, Constants.SORT_BY_TITLE))));
    }

    @Override
    public void addComic(final File file) {
        getActivity().setRequestedOrientation(
                getResources().getBoolean(R.bool.landscape)
                        ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        showExtractingDialog((int) file.length() / 1024);

        new Thread(new Runnable() {
            @Override
            public void run() {
                cachedComicPath = file.getAbsolutePath();
                cachedExtractionPath = getActivity().getFilesDir() + "/" + MD5.calculateMD5(file);
                CollectionFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        collectionPresenter.addComic(file, CollectionFragment.this);
                    }
                });
            }
        }).start();

        originalFilePath = file.getAbsolutePath();
    }

    @Override
    public void updateCards(Comic comic) {
        if (cachedComicPath != null)
            cachedComicPath = null;

        if (cachedExtractionPath != null)
            cachedExtractionPath = null;

        dismissDialog();

        if (overwriting) {
            ((ComicAdapter) recyclerViewComics.getAdapter()).removeComic(comic, false);
            overwriting = false;
        }

        ((ComicAdapter) recyclerViewComics.getAdapter()).insertComic(comic,
                SortOrder.getEnumByString(Prefs.with(getContext()).read(Constants.KEY_PREFERENCES_SORT)));

        askPermissionDeleteOriginalFile(originalFilePath);
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
                .cancelable(false)
                .canceledOnTouchOutside(false)
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
                .show();
    }

    @Override
    public void renameComicTitle(final Comic comic, final int position) {
        final MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.new_title), comic.getTitle(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        comic.setTitle(input.toString());
                        collectionPresenter.renameComic(comic);
                        ((ComicAdapter) recyclerViewComics.getAdapter()).updateTitleBehaviour(position);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toasty.info(getContext(), getString(R.string.rename_cancelled)).show();
                    }
                })
                .show();


        if (dialog.getInputEditText() != null)
            dialog.getInputEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // unused
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    final View okButton = dialog.getActionButton(DialogAction.POSITIVE);

                    if (charSequence.toString().isEmpty())
                        okButton.setEnabled(false);
                    else
                        okButton.setEnabled(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // unused
                }
            });
    }

    private void exportAsCBZ(int position) {
        Comic comic = ((ComicAdapter) recyclerViewComics.getAdapter()).getComic(position);
        collectionPresenter.exportAsCBZ(listFilesForFolder(new File(comic.getFilePath()), comic),
                new File("/sdcard/Download/" + comic.getTitle() + ".cbz"));
    }

    private ArrayList<String> listFilesForFolder(final File folder, Comic comic) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory())
                listFilesForFolder(fileEntry, comic);
            else
                files.add(comic.getFilePath() + "/" + fileEntry.getName());
        }
        return files;
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        dismissDialog();

        switch (errorMessage) {
            case Constants.COMIC_ALREADY_ADDED_MSG:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.overwrite_comic)
                        .content(R.string.comic_already_added)
                        .cancelable(false)
                        .canceledOnTouchOutside(false)
                        .positiveText(R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                overwriting = true;
                                collectionPresenter.deleteComic(cachedExtractionPath);
                                addComic(new File(cachedComicPath));
                            }
                        })
                        .negativeText(R.string.cancel)
                        .show();
                break;
            default:
                Toasty.error(getContext(), getString(R.string.archive_corrupted), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void orderComic(SortOrder sortOrder) {
        ((ComicAdapter) recyclerViewComics.getAdapter()).orderComic(sortOrder, true);
    }

    @Override
    public void showExtractingDialog(int progress) {
        extractionStarted = false;
        progressDialog = new MaterialDialog.Builder(getContext())
                .content(R.string.preparing_extraction)
                .progress(false, progress, true) // KiB
                .progressNumberFormat("%1d/%2d KiB")
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .show();
    }

    public void askPermissionDeleteOriginalFile(final String pathFile) {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDeleteOriginalFileDialog(pathFile);
                            }
                        }, 250);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        new MaterialDialog.Builder(getActivity())
                                .title(R.string.why_need_write)
                                .content(R.string.write_permissions)
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
                                .positiveText(R.string.ok)
                                .show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void showDeleteOriginalFileDialog(final String pathFile) {
        final String[] separatedPath = pathFile.split("/");
        final String action = Prefs.with(getContext()).read(Constants.KEY_PREFERENCES_DELETE_DIALOG_ACTION, Constants.DELETE_DIALOG_ACTION_NONE);

        if (action.equals(Constants.DELETE_DIALOG_ACTION_NONE)) {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.delete_original_file)
                    .content(getResources().getString(R.string.are_you_sure_delete, separatedPath[separatedPath.length - 1]))
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .positiveText(R.string.delete)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (dialog.isPromptCheckBoxChecked())
                                Prefs.with(getContext()).write(Constants.KEY_PREFERENCES_DELETE_DIALOG_ACTION, Constants.DELETE_DIALOG_ACTION_DELETE);
                            collectionPresenter.deleteOriginalFile(pathFile);
                        }
                    })
                    .negativeText(R.string.keep_original)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (dialog.isPromptCheckBoxChecked())
                                Prefs.with(getContext()).write(Constants.KEY_PREFERENCES_DELETE_DIALOG_ACTION, Constants.DELETE_DIALOG_ACTION_KEEP);
                        }
                    })
                    .checkBoxPromptRes(R.string.remember_action, false, null)
                    .show();
        } else {
            if (action.equals(Constants.DELETE_DIALOG_ACTION_DELETE))
                collectionPresenter.deleteOriginalFile(pathFile);
        }
    }

    @Override
    public void dismissDialog() {
        if (android.provider.Settings.System.getInt( // check if the rotate sensor is active
                getActivity().getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (askForAddComicDialog != null)
            askForAddComicDialog.dismiss();

        collectionPresenter.onDestroy();
    }

    @Override
    public void onExtractionUpdate(int i) {
        if (!extractionStarted) {
            progressDialog.setContent(getString(R.string.extracting_comic));
            extractionStarted = true;
        }

        progressDialog.incrementProgress(i - progressDialog.getCurrentProgress());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int position = ((ComicAdapter) this.recyclerViewComics.getAdapter()).getComicPosition();

        switch (item.getItemId()) {
            case R.id.rename_title:
                renameComicTitle(((ComicAdapter) recyclerViewComics.getAdapter()).getComic(position), position);
                return true;
            case R.id.export_cbz:
                exportAsCBZ(position);
                return true;
            case R.id.delete_comic:
                deleteComic(((ComicAdapter) recyclerViewComics.getAdapter()).getComic(position), position);
                ((ComicAdapter) recyclerViewComics.getAdapter()).removeComic(position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadComicFromExternalPath() {
        if (getArguments() != null && getArguments().getString(Constants.PATH_FROM_FILE) != null ) {
            String tempName = Uri.parse(getArguments().getString(Constants.PATH_FROM_FILE)).getLastPathSegment();

            if (Prefs.with(getContext()).readBoolean(Constants.KEY_PREFERENCES_SHOW_DIALOG, true)) {
                askForAddComicDialog = new MaterialDialog.Builder(getContext())
                        .title(R.string.add_comic_title)
                        .content(getResources().getString(R.string.are_you_sure, tempName))
                        .cancelable(false)
                        .canceledOnTouchOutside(false)
                        .positiveText(R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Prefs.with(getContext()).writeBoolean(Constants.KEY_PREFERENCES_SHOW_DIALOG, !dialog.isPromptCheckBoxChecked());
                                addComic(new File(Uri.parse(getArguments().getString(Constants.PATH_FROM_FILE)).getPath()));
                                nameFilePath = getArguments().getString(Constants.PATH_FROM_FILE);
                            }
                        })
                        .negativeText(R.string.cancel)
                        .checkBoxPromptRes(R.string.dont_ask_again, false, null)
                        .show();
            } else {
                addComic(new File(Uri.parse(getArguments().getString(Constants.PATH_FROM_FILE)).getPath()));
                nameFilePath = getArguments().getString(Constants.PATH_FROM_FILE);
            }
        }
    }
}
