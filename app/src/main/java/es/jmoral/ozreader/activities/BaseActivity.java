package es.jmoral.ozreader.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by owniz on 13/04/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState, @LayoutRes int contentViewId) {
        super.onCreate(savedInstanceState);
        setContentView(contentViewId);
        ButterKnife.bind(this);
        setUpViews();
        setListeners();
    }

    protected abstract void setUpViews();
    protected abstract void setListeners();
}
