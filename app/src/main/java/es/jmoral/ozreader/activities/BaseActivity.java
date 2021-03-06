package es.jmoral.ozreader.activities;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
