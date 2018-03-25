package com.example.gamebreakers.owner;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.Food;
import com.example.gamebreakers.owner.Fragment_Owner_ManageMenu.OnMenuItemSelectedListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link String} and makes a call to the
 * specified {@link OnMenuItemSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Adapter_Owner_ManageMenu extends RecyclerView.Adapter<Adapter_Owner_ManageMenu.ViewHolder> {

    private final List<Food> mValues;
    private final OnMenuItemSelectedListener mListener;

    public Adapter_Owner_ManageMenu(List<Food> items, OnMenuItemSelectedListener listener) {
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
        holder.mContentView.setText("$"+mValues.get(position).getPrice()/100
                +"."+mValues.get(position).getPrice()%100);
        holder.mImageView.setImageResource(R.drawable.ic_delete);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onMenuItemSelected(holder.mItem.getFoodName());
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
        public final ImageView mImageView;
        public Food mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_id);
            mContentView = (TextView) view.findViewById(R.id.item_content);
            mImageView = view.findViewById(R.id.item_image);
        }

        @Override
        public java.lang.String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
