package es.jmoral.simplecomicreader;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import es.dmoral.prefs.Prefs;
import es.jmoral.simplecomicreader.utils.Constants;

/**
 * Created by owniz on 10/04/17.
 */

public class SimpleComicReaderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(Prefs.with(getApplicationContext())
                .readBoolean(Constants.KEY_PREFERENCES_THEME) ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);
    }
}
