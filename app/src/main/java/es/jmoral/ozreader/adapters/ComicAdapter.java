package es.jmoral.ozreader.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.prefs.Prefs;
import es.jmoral.ozreader.R;
import es.jmoral.ozreader.custom.PaletteBitmap;
import es.jmoral.ozreader.fragments.collection.CollectionView;
import es.jmoral.ozreader.models.Comic;
import es.jmoral.ozreader.utils.Constants;

/**
 * Created by owniz on 14/04/17.
 */

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {
    private ArrayList<Comic> comics;
    private OnComicClickListener onComicClickListener;
    private int comicPosition;

    public interface OnComicClickListener {
        void onComicClicked(Comic comic);
    }

    public ComicAdapter(ArrayList<Comic> comics, OnComicClickListener onComicClickListener, CollectionView.SortOrder sortOrder) {
        this.comics = comics;
        orderComic(sortOrder, false);
        this.onComicClickListener = onComicClickListener;
    }

    @Override
    public ComicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ComicViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_cardview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ComicViewHolder holder, int position) {
        Comic comic = comics.get(position);

        holder.cardView.setVisibility(View.GONE);
        Glide.with(holder.cardView.getContext())
                .fromString()
                .asBitmap()
                .transcode(new PaletteBitmap.PaletteBitmapTranscoder(holder.cardView.getContext()), PaletteBitmap.class)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(comic.getCoverPath())
                .into(new ImageViewTarget<PaletteBitmap>(holder.imageViewCover) {
                    @Override
                    protected void setResource(PaletteBitmap resource) {
                        super.view.setImageBitmap(resource.bitmap);
                        holder.cardView.setCardBackgroundColor(
                                Prefs.with(holder.cardView.getContext()).readBoolean(Constants.KEY_PREFERENCES_THEME)
                                        ? resource.palette.getMutedColor(Color.WHITE)
                                        : resource.palette.getLightMutedColor(Color.WHITE));
                        Palette.Swatch swatch =
                                Prefs.with(holder.cardView.getContext()).readBoolean(Constants.KEY_PREFERENCES_THEME)
                                        ? resource.palette.getMutedSwatch()
                                        : resource.palette.getLightMutedSwatch();
                        holder.imageViewDate.setColorFilter(swatch == null ? Color.DKGRAY : swatch.getTitleTextColor(), PorterDuff.Mode.SRC_IN);
                        holder.imageViewPages.setColorFilter(swatch == null ? Color.DKGRAY : swatch.getTitleTextColor(), PorterDuff.Mode.SRC_IN);
                        holder.cardView.setVisibility(View.VISIBLE);
                        holder.textViewTitle.setTextColor(swatch == null ? Color.DKGRAY : swatch.getTitleTextColor());
                        holder.textViewPages.setTextColor(swatch == null ? Color.DKGRAY : swatch.getTitleTextColor());
                        holder.textViewTimeStamp.setTextColor(swatch == null ? Color.DKGRAY : swatch.getTitleTextColor());
                    }
                });

        holder.textViewTitle.setText(comic.getTitle());
        holder.textViewPages.setText(holder.textViewPages.getContext().getString(R.string.page_of, comic.getCurrentPage(), comic.getNumPages()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(comic.getAddedTimeStamp());
        holder.textViewTimeStamp.setText(date);
    }

    public void insertComic(Comic comic, CollectionView.SortOrder sortOrder) {
        comics.add(comic);
        orderComic(sortOrder, true);
    }

    public void insertComicAtPosition(Comic comic, int position) {
        comics.add(position, comic);
        notifyItemInserted(position);
    }

    public void removeComic(int position) {
        comics.remove(position);
        notifyItemRemoved(position);
    }

    public void updateTitleBehaviour(int position) {
        notifyItemChanged(position);
    }

    public void removeComic(Comic comic, boolean notify) {
        int oldPos = getComicPosByFilePath(comic);

        if (oldPos != -1) {
            comics.remove(oldPos);

            if (notify)
                notifyItemRemoved(oldPos);
        }
    }

    private int getComicPosByFilePath(Comic comic) {
        for (Comic aComic : comics) {
            if (aComic.getFilePath().equals(comic.getFilePath()))
                return comics.indexOf(aComic);
        }

        return -1;
    }

    public Comic getComic(int position) {
        return comics.get(position);
    }

    public void orderComic(final CollectionView.SortOrder sortOrder, boolean reorder) {
        Collections.sort(comics, new Comparator<Comic>() {
            @Override
            public int compare(Comic c1, Comic c2) {
                switch (sortOrder) {
                    case SORT_TITLE:
                        return (c1.getTitle()).compareToIgnoreCase(c2.getTitle());
                    case SORT_NEWEST:
                        return (int) c2.getAddedTimeStamp() - (int) c1.getAddedTimeStamp();
                    case SORT_OLDEST:
                        return (int) c1.getAddedTimeStamp() - (int) c2.getAddedTimeStamp();
                    default:
                        return -1;
                }
            }
        });

        if (reorder)
            notifyItemRangeChanged(0, comics.size());
    }

    public void updateComic(Comic comic) {
        int position = comics.indexOf(comic);

        comics.set(position, comic);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return comics != null ? comics.size() : 0;
    }

    public int getComicPosition() {
        return comicPosition;
    }

    public void setComicPosition(int comicPosition) {
        this.comicPosition = comicPosition;
    }

    class ComicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardViewComic) CardView cardView;
        @BindView(R.id.imageViewCover) ImageView imageViewCover;
        @BindView(R.id.imageViewDate) ImageView imageViewDate;
        @BindView(R.id.imageViewPages) ImageView imageViewPages;
        @BindView(R.id.textViewTitle) TextView textViewTitle;
        @BindView(R.id.textViewPage) TextView textViewPages;
        @BindView(R.id.textViewTimeStamp) TextView textViewTimeStamp;

        ComicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onComicClickListener.onComicClicked(comics.get(getAdapterPosition()));
                }
            });

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setComicPosition(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}