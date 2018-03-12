package com.example.gamebreakers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zNotAgain on 28/2/2018.
 */

public class StallCategoryAdapter extends ArrayAdapter<String> {

    Context mContext;
    int mLayoutResId;
    String mData[] = null;

    public StallCategoryAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mLayoutResId = resource;
        this.mData = objects;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        PlaceHolder holder = null;

        //if we currently don't have a row View to reuse...
        if(row == null){
            //Create a new View
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResId,parent,false);

            holder = new PlaceHolder();

            holder.nameView = (TextView) row.findViewById(R.id.foodCategoryName);

            row.setTag(holder);
        }else{
            //Otherwise use an existing View
            holder = (PlaceHolder) row.getTag();
        }

        //Getting the data from the data array
        String fcd = mData[position];

        //Setup and reuse the same listener for each row
        holder.nameView.setOnClickListener(PopupListener);
        Integer rowPosition = position;
        holder.nameView.setTag(rowPosition);

        //setting the view to reflect the data we need to display
        holder.nameView.setText(fcd);

        //returning the row (because this is called getView after all
        return row;

    }

    View.OnClickListener PopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer viewPosition = (Integer) v.getTag();
            String fcd = mData[viewPosition];
            Toast.makeText(getContext(),fcd, Toast.LENGTH_SHORT).show();
        }
    };

    private static class PlaceHolder {
        TextView nameView;
    }
}
