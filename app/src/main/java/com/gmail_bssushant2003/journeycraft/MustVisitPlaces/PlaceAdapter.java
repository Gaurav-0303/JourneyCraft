package com.gmail_bssushant2003.journeycraft.MustVisitPlaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gmail_bssushant2003.journeycraft.R;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private final Context context;
    private List<Place> places;

    public PlaceAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_items, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place place = places.get(position);
        holder.nameTextView.setText(place.getName());

        // Use Glide to load the image URL into the ImageView
        Glide.with(context)
                .load(place.getImageUrl())
                .into(holder.imageView);

        // Set best time to visit
        holder.bestTimeToVisitTextView.setText(place.getBestTimeToVisit());

        // Initially hide the best time to visit
        holder.bestTimeToVisitTextView.setVisibility(View.GONE);

        // Handle expand button click to toggle visibility
        holder.expandButton.setOnClickListener(v -> {
            if (holder.bestTimeToVisitTextView.getVisibility() == View.GONE) {
                holder.bestTimeToVisitTextView.setVisibility(View.VISIBLE);
                holder.expandButton.setImageResource(R.drawable.up);
                //holder.expandButton.setText("Collapse");
            } else {
                holder.bestTimeToVisitTextView.setVisibility(View.GONE);
                holder.expandButton.setImageResource(R.drawable.down);
                //holder.expandButton.setText("Expand");
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setData(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        TextView bestTimeToVisitTextView;
        ImageView imageView;
        ImageButton expandButton;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView);
            bestTimeToVisitTextView = itemView.findViewById(R.id.bestTimeToVisitTextView);
            imageView = itemView.findViewById(R.id.imageView);
            expandButton = itemView.findViewById(R.id.expandButton);
        }
    }
}
