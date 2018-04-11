package com.example.gamebreakers.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gamebreakers.R;

public class Fragment_User_MainMenu extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_mainmenu,container, false);

        Activity_User act = (Activity_User) getActivity();
        TextView text = view.findViewById(R.id.balance);
        text.setText("Balance: $" +act.user.getBalance());

        return view;
    }
}
