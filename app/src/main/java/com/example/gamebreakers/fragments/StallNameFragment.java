package com.example.gamebreakers.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gamebreakers.OnHeadlineSelectedListener;
import com.example.gamebreakers.R;
import com.example.gamebreakers.manually_keyed_data.Stall_Details;

/**
 * Created by zNotAgain on 28/2/2018.
 */

public class StallNameFragment extends ListFragment {

    OnHeadlineSelectedListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Checks that the container Activity has implemented necessary interface
        //if not, throw an exception so we can fix it
        try{
            callback = (OnHeadlineSelectedListener) activity; //IMPORTANT. sets callback to implementation activity
        }catch(ClassCastException e){
            throw new ClassCastException((activity.toString()) + "must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int layout = android.R.layout.simple_list_item_activated_1; //A_studio command. opens list
        String[] data = Stall_Details.StallName;        //string array. data inside S_details.java

        //updates text within list.
        setListAdapter(new ArrayAdapter<String>(getActivity(),layout,data));
    }

    //method is part of A_studio Listfragment. But it is overwritten here.
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //Notify the parent of the selected item
        callback.onArticleSelected(position);

        //again set the item to be highlighted in a two-pane layout
        l.setItemChecked(position,true);

    }
}
