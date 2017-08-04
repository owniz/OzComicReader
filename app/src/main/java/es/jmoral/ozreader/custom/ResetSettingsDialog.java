package es.jmoral.ozreader.custom;

import android.content.Context;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import es.jmoral.ozreader.R;

@SuppressWarnings("PublicMethodWithoutLogging")
public class ResetSettingsDialog extends DialogPreference {
    public ResetSettingsDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        new MaterialDialog.Builder(getContext())
                .content("Are you sure you want to revert all settings to their default values?")
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .positiveText(R.string.reset)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .negativeText(android.R.string.cancel)
                .show();


    }
}