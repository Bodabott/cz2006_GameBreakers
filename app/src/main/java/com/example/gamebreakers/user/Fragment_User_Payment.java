package com.example.gamebreakers.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebreakers.R;

public class Fragment_User_Payment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_payment, container, false);

        //check selected values. If have, update textview
        Activity_User act = (Activity_User) getActivity();
        String stallname= act.stallName;
        String food=act.food;

        if (stallname!=null) {
            TextView selectedStall = view.findViewById(R.id.selected_stall);
            selectedStall.setText("Stall: "+stallname);
        }
        if (food!=null) {
            TextView selectedStall = view.findViewById(R.id.selected_food);
            selectedStall.setText("Food: "+food);
        }

        return view;
    }
}
