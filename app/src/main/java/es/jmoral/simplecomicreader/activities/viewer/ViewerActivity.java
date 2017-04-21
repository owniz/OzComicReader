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
import es.jmoral.simplecomicreader.utils.Constants;

public class ViewerActivity extends BaseActivity implements ViewerView {
    @BindView(R.id.viewPager) ViewPager viewPager;

    private ViewerPresenter viewerPresenter;

    private String comicPath;
    private int currentPage;
    private int numPages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        setImmersiveMode();
        viewerPresenter = new ViewerPresenterImpl(this);
        Intent intent = getIntent();
        comicPath = intent.getExtras().getString(Constants.KEY_COMIC_PATH);
        currentPage = intent.getExtras().getInt(Constants.KEY_CURRENT_PAGE);
        numPages = intent.getExtras().getInt(Constants.KEY_TOTAL_PAGES);
        super.onCreate(savedInstanceState, R.layout.activity_viewer);
    }

    @Override
    protected void setUpViews() {
        viewerPresenter.readComic(comicPath, numPages);
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

    private void setImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        // si es superior a 18 recuperamos la configuración anterior y además añadimos otra opción
        // para esas versiones
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().getDecorView().setSystemUiVisibility(
                    getWindow().getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
    }
}
