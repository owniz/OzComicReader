package es.jmoral.simplecomicreader.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.prefs.Prefs;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.settings.SettingsActivity;
import es.jmoral.simplecomicreader.utils.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_collection);
        setNavBarColor();
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

    private void showShareDialog() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject test");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "extra text that you want to put");
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    private void openMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        //emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse(getString(R.string.mailto)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.scr_feedback));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_feedback)));
    }

    private void setNavBarColor() {
        ColorStateList myList;
        if (Prefs.with(this).readBoolean(Constants.KEY_PREFERENCES_THEME)) {
            myList= new ColorStateList(
                    new int[][] {
                            new int[] { android.R.attr.state_enabled}
                    },
                    new int[] {
                            ContextCompat.getColor(this, R.color.white)
                    }
            );
        } else {
            myList= new ColorStateList(
                    new int[][] {
                            new int[] { android.R.attr.state_enabled}
                    },
                    new int[] {
                            ContextCompat.getColor(this, R.color.black)
                    }
            );
        }

        navigationView.setItemTextColor(myList);
        navigationView.setItemIconTintList(myList);
    }
}
