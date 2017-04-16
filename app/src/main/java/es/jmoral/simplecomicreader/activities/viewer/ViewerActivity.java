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
    private final ArrayList<Comic> comics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        viewerPresenter = new ViewerPresenterImpl();

        Intent intent = getIntent();
        comicPath = intent.getExtras().getString(Constants.KEY_COMIC_PATH);
        currentPage = intent.getExtras().getInt(Constants.KEY_CURRENT_PAGE);
    }

    @Override
    protected void setUpViews() {
        viewPager.setAdapter(new ViewerAdapter(this, comics));
        showComic();
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void showComic() {
        viewerPresenter.openComic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewerPresenter.onDestroy();
    }
}
