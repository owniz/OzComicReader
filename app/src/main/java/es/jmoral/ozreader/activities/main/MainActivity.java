package es.jmoral.ozreader.activities.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.regex.Pattern;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.jmoral.ozreader.R;
import es.jmoral.ozreader.activities.BaseActivity;
import es.jmoral.ozreader.activities.settings.SettingsActivity;
import es.jmoral.ozreader.fragments.collection.CollectionFragment;
import es.jmoral.ozreader.utils.Constants;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private ActionBarDrawerToggle toggle;
    private String pathFromFile;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pathFromFile = getIntent().getStringExtra(Constants.PATH_FROM_FILE);

        super.onCreate(savedInstanceState, R.layout.activity_main);
    }

    @Override
    protected void setUpViews() {
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // disables the hamburger to arrow
            }
        };
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_collection);
        setNavViewColor();

        if (pathFromFile != null && !pathFromFile.equals(""))
            setFragment(CollectionFragment.newInstance(pathFromFile), CollectionFragment.class.toString());
        else
            setFragment(CollectionFragment.newInstance(), CollectionFragment.class.toString());

        pathFromFile = null;
    }

    @Override
    protected void setListeners() {
        drawer.addDrawerListener(toggle);

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                askPermission();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_collection:
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_share:
                showShareDialog();
                break;
            case R.id.nav_contact:
                openMail();
                break;
            case R.id.nav_donate:
                openDonate();
                break;
            default:
                return false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setFragment(@NonNull Fragment fragment, String tag) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_cards, fragment, tag);
        fragmentTransaction.commitNow();
    }

    @Override
    public void showShareDialog() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.download_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.download_url));
        startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
    }

    @Override
    public void openMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        //emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse(getString(R.string.mailto)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.scr_feedback));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_feedback)));
    }

    // Disables accent color tint on navigation menu
    @Override
    public void setNavViewColor() {
        ColorStateList myList;
        if (Prefs.with(this).readBoolean(Constants.KEY_PREFERENCES_THEME))
            myList= new ColorStateList(
                new int[][] {new int[] { android.R.attr.state_enabled}},
                new int[] {ContextCompat.getColor(this, R.color.white)});
        else
            myList= new ColorStateList(
                new int[][] {new int[] { android.R.attr.state_enabled}},
                new int[] {ContextCompat.getColor(this, R.color.black)});

        navigationView.setItemTextColor(myList);
        navigationView.setItemIconTintList(myList);
    }

    @Override
    public void openDonate() {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(new String(Base64.decode(getString(R.string.pp_url), Base64.DEFAULT)))));
    }

    @Override
    public void showFileChooserDialog() {
        new MaterialFilePicker()
                .withActivity(MainActivity.this)
                .withRequestCode(Constants.REQUEST_CODE_FILE)
                .withFilter(Pattern.compile(".*\\.(cbz|cbr)$"))
                .withRootPath("/")
                .withPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download")
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_FILE && resultCode == RESULT_OK && data != null)
            if (getSupportFragmentManager().findFragmentByTag(CollectionFragment.class.toString()) != null)
                ((CollectionFragment) getSupportFragmentManager().findFragmentByTag(
                        CollectionFragment.class.toString()))
                        .addComic(new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)));
    }

    private void askPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showFileChooserDialog();
                            }
                        }, 250);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.why_need)
                                .content(R.string.read_permissions)
                                .cancelable(false).canceledOnTouchOutside(false)
                                .positiveText(R.string.ok)
                                .show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public FloatingActionButton getFAB() {
        return fab;
    }
}
