package com.example.gamebreakers.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gamebreakers.FoodDishesAdapter;
import com.example.gamebreakers.manually_keyed_data.Food_Details;
import com.example.gamebreakers.R;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class FoodDescriptionFragment extends Fragment {

    public final static String ARG_POSITION = "position";
    private int currentPosition = -1;

    ListView mFoodDishesView = null;
    FoodDishesAdapter mFoodDishesAdapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){
            //if we recreated this Fragment (for instance from a screen rotate)
            //restore the previous article selection
            currentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        //inflate the view for this fragment
        View mFragmentView = inflater.inflate(R.layout.food_description_fragment,container,false);

        return mFragmentView;
    }

    public void updateArticleView(int position){

        final View v = getView();
        TextView article = v.findViewById(R.id.article);
        String[] data = Food_Details.FoodDescription;
        article.setText(data[position]);
        currentPosition = position;

        mFoodDishesView = v.findViewById(R.id.myFoodDishesView);
        mFoodDishesAdapter = new FoodDishesAdapter(v.getContext(),R.layout.food_dishes_row,Food_Details.Food_Dishes_Details);
        if(mFoodDishesView != null){
            mFoodDishesView.setAdapter(mFoodDishesAdapter);
        }
        mFoodDishesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("FoodCategory",Food_Details.Food_Dishes_Details[i]);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //During startup, we should check if there are arguments (data)
        //passed to this Fragment. We know the layout has already been
        //applied to the Fragment, so we can safely call the method that
        //sets the article text

        Bundle args = getArguments();
        if(args != null){
            //set the article based on the argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
        }else if (currentPosition != -1){
            //set the article based on the saved instance state defined during onCreateView
            updateArticleView(currentPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the current selection for later recreation of this Fragment
        outState.putInt(ARG_POSITION,currentPosition);
    }
}

