package com.example.gamebreakers.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gamebreakers.OnHeadlineSelectedListener;
import com.example.gamebreakers.manually_keyed_data.Food_Details;
import com.example.gamebreakers.R;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class FoodNameFragment extends ListFragment {

    OnHeadlineSelectedListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Checks that the container Activity has implemented necessary interface
        //if not, throw an exception so we can fix it
        try{
            callback = (OnHeadlineSelectedListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException((activity.toString()) + "must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int layout = android.R.layout.simple_list_item_activated_1; //not available before honeycomb
        String[] data = Food_Details.FoodName;

        setListAdapter(new ArrayAdapter<String>(getActivity(),layout,data));
    }

    @Override
    public void onStart() {
        super.onStart();

        //When in a two-pane layout, set the lightview to highlight the list item
        //instead of just simply blinking
        Fragment f = getFragmentManager().findFragmentById(R.id.article_fragment);
        ListView v = getListView();
        if(f != null && v != null){
            v.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //Notify the parent of the selected item
        callback.onArticleSelected(position);

        //again set the item to be highlighted in a two-pane layout
        l.setItemChecked(position,true);

    }
}
