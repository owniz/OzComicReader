package es.jmoral.simplecomicreader.activities.main;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;

import java.io.File;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.activities.BaseActivity;
import es.jmoral.simplecomicreader.activities.settings.SettingsActivity;
import es.jmoral.simplecomicreader.fragments.collection.CollectionFragment;
import es.jmoral.simplecomicreader.utils.Constants;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView,
        FileChooserDialog.FileCallback {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
    }

    @Override
    protected void setUpViews() {
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // disables the hamburguer to arrow
            }
        };
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_collection);
        setNavBarColor();

        setFragment(CollectionFragment.newInstance(), CollectionFragment.class.toString());
    }

    @Override
    protected void setListeners() {
        drawer.addDrawerListener(toggle);

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                methodRequiresTwoPermission();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_sort_menu, menu);
        return true;
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
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.nav_share:
                showShareDialog();
                break;
            case R.id.nav_contact:
                openMail();
                break;
            case R.id.nav_donate:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_menu:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setFragment(@NonNull Fragment fragment, String tag) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_cards, fragment, tag);
        fragmentTransaction.commitNow();
    }

    @Override
    public void showShareDialog() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.download_subject));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.download_url));
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

    private void setNavBarColor() {
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
    public void showFileChooserDialog() {
        new FileChooserDialog.Builder(this)
                .initialPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download")
                .mimeType("image/*") // Optional MIME type filter
                .extensionsFilter(".cbr", ".cbz")
                .goUpLabel("Up") // custom go up label, default label is "..."
                .show();
    }

    @Override
    public void onFileSelection(@NonNull FileChooserDialog dialog, @NonNull File file) {
        if (getSupportFragmentManager().findFragmentByTag(CollectionFragment.class.toString()) != null)
            ((CollectionFragment) getSupportFragmentManager().findFragmentByTag(CollectionFragment.class.toString())).addComic(file);
    }

    @Override
    public void onFileChooserDismissed(@NonNull FileChooserDialog dialog) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @AfterPermissionGranted(Constants.READ_WRITE_PERMISSIONS)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) { // Already have permission
            showFileChooserDialog();
        } else { // Do not have permissions
            EasyPermissions.requestPermissions(this, getString(R.string.read_permissions),
                    Constants.READ_WRITE_PERMISSIONS, perms);
        }
    }
}
