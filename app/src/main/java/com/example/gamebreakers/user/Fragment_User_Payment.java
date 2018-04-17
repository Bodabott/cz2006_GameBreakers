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
        String stallname= act.stall.getStallName();
        String food=act.food;

        if (stallname!=null) {
            TextView selectedStall = view.findViewById(R.id.selected_stall);
            String temp = "Stall: " + stallname;
            selectedStall.setText(temp);
        }
        if (food!=null) {
            TextView selectedfood = view.findViewById(R.id.selected_food);
            String temp = "Food: " + food;
            selectedfood.setText(temp);
        }

        TextView text = view.findViewById(R.id.earliestTime);
        int hour = act.getEarliestOrderTime().getHour();
        int minute = act.getEarliestOrderTime().getMinute();
        String newHour,newMinute;
        if((hour/10) == 0){
            newHour = "0" + String.valueOf(hour);
            if((minute/10) == 0){
                newMinute = "0" + String.valueOf(minute);
                String temp = newHour + ":" + newMinute + " ";
                text.setText(temp);
            }
        }else{
            String temp = hour + ":" + minute + " ";
            text.setText(temp);
        }
        return view;
    }
}
