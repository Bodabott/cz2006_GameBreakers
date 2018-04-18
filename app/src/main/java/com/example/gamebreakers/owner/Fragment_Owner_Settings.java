package com.example.gamebreakers.owner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.SQL;

public class Fragment_Owner_Settings extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_settings,container, false);

        String stallName = ((Activity_Owner) getActivity()).stallName;

        TextView stallText= view.findViewById(R.id.settings_stallName);
        stallText.setText(stallName);

        TextView username= view.findViewById(R.id.settings_username);
        username.setText(SQL.getOwnerUsername(stallName));
        TextView pass= view.findViewById(R.id.settings_password);
        pass.setText(SQL.getOwnerPassword(stallName));

        TextView postalCode = view.findViewById(R.id.settings_postalCode);
        postalCode.setText(SQL.getOwnerPostalCode(stallName));

        return view;
    }
}
