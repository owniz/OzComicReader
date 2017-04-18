package es.jmoral.simplecomicreader.adapters;

import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.jmoral.simplecomicreader.R;

/**
 * Created by owniz on 16/04/17.
 */

public class ViewerAdapter extends PagerAdapter {
    private ArrayList<String> pathImnages;

    public ViewerAdapter(ArrayList<String> pathImnages) {
        this.pathImnages = pathImnages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.image_page_layout, container, false);
        ViewerVierHolder viewerVierHolder = new ViewerVierHolder(view);
        viewerVierHolder.photoView.setImageBitmap(BitmapFactory.decodeFile(pathImnages.get(position)));
        container.addView(viewerVierHolder.itemView);

        return viewerVierHolder;
    }

    @Override
    public int getCount() {
        return pathImnages != null ? pathImnages.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof ViewerVierHolder
                && view.equals(((ViewerVierHolder) object).itemView);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((ViewerVierHolder) object).itemView);
    }

    static class ViewerVierHolder {
        private final View itemView;
        @BindView(R.id.imagePage) PhotoView photoView;

        ViewerVierHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
