package com.example.gamebreakers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gamebreakers.OnHeadlineSelectedListener;
import com.example.gamebreakers.fragments.FoodDescriptionFragment;
import com.example.gamebreakers.fragments.FoodNameFragment;
import com.example.gamebreakers.R;
import com.example.gamebreakers.fragments.StallNameFragment;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class FoodActivity extends Activity implements OnHeadlineSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        //check if the activity is using the layout version
        //with the FrameLayout, if so, we have to add the fragment
        //(it won't be done automatically)
        if(findViewById(R.id.foodcontainer) != null){

            //However if we're being restored from a previous state,
            //then don't do anything
            if(savedInstanceState != null){
                return;
            }

            //Create an instance of the Headline Fragment
            FoodNameFragment foodNameFragment = new FoodNameFragment();

            //In the case this activity was started with special instructions from an Intent,
            //pass the Intent's extras to the fragment as arguments
            foodNameFragment.setArguments(getIntent().getExtras());

            //Ask the Fragment manager to add it to the FrameLayout
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.foodcontainer, foodNameFragment)
                    .commit();

        }
    }

    @Override
    public void onArticleSelected(int position) {
        //Capture the article fragment from the activity's dual-pane layout
        FoodDescriptionFragment foodDescriptionFragment = (FoodDescriptionFragment) getFragmentManager().findFragmentById(R.id.article_fragment);

        //if we don't find one, we must not be in two pane mode
        //let's swap the Fragments instead
        if(foodDescriptionFragment != null){

            //we must be in two pane layout
            foodDescriptionFragment.updateArticleView(position);

        }else{
            //we must be in one pane layout

            //Create Fragment and give it an argument for the selected article right away
            FoodDescriptionFragment swapFragment = new FoodDescriptionFragment();
            Bundle args = new Bundle();
            args.putInt(FoodDescriptionFragment.ARG_POSITION,position);
            swapFragment.setArguments(args);

            //now that the Fragment is prepared, swap it
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.foodcontainer,swapFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void goStallOrder(View view) {
        Intent stallOrderIntent = new Intent(view.getContext(),StallOrderActivity.class);
        startActivity(stallOrderIntent);
    }

    public void goPayment(View view) {
        Intent goIntent = new Intent(view.getContext(),PaymentActivity.class);
        startActivity(goIntent);
    }

    public void returnFoodActivity(View v) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container,new FoodNameFragment())
                .commit();
    }
}