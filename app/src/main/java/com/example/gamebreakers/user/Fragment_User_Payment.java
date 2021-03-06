package com.example.gamebreakers.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebreakers.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Fragment_User_Payment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_payment, container, false);

        //check selected values. If have, update textView
        Activity_User act = (Activity_User) getActivity();
        String stallName = act.stall.getStallName();
        String food = act.food;

        if (stallName != null) {
            TextView selectedStall = view.findViewById(R.id.selected_stall);
            String temp = "Stall: " + stallName;
            selectedStall.setText(temp);
        }
        if (food!=null) {
            TextView selectedFood = view.findViewById(R.id.selected_food);
            String temp = "Food: " + food;
            selectedFood.setText(temp);
        }

        TextView text = view.findViewById(R.id.earliestTime);
        Calendar cal = act.getEarliestOrderTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        String temp = sdf.format(cal.getTime());
        text.setText(temp + " ");

        return view;
    }
}
