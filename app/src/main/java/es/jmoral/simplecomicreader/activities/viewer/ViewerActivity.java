package es.jmoral.simplecomicreader.activities.viewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import es.jmoral.mortadelo.models.Comic;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.activities.BaseActivity;
import es.jmoral.simplecomicreader.adapters.ViewerAdapter;
import es.jmoral.simplecomicreader.utils.Constants;

public class ViewerActivity extends BaseActivity implements ViewerView {
    @BindView(R.id.viewPager) ViewPager viewPager;

    private ViewerPresenter viewerPresenter;

    private String comicPath;
    private int currentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewerPresenter = new ViewerPresenterImpl(this);
        Intent intent = getIntent();
        comicPath = intent.getExtras().getString(Constants.KEY_COMIC_PATH);
        currentPage = intent.getExtras().getInt(Constants.KEY_CURRENT_PAGE);
        super.onCreate(savedInstanceState, R.layout.activity_viewer);
    }

    @Override
    protected void setUpViews() {
        viewerPresenter.readComic(comicPath);
    }

    @Override
    protected void setListeners() {
        // unused
    }

    @Override
    public void showComic(ArrayList<String> pathImages) {
        viewPager.setAdapter(new ViewerAdapter(pathImages));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewerPresenter.onDestroy();
    }
}
