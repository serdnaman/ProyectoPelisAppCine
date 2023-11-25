package com.example.proyectopelisappcine.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.model.Film;

import java.util.List;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.FilmsListAdapterViewHolder> {
    private List<Film> filmListDataset;
    private final FilmAdapter.OnFilmClickListener filmListListener;
    private final int itemlist;

    public interface ButtonListener {
        void onButtonPressed(Film film, FilmsAdapter filmsAdapter);
    }

    public static class FilmsListAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView pTittleView;
        final TextView pDateView;
        final ImageView pPostedView;

        public FilmsListAdapterViewHolder(View view) {
            super(view);
            pTittleView = view.findViewById(R.id.filmTitle);
            pDateView = view.findViewById(R.id.filmReleaseDate);
            pPostedView = view.findViewById(R.id.poster);
        }
    }

    public FilmsAdapter(List<Film> myDataset, int itemlist, Context context) {
        this.filmListDataset = myDataset;
        this.itemlist = itemlist;
        this.filmListListener = (FilmAdapter.OnFilmClickListener) context;
    }

    public void swap(List<Film> dataset) {
        filmListDataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filmListDataset.size();
    }

    @NonNull
    @Override
    public FilmsListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.itemlist, parent, false);
        return new FilmsListAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmsListAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pTittleView.setText(filmListDataset.get(position).getTitle());
        holder.pDateView.setText(filmListDataset.get(position).getReleaseDate().split("-")[0]);
        Glide.with(holder.pPostedView.getContext()).load("https://image.tmdb.org/t/p/original/"+filmListDataset.get(position).getPosterPath()).into(holder.pPostedView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filmListListener.onFilmClick(filmListDataset.get(position));
            }
        });
    }
}
