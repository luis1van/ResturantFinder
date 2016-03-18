package com.example.luis.placerest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Luis on 3/18/2016.
 */
public class RestaurantViewHolder extends RecyclerView.ViewHolder {
protected TextView vName;
protected TextView vAddress;

public RestaurantViewHolder(View v) {
        super(v);
        vName = (TextView) v.findViewById(R.id.info_text);
        vAddress =(TextView) v.findViewById(R.id.description);
        }
}
