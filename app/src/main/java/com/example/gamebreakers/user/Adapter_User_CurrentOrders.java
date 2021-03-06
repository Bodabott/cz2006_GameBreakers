package com.example.gamebreakers.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.Order;
import com.example.gamebreakers.user.Fragment_User_CurrentOrders.OnOrderSelectedListener;

import java.util.List;
import java.util.Random;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link Fragment_User_CurrentOrders.OnOrderSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Adapter_User_CurrentOrders extends RecyclerView.Adapter<Adapter_User_CurrentOrders.ViewHolder> {

    private final List<Order> mValues;
    private final Fragment_User_CurrentOrders.OnOrderSelectedListener mListener;

    public Adapter_User_CurrentOrders(List<Order> items, OnOrderSelectedListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getFoodName());
        boolean cooked = mValues.get(position).isCompleted();

        Random ran =  new Random();
        int pass = ran.nextInt(9999);

        String temp = mValues.get(position).getCollectiontime() + "   status:" + (cooked ? " done (" + pass + ") " : " cooking");
        holder.mContentView.setText(temp);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onOrderSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Order mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_id);
            mContentView = (TextView) view.findViewById(R.id.item_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
