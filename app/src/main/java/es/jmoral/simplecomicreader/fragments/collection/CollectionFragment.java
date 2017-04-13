package es.jmoral.simplecomicreader.fragments.collection;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionFragment extends BaseFragment {

    public static Fragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_collection, container, savedInstanceState);
    }

    @Override
    protected void setUpViews() {
    }

    @Override
    protected void setListeners() {

    }
}
