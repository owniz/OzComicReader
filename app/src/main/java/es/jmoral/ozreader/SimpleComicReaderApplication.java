package es.jmoral.ozreader;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

import es.dmoral.prefs.Prefs;
import es.jmoral.ozreader.utils.Constants;

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
