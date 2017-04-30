package es.jmoral.simplecomicreader.activities.viewer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.activities.BaseActivity;
import es.jmoral.simplecomicreader.adapters.ViewerAdapter;
import es.jmoral.simplecomicreader.models.Comic;
import es.jmoral.simplecomicreader.utils.Constants;

public class ViewerActivity extends BaseActivity implements ViewerView {
    @BindView(R.id.viewPager) ViewPager viewPager;

    private ViewerPresenter viewerPresenter;

    private Comic comic;

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

                 if (position == 0)
                     Toasty.info(ViewerActivity.this, getString(R.string.first_page_reached)).show();
                 else if (position == (comic.getNumPages() - 1))
                     Toasty.info(ViewerActivity.this, getString(R.string.last_page_reached)).show();
             }

             @Override
             public void onPageScrollStateChanged(int state) {
                 // unused
             }
         });
    }

    @Override
    public void showComic(ArrayList<String> pathImages) {
        viewPager.setAdapter(new ViewerAdapter(pathImages));
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
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_COMIC, comic);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void setImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().getDecorView().setSystemUiVisibility(
                    getWindow().getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
    }
}
