package com.vignesh.revealmydestination;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vignesh.revealmydestination.ListTripFragment.OnListFragmentInteractionListener;
import com.vignesh.revealmydestination.Model.Trip;
import com.vignesh.revealmydestination.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTripRecyclerViewAdapter extends RecyclerView.Adapter<MyTripRecyclerViewAdapter.ViewHolder> {

    private final List<Trip> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTripRecyclerViewAdapter(List<Trip> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //holder.mItem = mValues.get(position);
        holder.sourceLocation.setText(mValues.get(position).getSrc_location());
        holder.destinationLocation.setText(mValues.get(position).getDst_location());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(mValues.get(position));
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setBackgroundColor(Color.BLUE);
                Log.d("view long clicked", mValues.get(position).toString());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView sourceLocation;
        public final TextView destinationLocation;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            sourceLocation = (TextView) view.findViewById(R.id.tripSourceLocationText);
            destinationLocation = (TextView) view.findViewById(R.id.tripDestinationLocationText);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + destinationLocation.getText() + "'";
        }
    }

}
