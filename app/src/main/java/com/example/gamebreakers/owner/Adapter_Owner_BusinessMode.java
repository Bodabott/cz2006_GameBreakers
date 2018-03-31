package com.example.gamebreakers.owner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebreakers.R;
<<<<<<< HEAD
import com.example.gamebreakers.entities.Order;
=======
>>>>>>> parent of 4839ec0... Revert "Merge remote-tracking branch 'origin/master'"
import com.example.gamebreakers.owner.Fragment_Owner_BusinessMode.OnOrderSelectedListener;

import java.util.List;

/**
<<<<<<< HEAD
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
=======
 * {@link RecyclerView.Adapter} that can display a {@link String} and makes a call to the
>>>>>>> parent of 4839ec0... Revert "Merge remote-tracking branch 'origin/master'"
 * specified {@link OnOrderSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Adapter_Owner_BusinessMode extends RecyclerView.Adapter<Adapter_Owner_BusinessMode.ViewHolder> {

<<<<<<< HEAD
    private final List<Order> mValues;
    private final OnOrderSelectedListener mListener;
    private final Activity_Owner_BusinessMode mActivity;

    public Adapter_Owner_BusinessMode(List<Order> items, Fragment_Owner_BusinessMode.OnOrderSelectedListener listener, Activity_Owner_BusinessMode activity) {
=======
    private final List<String> mValues;
    private final OnOrderSelectedListener mListener;
    private final Activity_Owner_BusinessMode mActivity;

    public Adapter_Owner_BusinessMode(List<String> items, Fragment_Owner_BusinessMode.OnOrderSelectedListener listener, Activity_Owner_BusinessMode activity) {
>>>>>>> parent of 4839ec0... Revert "Merge remote-tracking branch 'origin/master'"
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
<<<<<<< HEAD
        holder.mIdView.setText(mValues.get(position).getFoodName());
        holder.mContentView.setText(mValues.get(position).getFoodName());
=======
        holder.mIdView.setText(mValues.get(position));
        holder.mContentView.setText(mValues.get(position));
>>>>>>> parent of 4839ec0... Revert "Merge remote-tracking branch 'origin/master'"

        holder.mView.setOnTouchListener(new OnSwipeTouchListener(mActivity) {
            public void onTap() {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
<<<<<<< HEAD
                    mListener.onOrderSelected(holder.mItem.getFoodName());
=======
                    mListener.onOrderSelected(holder.mItem);
>>>>>>> parent of 4839ec0... Revert "Merge remote-tracking branch 'origin/master'"
                }
            }
            public void onSwipeRight() {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
<<<<<<< HEAD
                    mListener.finishOrder(holder.mItem.getFoodName());
=======
                    mListener.finishOrder(holder.mItem);
>>>>>>> parent of 4839ec0... Revert "Merge remote-tracking branch 'origin/master'"
                }
            }
            public void onSwipeLeft() {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
<<<<<<< HEAD
                    mListener.cancelOrder(holder.mItem.getFoodName());
=======
                    mListener.cancelOrder(holder.mItem);
>>>>>>> parent of 4839ec0... Revert "Merge remote-tracking branch 'origin/master'"
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
<<<<<<< HEAD
        public Order mItem;
=======
        public String mItem;
>>>>>>> parent of 4839ec0... Revert "Merge remote-tracking branch 'origin/master'"

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
