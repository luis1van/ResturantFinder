package com.example.luis.placerest;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    private int mCurrentlyExpandedPosition = RecyclerView.NO_POSITION;
    private long mCurrentlyExpandedRowId = NO_EXPANDED_LIST_ITEM;
    private static final int NO_EXPANDED_LIST_ITEM = -1;
    private final View.OnClickListener mExpandCollapseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RestaurantViewHolder viewHolder = (RestaurantViewHolder) v.getTag();
            if (viewHolder == null) {
                return;
            }

            if (viewHolder.getAdapterPosition() == mCurrentlyExpandedPosition) {
                // Hide actions, if the clicked item is the expanded item.
                viewHolder.showActions(false,restaurantList.get(viewHolder.rowId));

                mCurrentlyExpandedPosition = RecyclerView.NO_POSITION;
                mCurrentlyExpandedRowId = NO_EXPANDED_LIST_ITEM;
            } else {
                expandViewHolderActions(viewHolder);
            }

        }
    };

    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder restaurantViewHolder, int i) {

        bindCallLogListViewHolder(restaurantViewHolder,i);

    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return createRestaurantViewHolder(viewGroup);
    }

    private void expandViewHolderActions(RestaurantViewHolder viewHolder) {
        // If another item is expanded, notify it that it has changed. Its actions will be
        // hidden when it is re-binded because we change mCurrentlyExpandedPosition below.
        if (mCurrentlyExpandedPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(mCurrentlyExpandedPosition);
        }
        // Show the actions for the clicked list item.
        viewHolder.showActions(true,restaurantList.get(viewHolder.getAdapterPosition()));
        mCurrentlyExpandedPosition = viewHolder.getAdapterPosition();
        mCurrentlyExpandedRowId = viewHolder.rowId;
    }
    private RestaurantViewHolder createRestaurantViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_view_item, parent, false);
        RestaurantViewHolder viewHolder = RestaurantViewHolder.create(
                view,
               parent.getContext(),
                mExpandCollapseListener);
//                mTelecomCallLogCache,
//                mCallLogListItemHelper,
//                mVoicemailPlaybackPresenter);

      viewHolder.cardView.setTag(viewHolder);
        //viewHolder.callLogEntryView.setAccessibilityDelegate(mAccessibilityDelegate);

       //viewHolder.primaryActionView.setOnCreateContextMenuListener(mOnCreateContextMenuListener);
        viewHolder.primaryActionView.setTag(viewHolder);

        return viewHolder;
    }

    private void bindCallLogListViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RestaurantViewHolder views = (RestaurantViewHolder) viewHolder;
        Restaurant ri = restaurantList.get(viewHolder.getAdapterPosition());
        views.rowId=position;
        views.vName.setText(ri.getName());
        views.vAddress.setText(ri.getAddress());

/*      views.cWeb.setText(ri.getWebsite() == null ? "" : ri.getWebsite());
        views.cTel.setText(ri.getTel()==null ? "" : ri.getTel());*/
        views.showActions(mCurrentlyExpandedPosition == position,restaurantList.get(viewHolder.getAdapterPosition()));

    }

}