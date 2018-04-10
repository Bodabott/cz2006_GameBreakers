package com.example.gamebreakers.owner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.Order;
import com.example.gamebreakers.owner.Fragment_Owner_BusinessMode.OnOrderSelectedListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link OnOrderSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Adapter_Owner_BusinessMode extends RecyclerView.Adapter<Adapter_Owner_BusinessMode.ViewHolder> {

    private final List<Order> mValues;
    private final OnOrderSelectedListener mListener;
    private final Activity_Owner_BusinessMode mActivity;

    public Adapter_Owner_BusinessMode(List<Order> items, Fragment_Owner_BusinessMode.OnOrderSelectedListener listener, Activity_Owner_BusinessMode activity) {
        mValues = items;
        mListener = listener;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getFoodName());
        holder.mContentView.setText(mValues.get(position).getFoodName());

        holder.mView.setOnTouchListener(new OnSwipeTouchListener(mActivity) {
            public void onTap() {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onOrderSelected(holder.mItem.getFoodName());
                }
            }
            public void onSwipeRight() {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.finishOrder(holder.mItem.getFoodName());
                }
            }
            public void onSwipeLeft() {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.cancelOrder(holder.mItem.getFoodName());
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
            mIdView = (TextView) view.findViewById(R.id.order_id);
            mContentView = (TextView) view.findViewById(R.id.order_content);
        }

        @Override
        public java.lang.String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
