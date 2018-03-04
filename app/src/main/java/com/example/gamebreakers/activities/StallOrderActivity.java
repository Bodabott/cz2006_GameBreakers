package com.example.gamebreakers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gamebreakers.OnHeadlineSelectedListener;
import com.example.gamebreakers.R;
import com.example.gamebreakers.fragments.StallDescriptionFragment;
import com.example.gamebreakers.fragments.StallNameFragment;

/**
 * Created by zNotAgain on 28/2/2018.
 */

public class StallOrderActivity extends Activity implements OnHeadlineSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall_order);

        //check if the activity is using the layout version
        //with the FrameLayout, if so, we have to add the fragment
        //(it won't be done automatically)
        if(findViewById(R.id.container) != null){

            //However if we're being restored from a previous state,
            //then don't do anything
            if(savedInstanceState != null){
                return;
            }

            //Create an instance of the Headline Fragment
            StallNameFragment stallNameFragment = new StallNameFragment();

            //In the case this activity was started with special instructions from an Intent,
            //pass the Intent's extras to the fragment as arguments
            stallNameFragment.setArguments(getIntent().getExtras());

            //Ask the Fragment manager to add it to the FrameLayout
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, stallNameFragment)
                    .commit();

        }
    }

    //runs when list item is selected.
    @Override
    public void onArticleSelected(int position) {
        //Capture the article fragment from the activity's dual-pane layout
        StallDescriptionFragment stallDescriptionFragment = (StallDescriptionFragment) getFragmentManager().findFragmentById(R.id.article_fragment);

        //if we don't find one, we must not be in two pane mode
        //let's swap the Fragments instead
        if(stallDescriptionFragment != null){

            //we must be in two pane layout
            stallDescriptionFragment.updateArticleView(position);

        }else{
            //we must be in one pane layout

            //Create Fragment and give it an argument for the selected article right away
            StallDescriptionFragment swapFragment = new StallDescriptionFragment();
            Bundle args = new Bundle();
            args.putInt(StallDescriptionFragment.ARG_POSITION,position);
            swapFragment.setArguments(args);

            //now that the Fragment is prepared, swap it
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,swapFragment)   //go to stall description fragment
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void goFoodActivity(View view) {

        Intent goIntent = new Intent(view.getContext(),FoodActivity.class);
        startActivity(goIntent);
    }

    public void goMainMenu(View view) {
        Intent loginIntent = new Intent(view.getContext(),MainMenuActivity.class);
        startActivity(loginIntent);
    }

    public void returnToStallOrderActivity(View v) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container,new StallNameFragment())
                .commit();
    }
}