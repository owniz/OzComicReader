package es.jmoral.ozreader.activities.splashscreen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import butterknife.BindView;
import es.jmoral.ozreader.R;
import es.jmoral.ozreader.activities.BaseActivity;
import es.jmoral.ozreader.activities.main.MainActivity;
import es.jmoral.ozreader.utils.Constants;

public class SplashScreenActivity extends BaseActivity {
    @BindView(R.id.buttonPermission) Button button;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_splash_screen);

        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (getIntent().getDataString() != null && getIntent().getAction().equals(Intent.ACTION_VIEW)) {
            intent.putExtra(Constants.PATH_FROM_FILE, getIntent().getDataString());
            getIntent().setAction("");
            askPermission();
        } else {
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void setUpViews() {
        // unused
    }

    @Override
    protected void setListeners() {
        // unused
    }

    private void askPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        new MaterialDialog.Builder(SplashScreenActivity.this)
                                .title(R.string.why_need)
                                .content(R.string.read_permissions)
                                .cancelable(false).canceledOnTouchOutside(false)
                                .positiveText(R.string.ok)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        button.setVisibility(View.VISIBLE);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                askPermission();
                                            }
                                        });
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
}
