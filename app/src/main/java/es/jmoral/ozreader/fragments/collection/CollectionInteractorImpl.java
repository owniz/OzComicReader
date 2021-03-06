package es.jmoral.ozreader.fragments.collection;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.jmoral.mortadelo.Mortadelo;
import es.jmoral.mortadelo.listeners.ComicExtractionUpdateListener;
import es.jmoral.mortadelo.listeners.ComicReceivedListener;
import es.jmoral.mortadelo.utils.MD5;
import es.jmoral.ozreader.database.ComicDBHelper;
import es.jmoral.ozreader.models.Comic;
import es.jmoral.ozreader.utils.Constants;
import es.jmoral.ozreader.utils.CreateCBZUtils;
import es.jmoral.ozreader.utils.SimpleComicReaderUtils;
import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by owniz on 14/04/17.
 */

class CollectionInteractorImpl implements CollectionInteractor {
    @Override
    public void readSavedComics(@NonNull Context context, OnReadSavedComicsListener onReadSavedComicsListener) {
        ArrayList<Comic> comics = new ArrayList<>();

        Cursor comicCursor = cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getReadableDatabase()).query(Comic.class).getCursor();

        try {
            QueryResultIterable<Comic> itr = cupboard().withCursor(comicCursor).iterate(Comic.class);

            for (Comic comic : itr) {
                comics.add(comic);
            }
        } finally {
            comicCursor.close();
        }

        onReadSavedComicsListener.onComicsReadOk(comics);
    }

    @Override
    public void retrieveComic(@NonNull final Context context, final File file, final OnRetrieveComicListener onRetrieveComicListener,
                              final ComicExtractionUpdateListener comicExtractionUpdateListener) {
        List<String> pagesFolder = Arrays.asList(context.getFilesDir().list());

        final String md5 = MD5.calculateMD5(file);

        if (pagesFolder.contains(md5)) {
            onRetrieveComicListener.onComicError(Constants.COMIC_ALREADY_ADDED_MSG);
            return;
        }

        new Mortadelo(context, new ComicReceivedListener() {
            @Override
            public void onComicReceived(es.jmoral.mortadelo.models.Comic comic) {
                String title = SimpleComicReaderUtils.getStringFromRegex(file.getName(), Constants.REGEX_TO_CLEAR_NAME);

                comic.setPages(cleanPages(comic.getPages()));

                if (comic.getPages().isEmpty()) {
                    deleteRecursive(new File(context.getFilesDir() + "/" + comic.getMD5hash()));
                    onRetrieveComicListener.onComicError("Empty pages");
                    return;
                }

                onRetrieveComicListener.onComicReceived(new Comic(comic.getPages().get(0),
                        context.getFilesDir() + "/" + comic.getMD5hash(),
                        System.currentTimeMillis(), title, comic.getPages().size(), 1));
            }

            @Override
            public void onComicFailed(String s) {
                deleteRecursive(new File(context.getFilesDir() + "/" + md5));
                onRetrieveComicListener.onComicError(s);
            }
        }, comicExtractionUpdateListener).obtainComic(file.getAbsolutePath());
    }

    @Override
    public void saveComic(@NonNull Context context, Comic comic, OnSaveComicListener onSaveComicListener) {
        Long _id = cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).put(comic);
        comic.set_id(_id);
        onSaveComicListener.onSavedComicOk(comic);
    }

    @Override
    public void deleteComic(@NonNull Context context, Comic comic, OnDeleteComicListener onDeleteComicListener) {
        File dir = new File(comic.getFilePath());
        deleteRecursive(dir);

        cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).delete(comic);
        onDeleteComicListener.onDeleteOk();
    }

    @Override
    public void deleteComic(@NonNull Context context, @NonNull String comicPath) {
        File dir = new File(comicPath);
        deleteRecursive(dir);

        cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).delete(Comic.class, "filePath = ?", comicPath);
    }

    @Override
    public void renameComic(@NonNull Context context, Comic comic) {
        cupboard().withDatabase(ComicDBHelper.getComicDBHelper(context).getWritableDatabase()).put(comic);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void exportAsCBZ(@NonNull ArrayList<String> files, @NonNull File cbzFile, CreateCBZUtils.OnCreatingCBZListener onCreatingCBZListener,
                            CreateCBZUtils.OnCreatedCBZListener onCreatedCBZListener) {
        File comicFolder = new File(Environment.getExternalStorageDirectory() + "/Comics/");

        if (!comicFolder.exists())
            comicFolder.mkdirs();

        CreateCBZUtils.createCBZ(files, cbzFile, onCreatingCBZListener, onCreatedCBZListener);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteOriginalFile(@NonNull String pathFile) {
        File file = new File(pathFile);

        if (file.exists())
            file.delete();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }

        fileOrDirectory.delete();
    }

    private ArrayList<String> cleanPages(ArrayList<String> pages) {
        ArrayList<String> cleanedPages = new ArrayList<>();

        for (String eachPage : pages) {
            if (SimpleComicReaderUtils.isValidFormat(eachPage)) {
                cleanedPages.add(eachPage);
            }
        }

        return cleanedPages;
    }
}
