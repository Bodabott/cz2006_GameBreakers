package com.example.gamebreakers.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.Food;
import com.example.gamebreakers.entities.Food_Details;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_Food extends Activity {

    public static final String FOOD_NAME = "Activity_Food";

    Food[] mFoodArray = Food_Details.mFoodArray;

    // Initialise
    ListView mListView;
    Adapter_Food mAdapterFood;
    TextView mCategoryTextView,mStallTextView,mFoodTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        // Connect to respective items in layout
        mListView = findViewById(R.id.myFoodListView);
        mStallTextView = findViewById(R.id.stall_status);
        mCategoryTextView = findViewById(R.id.category_status);
        mFoodTextView = findViewById(R.id.food_status);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String stallMessage = intent.getStringExtra(Activity_Stall.STALL_NAME);
        String categoryMessage = intent.getStringExtra(Activity_Category.CATEGORY_NAME);
        String foodMessage = intent.getStringExtra(FOOD_NAME);

        if(stallMessage != null){
            mStallTextView.setText(stallMessage);
            if(categoryMessage != null){
                mCategoryTextView.setText(categoryMessage);
                if(foodMessage != null){
                    mFoodTextView.setText(foodMessage);
                }
            }
        }

        mAdapterFood = new Adapter_Food(getApplicationContext(),R.layout.row_food,mFoodArray);

        if(mListView != null){
            mListView.setAdapter(mAdapterFood);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("PLACE",mFoodArray[position].getFoodName());
                String temp = "Food: " + mFoodArray[position].getFoodName();
                mFoodTextView.setText(temp);
            }
        });
    }

    /** Called when the user taps the Send button */
    public void placeOrder(View view) {
        Intent intent = new Intent(this, Activity_Payment.class);

        TextView stallNameTextView = findViewById(R.id.stall_status);
        TextView categoryTextView = findViewById(R.id.category_status);
        TextView foodTextView = findViewById(R.id.food_status);

        String stallNameMessage = stallNameTextView.getText().toString();
        String categoryMessage = categoryTextView.getText().toString();
        String foodMessage = foodTextView.getText().toString();

        if(foodMessage.matches("Food: ")){
            Toast.makeText(view.getContext(),"Food not chosen",Toast.LENGTH_LONG).show();
            return;
        }

        intent.putExtra(Activity_Stall.STALL_NAME, stallNameMessage);
        intent.putExtra(Activity_Category.CATEGORY_NAME, categoryMessage);
        intent.putExtra(FOOD_NAME, foodMessage);

        startActivity(intent);
    }

    public void backtoCategory(View v) {
        Intent backIntent = new Intent(v.getContext(),Activity_Category.class);

        TextView stallNameTextView = findViewById(R.id.stall_status);
        TextView categoryNameTextView = findViewById(R.id.category_status);

        String stallNameMessage = stallNameTextView.getText().toString();
        String categoryNameMessage = categoryNameTextView.getText().toString();

        backIntent.putExtra(Activity_Stall.STALL_NAME,stallNameMessage);
        backIntent.putExtra(Activity_Category.CATEGORY_NAME,categoryNameMessage);

        startActivity(backIntent);
    }

    public void backtoUserMenu(View v) {
        Intent backIntent = new Intent(v.getContext(),Activity_UserMenu.class);
        startActivity(backIntent);
    }
}
