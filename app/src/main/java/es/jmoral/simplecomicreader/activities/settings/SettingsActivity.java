package es.jmoral.simplecomicreader.activities.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.jakewharton.processphoenix.ProcessPhoenix;

import es.dmoral.prefs.Prefs;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.utils.Constants;

/**
 * A that presents a set of application preferences. On
 * handset devices, preferences are presented as a single list. On tablets,
 * preferences are split by category, with category headers shown to the left of
 * the list of preferences.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(final Preference preference, Object value) {
            final String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(listPreference.getEntries()[index]);
            } else if (preference instanceof SwitchPreference) {
                if (((SwitchPreference) preference).isChecked() != Boolean.valueOf(stringValue)) {
                    if (preference.getKey().equals(Constants.KEY_PREFERENCES_THEME)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProcessPhoenix.triggerRebirth(preference.getContext());
                            }
                        }, 250);
                    }

                    preference.setSummary(stringValue);
                    return true;
                } else {
                    return false;
                }
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setTheme(Prefs.with(this).readBoolean(Constants.KEY_PREFERENCES_THEME)
                    ? android.R.style.ThemeOverlay_Material_Dark
                    : android.R.style.ThemeOverlay_Material);
        else
            setTheme(Prefs.with(this).readBoolean(Constants.KEY_PREFERENCES_THEME)
                    ? R.style.AppTheme_Dark
                    : R.style.AppTheme_Light);

        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new GeneralPreferenceFragment(), GeneralPreferenceFragment.class.getName())
                    .commit();
        }

        setupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #onPreferenceChangeListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        final String key = preference.getKey();
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(onPreferenceChangeListener);

        // Trigger the listener immediately with the preference's
        // current value.
        switch (key) {
            case Constants.KEY_PREFERENCES_THEME:
                onPreferenceChangeListener.onPreferenceChange(preference,
                        Prefs.with(preference.getContext()).readBoolean(key));
                break;
            case Constants.KEY_PREFERENCES_SORT:
                onPreferenceChangeListener.onPreferenceChange(preference,
                        Prefs.with(preference.getContext()).read(key));
                break;
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane preferences UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            bindPreferenceSummaryToValue(findPreference(Constants.KEY_PREFERENCES_THEME));
            bindPreferenceSummaryToValue(findPreference(Constants.KEY_PREFERENCES_SORT));
        }
    }
}
