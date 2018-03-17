package com.example.gamebreakers.owner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebreakers.R;

public class Fragment_Owner_MainMenu extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_owner_mainmenu,container, false);
        TextView stallNameTextView = view.findViewById(R.id.stall_Name);

        String stallName=((Activity_Owner) getActivity()).stallName;

        if(stallName != null)
            stallNameTextView.setText(stallName);
        return view;
    }
}
