package com.example.shanjing.test;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StallFragment extends Fragment {

    Toolbar toolbar;
    ImageView picture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stall, container, false);

        int i = getArguments().getInt("int");

        if (container != null) {
            container.removeAllViews();  //remove things in the background
        }

        toolbar = (Toolbar) view.findViewById(R.id.stall_toolbar);
        picture = (ImageView) view.findViewById(R.id.stall_image);

        final ArrayList<String> stalls = new ArrayList<>();
        stalls.add("Chicken Rice Stall");
        stalls.add("Duck Rice Stall");
        stalls.add("Pork Rice Stall");
        stalls.add("Fish Rice Stall");

        toolbar.setTitle(stalls.get(i));

        if(toolbar.getTitle().toString().equalsIgnoreCase("Chicken Rice Stall")) {
            picture.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.chicken));
        }
        else if(toolbar.getTitle().toString().equalsIgnoreCase("Duck Rice Stall")) {
            picture.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.duck));
        }
        else if(toolbar.getTitle().toString().equalsIgnoreCase("Pork Rice Stall")) {
            picture.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.pork));
        }
        else if(toolbar.getTitle().toString().equalsIgnoreCase("Fish Rice Stall")) {
            picture.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.fish));
        }

        return view;
    }

}
