package es.jmoral.ozreader.activities.viewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;
import es.dmoral.toasty.Toasty;
import es.jmoral.ozreader.R;
import es.jmoral.ozreader.activities.BaseActivity;
import es.jmoral.ozreader.adapters.ViewerAdapter;
import es.jmoral.ozreader.custom.ColorCircleDrawable;
import es.jmoral.ozreader.custom.FixedViewPager;
import es.jmoral.ozreader.custom.ZoomOutPageTransformer;
import es.jmoral.ozreader.models.Comic;
import es.jmoral.ozreader.utils.Constants;

public class ViewerActivity extends BaseActivity implements ViewerView {
    @BindView(R.id.viewPager) FixedViewPager viewPager;
    @BindView(R.id.seekbar) SeekBar seekBar;
    @BindView(R.id.textViewPage) TextView textViewPage;
    private ViewerPresenter viewerPresenter;
    private Handler visibilityHandler;
    private Runnable visibilityRunnable;
    private boolean hide = false;
    private Comic comic;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        setImmersiveMode();
        viewerPresenter = new ViewerPresenterImpl(this);
        Intent intent = getIntent();
        comic = intent.getParcelableExtra(Constants.KEY_COMIC);
        super.onCreate(savedInstanceState, R.layout.activity_viewer);
    }

    @Override
    protected void setUpViews() {
        viewerPresenter.readComic(comic.getFilePath(), comic.getNumPages());

        if (Prefs.with(this).readBoolean(Constants.KEY_PREFERENCES_ANIMATION, true))
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        seekBar.setMax(comic.getNumPages() - 1);
        seekBar.setProgress(comic.getCurrentPage() - 1);
        seekBar.setVisibility(View.INVISIBLE);
        textViewPage.setVisibility(View.INVISIBLE);
        textViewPage.setBackground(
                new ColorCircleDrawable(ResourcesCompat.getColor(getResources(), R.color.dark_background, null))
        );
        textViewPage.setText(String.valueOf(comic.getCurrentPage()));

        visibilityHandler = new Handler();
        visibilityRunnable = new Runnable() {
            @Override
            public void run() {
                if (seekBar != null) {
                    seekBar.setVisibility(View.INVISIBLE);
                    textViewPage.setVisibility(View.INVISIBLE);
                    hide = false;
                }
            }
        };
    }

    @Override
    protected void setListeners() {
         viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
             @Override
             public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                 // unused
             }

             @Override
             public void onPageSelected(int position) {
                 comic.setCurrentPage(position + 1);
                 seekBar.setProgress(comic.getCurrentPage() - 1);
                 visibilityHandler.postDelayed(visibilityRunnable, 3500);

                 if (position == 0)
                     Toasty.normal(
                             ViewerActivity.this,
                             getString(R.string.first_page_reached),
                             ContextCompat.getDrawable(ViewerActivity.this, R.drawable.ic_import_contacts_black_24dp)
                     ).show();
                 else if (position == (comic.getNumPages() - 1))
                     Toasty.normal(
                             ViewerActivity.this,
                             getString(R.string.last_page_reached),
                             ContextCompat.getDrawable(ViewerActivity.this, R.drawable.ic_import_contacts_black_24dp)
                     ).show();
             }

             @Override
             public void onPageScrollStateChanged(int state) {
                 // unused
             }
         });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewPage.setText(String.valueOf(i + 1));
                visibilityHandler.removeCallbacks(visibilityRunnable);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // unused
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                viewPager.setCurrentItem(seekBar.getProgress());
                visibilityHandler.postDelayed(visibilityRunnable, 3500);
            }
        });
    }

    @Override
    public void showComic(ArrayList<String> pathImages) {
        viewPager.setAdapter(new ViewerAdapter(pathImages, new ViewerAdapter.OnSliderShownListener() {
            @Override
            public void onSeekBarShown() {
                if (!hide) {
                    textViewPage.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    visibilityHandler.postDelayed(visibilityRunnable, 3500);
                    hide = true;
                } else {
                    visibilityHandler.removeCallbacks(visibilityRunnable);
                    textViewPage.setVisibility(View.INVISIBLE);
                    seekBar.setVisibility(View.INVISIBLE);
                    hide = false;
                }
            }
        }));
        viewPager.setCurrentItem(comic.getCurrentPage() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewerPresenter.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewerPresenter.setCurrentPage(comic);
    }

    @Override
    public void onResume(){
        super.onResume();
        setImmersiveMode();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_COMIC, comic);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void setImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().getDecorView().setSystemUiVisibility(
                    getWindow().getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
    }
}
