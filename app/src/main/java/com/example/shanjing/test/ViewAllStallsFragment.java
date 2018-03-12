package com.example.shanjing.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Created by Shan Jing on 8/3/2018.
 */

public class ViewAllStallsFragment extends Fragment {

    onFragmentViewStall viewStall;

    public interface onFragmentViewStall {
        void displayStall(int i);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            viewStall = (onFragmentViewStall) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onFragmentViewStall");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_stalls, container, false);

        if (container != null) {
            container.removeAllViews();  //remove things in the background
        }

        String[] stalls = {"Chicken Rice Stall", "Duck Rice Stall", "Pork Rice Stall", "Fish Rice Stall"};

        ListView listView = (ListView)view.findViewById(R.id.listOfStalls);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stalls);

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewStall.displayStall(i);
            }
        });

        return view;
    }
}
