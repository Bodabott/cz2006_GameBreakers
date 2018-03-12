package com.example.shanjing.test;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




/**
 * Created by Shan Jing on 8/3/2018.
 */

public class BrowseFragment extends Fragment {

    OnFragmentSendList sendList;

    public interface OnFragmentSendList {
        void displayList();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendList = (OnFragmentSendList) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnFragmentSendList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.view_stalls_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    sendList.displayList();
                }
        });
    }


}
