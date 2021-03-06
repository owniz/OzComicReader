package es.jmoral.ozreader.adapters;

import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.prefs.Prefs;
import es.jmoral.ozreader.R;
import es.jmoral.ozreader.utils.Constants;

/**
 * Created by owniz on 16/04/17.
 */

public class ViewerAdapter extends PagerAdapter {
    private ArrayList<String> pathImages;
    private OnSliderShownListener onSliderShownListener;

    public interface OnSliderShownListener {
        void onSeekBarShown();
    }

    public ViewerAdapter(ArrayList<String> pathImages, OnSliderShownListener onSliderShownListener) {
        this.pathImages = pathImages;
        this.onSliderShownListener = onSliderShownListener;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.image_page_layout, container, false);
        ViewerViewHolder viewerViewHolder = new ViewerViewHolder(view);
        DrawableRequestBuilder glideRequestBuilder = Glide.with(viewerViewHolder.itemView.getContext())
                .fromString()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(pathImages.get(position));

        if (Prefs.with(container.getContext()).readBoolean(Constants.KEY_PREFERENCES_QUALITY, true))
            glideRequestBuilder.dontTransform();

        glideRequestBuilder.into(viewerViewHolder.photoView);

        container.addView(viewerViewHolder.itemView);

        viewerViewHolder.itemView.setClickable(true);
        viewerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSliderShownListener.onSeekBarShown();
            }
        });

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
