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

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmsAdapterViewHolder>{
    private List<Film> filmDataset;
    private final OnFilmClickListener filmListener;
    //Sirve para pasarle al adapterel layout sobre el que se debe actuar
    private final int itemlist;

    public interface OnFilmClickListener {
        void onFilmClick(Film item);
    }

    public static class FilmsAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView pTittleView;
        final TextView pDateView;
        final ImageView pPostedView;

        public FilmsAdapterViewHolder(View view) {
            super(view);
            pTittleView = view.findViewById(R.id.filmTitle);
            pDateView = view.findViewById(R.id.filmReleaseDate);
            pPostedView = view.findViewById(R.id.poster);
        }
    }

    public FilmAdapter(List<Film> myDataset, int itemlist, Context context) {
        this.filmDataset = myDataset;
        this.itemlist = itemlist;
        filmListener = (OnFilmClickListener) context;
    }

    public void swap(List<Film> dataset) {
        filmDataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filmDataset.size();
    }

    @NonNull
    @Override
    public FilmsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.itemlist, parent, false);
        return new FilmsAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmsAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pTittleView.setText(filmDataset.get(position).getTitle());
        holder.pDateView.setText(filmDataset.get(position).getReleaseDate().split("-")[0]);
        Glide.with(holder.pPostedView.getContext()).load("https://image.tmdb.org/t/p/original/"+filmDataset.get(position).getPosterPath()).into(holder.pPostedView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filmListener.onFilmClick(filmDataset.get(position));
            }
        });
    }
}
