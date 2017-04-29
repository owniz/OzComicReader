package es.jmoral.simplecomicreader.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.jmoral.simplecomicreader.R;

/**
 * Created by owniz on 16/04/17.
 */

public class ViewerAdapter extends PagerAdapter {
    private ArrayList<String> pathImages;

    public ViewerAdapter(ArrayList<String> pathImages) {
        this.pathImages = pathImages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.image_page_layout, container, false);
        ViewerViewHolder viewerViewHolder = new ViewerViewHolder(view);
        Glide.with(viewerViewHolder.itemView.getContext()).load(pathImages.get(position)).into(viewerViewHolder.photoView);

        container.addView(viewerViewHolder.itemView);

        return viewerViewHolder;
    }

    @Override
    public int getCount() {
        return pathImages != null ? pathImages.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof ViewerViewHolder
                && view.equals(((ViewerViewHolder) object).itemView);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((ViewerViewHolder) object).itemView);
    }

    static class ViewerViewHolder {
        private final View itemView;
        @BindView(R.id.imagePage) PhotoView photoView;

        ViewerViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
