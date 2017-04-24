package es.jmoral.simplecomicreader.activities.viewer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
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
        // unused
    }

    @Override
    public void showComic(ArrayList<String> pathImages) {
        viewPager.setAdapter(new ViewerAdapter(pathImages));
        viewPager.setCurrentItem(comic.getCurrentPage() - 1);
    }

    @Override
    public void updateComic(Comic comic) {
        this.comic = comic;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewerPresenter.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewerPresenter.setCurrentPage(comic,viewPager.getCurrentItem() + 1);
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
