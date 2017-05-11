package es.jmoral.simplecomicreader.activities.splashscreen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.activities.main.MainActivity;
import es.jmoral.simplecomicreader.utils.Constants;

public class SplashScreenActivity extends AppCompatActivity {
    private Button button;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        button = (Button) findViewById(R.id.buttonPermission);

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
