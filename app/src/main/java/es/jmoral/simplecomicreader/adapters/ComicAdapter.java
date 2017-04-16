package es.jmoral.simplecomicreader.adapters;

import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 14/04/17.
 */

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {
    public interface OnComicClickListener {
        void onComicClicked(Comic comic);
    }

    private ArrayList<Comic> comics;
    private OnComicClickListener onComicClickListener;

    public ComicAdapter(ArrayList<Comic> comics, OnComicClickListener onComicClickListener) {
        this.comics = comics;
        this.onComicClickListener = onComicClickListener;
    }

    @Override
    public ComicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ComicViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_cardview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ComicViewHolder holder, int position) {
        Comic comic = comics.get(position);

        File imageFile = new File(comic.getCoverPath());

        holder.imageViewCover.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        holder.textViewTitle.setText(comic.getTitle());
        holder.textViewPages.setText(holder.textViewPages.getContext().getString(R.string.page_of, comic.getCurrentPage(), comic.getNumPages()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(comic.getAddedTimeStamp());
        holder.textViewTimeStamp.setText(date);
    }

    public void insertComic(Comic comic) {
        comics.add(comic);
        notifyItemInserted(comics.size() - 1);
    }

    public void removeComic(int position) {
        comics.remove(position);
        notifyItemRemoved(position);
    }

    public Comic getComic(int position) {
        return comics.get(position);
    }

    @Override
    public int getItemCount() {
        return comics != null ? comics.size() : 0;
    }

    class ComicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardViewComic) CardView cardView;
        @BindView(R.id.imageViewCover) ImageView imageViewCover;
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

        }
    }
}
