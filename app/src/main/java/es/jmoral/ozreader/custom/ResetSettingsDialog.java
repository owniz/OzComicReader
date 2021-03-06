package es.jmoral.ozreader.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.preference.DialogPreference;
import androidx.annotation.NonNull;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.processphoenix.ProcessPhoenix;

import es.dmoral.prefs.Prefs;
import es.jmoral.ozreader.R;
import es.jmoral.ozreader.activities.main.MainActivity;
import es.jmoral.ozreader.utils.Constants;

@SuppressWarnings("PublicMethodWithoutLogging")
public class ResetSettingsDialog extends DialogPreference {
    public ResetSettingsDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        new MaterialDialog.Builder(getContext())
                .content(R.string.reset_set_question)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .positiveText(R.string.reset)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Prefs.with(getContext()).writeBoolean(Constants.KEY_PREFERENCES_THEME, false);
                        Prefs.with(getContext()).write(Constants.KEY_PREFERENCES_SORT, Constants.SORT_BY_TITLE);
                        Prefs.with(getContext()).writeBoolean(Constants.KEY_PREFERENCES_ANIMATION, true);
                        Prefs.with(getContext()).writeBoolean(Constants.KEY_PREFERENCES_QUALITY, true);
                        Prefs.with(getContext()).write(Constants.KEY_PREFERENCES_DELETE_DIALOG_ACTION, Constants.DELETE_DIALOG_ACTION_NONE);
                        Prefs.with(getContext()).writeBoolean(Constants.KEY_PREFERENCES_SHOW_DIALOG, true);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProcessPhoenix.triggerRebirth(getContext(),
                                        new Intent(getContext(), MainActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        }, 250);
                    }
                })
                .negativeText(android.R.string.cancel)
                .show();


    }
}