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

import com.example.gamebreakers.entities.Stall_Details;
import com.example.gamebreakers.R;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_Category extends Activity {
    public static final String CATEGORY_NAME = "Activity_Category";

    String[] mCategoryArray = Stall_Details.Stall_Category_Details;

    ListView mListView;
    Adapter_Category mAdapterCategory;
    TextView mCategoryTextView,mStallTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mListView = findViewById(R.id.myCategoryListView);
        mStallTextView = findViewById(R.id.stall_status);
        mCategoryTextView = findViewById(R.id.category_status);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String categoryMessage = intent.getStringExtra(CATEGORY_NAME);
        final String stallMessage = intent.getStringExtra(Activity_Stall.STALL_NAME);

        if(stallMessage != null){
            mStallTextView.setText(stallMessage);
            if(categoryMessage != null){
                mCategoryTextView.setText(categoryMessage);
            }
        }

        mAdapterCategory = new Adapter_Category(getApplicationContext(),R.layout.row_category,mCategoryArray);

        if(mListView != null){
            mListView.setAdapter(mAdapterCategory);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("PLACE",mCategoryArray[position]);
                String temp = "Category: " + mCategoryArray[position];
                mCategoryTextView.setText(temp);
            }
        });
    }

    /** Called when the user taps the Send button */
    public void chooseFood(View view) {
        Intent intent = new Intent(this, Activity_Food.class);

        TextView stallNameTextView = findViewById(R.id.stall_status);
        TextView categoryTextView = findViewById(R.id.category_status);

        String stallNameMessage = stallNameTextView.getText().toString();
        String categoryMessage = categoryTextView.getText().toString();

        if(categoryMessage.matches("Category: ")){
            Toast.makeText(view.getContext(),"Category not chosen",Toast.LENGTH_LONG).show();
            return;
        }

        intent.putExtra(Activity_Stall.STALL_NAME, stallNameMessage);
        intent.putExtra(CATEGORY_NAME, categoryMessage);

        startActivity(intent);
    }

    public void backtoStall(View v){
        Intent backIntent = new Intent(v.getContext(),Activity_Stall.class);

        TextView stallNameTextView = findViewById(R.id.stall_status);
        String stallNameMessage = stallNameTextView.getText().toString();

        backIntent.putExtra(Activity_Stall.STALL_NAME,stallNameMessage);
        startActivity(backIntent);
    }

    public void backtoUserMenu(View view) {
        Intent backIntent = new Intent(view.getContext(),Activity_UserMenu.class);
        startActivity(backIntent);
    }
}
