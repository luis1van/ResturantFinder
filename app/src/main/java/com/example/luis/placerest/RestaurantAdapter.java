package com.example.luis.placerest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<Restaurant> restaurantList;

    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder restaurantViewHolder, int i) {
        Restaurant ri = restaurantList.get(i);
        restaurantViewHolder.vName.setText(ri.getName());
        restaurantViewHolder.vAddress.setText(ri.getAddress());
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_item, viewGroup, false);

        return new RestaurantViewHolder(itemView);
    }

}