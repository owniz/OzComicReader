package es.jmoral.simplecomicreader.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

import es.jmoral.mortadelo.models.Comic;

/**
 * Created by owniz on 16/04/17.
 */

public class ViewerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Comic> comics;

    public ViewerAdapter(Context context, ArrayList<Comic> comics) {
        this.context = context;
        this.comics = comics;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
