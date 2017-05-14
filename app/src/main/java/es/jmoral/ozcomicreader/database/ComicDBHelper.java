package es.jmoral.ozcomicreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import es.jmoral.ozcomicreader.models.Comic;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by owniz on 14/04/17.
 */

public class ComicDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "comic.db";
    private static final int DATABASE_VERSION = 1;
    private static ComicDBHelper comicDBHelper;

    private ComicDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        cupboard().register(Comic.class);
    }

    public static ComicDBHelper getComicDBHelper(Context context) {
        if (comicDBHelper == null)
            comicDBHelper = new ComicDBHelper(context.getApplicationContext());

        return comicDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }
}
