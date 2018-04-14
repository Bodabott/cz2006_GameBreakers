package com.example.gamebreakers.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.entities.Food;
import com.example.gamebreakers.entities.SQL;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFoodSelectedListener}
 * interface.
 */
public class Fragment_User_BrowseFood extends Fragment {

    // TODO: Customize parameter argument names
    private static final java.lang.String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnFoodSelectedListener mListener;

    List<Food> food_List;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Fragment_User_BrowseFood() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        //get selected stall
        Activity_User act = (Activity_User) getActivity();
        String stallName= act.stall.getStallName();
        //get list of food
        Food[] foodArray = SQL.getStallMenu(stallName);
        food_List = Arrays.asList(foodArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_food, container, false);

        //check selected values. If have, update textview
        Activity_User act = (Activity_User) getActivity();
        String stallname= act.stall.getStallName();
        String food=act.food;

        if (stallname!=null) {
            TextView selectedStall = view.findViewById(R.id.selected_stall);
            selectedStall.setText("Stall: "+stallname);
        }
        if (food!=null) {
            TextView selectedStall = view.findViewById(R.id.selected_food);
            selectedStall.setText("Food: "+food);
        }

        // Set the adapter
        View list = view.findViewById(R.id.item_list);
        if (list instanceof RecyclerView) {
            Context context = list.getContext();
            RecyclerView recyclerView = (RecyclerView) list;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new Adapter_User_FoodList(food_List, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFoodSelectedListener) {
            mListener = (OnFoodSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOrderSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFoodSelectedListener {
        // TODO: Update argument type and name
        void onFoodSelected(String item);
    }
}
