package es.jmoral.simplecomicreader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.jmoral.simplecomicreader.R;
import es.jmoral.simplecomicreader.models.Comic;

/**
 * Created by owniz on 14/04/17.
 */

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {
    private ArrayList<Comic> comics;

    public ComicAdapter(ArrayList<Comic> comics) {
        this.comics = comics;
    }

    @Override
    public ComicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ComicViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_cardview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ComicViewHolder holder, int position) {
        Comic comic = comics.get(position);

        holder.textView.setText(comic.getTitle());
    }

    @Override
    public int getItemCount() {
        return comics != null ? comics.size() : 0;
    }

    static class ComicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text) TextView textView;

        ComicViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
