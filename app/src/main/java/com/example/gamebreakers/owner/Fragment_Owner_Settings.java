package com.example.gamebreakers.owner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.entities.SQL;

public class Fragment_Owner_Settings extends Fragment {
    SQL myDb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_settings,container, false);

        myDb=new SQL();
        String stallName = ((Activity_Owner) getActivity()).stallName;

        TextView stalltext= view.findViewById(R.id.settings_stallName);
        stalltext.setText(stallName);

        TextView username= view.findViewById(R.id.settings_username);
        username.setText(myDb.getOwnerUsername(stallName));
        TextView pass= view.findViewById(R.id.settings_password);
        pass.setText(myDb.getOwnerPassword(stallName));

        return view;
    }
}
