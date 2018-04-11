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
import com.example.gamebreakers.entities.Stall;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnStallNameSelectedListener}
 * interface.
 */
public class Fragment_User_BrowseStall extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnStallNameSelectedListener mListener;

    DatabaseHelper myDb;
    List<Stall> stallName_List;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Fragment_User_BrowseStall() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        myDb= new DatabaseHelper(getContext());

        Stall[] stall_Array = myDb.getArrayOfStall();
        stallName_List = Arrays.asList(stall_Array);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_stall, container, false);
        //check selected values. If have, update textview
        Activity_User act = (Activity_User) getActivity();
        Stall stall= act.stall;
        if (stall!=null) {
            TextView selectedStall = view.findViewById(R.id.selected_stall);
            selectedStall.setText("Stall: "+stall.getStallName());
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
            recyclerView.setAdapter(new Adapter_User_StallList(stallName_List, mListener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStallNameSelectedListener) {
            mListener = (OnStallNameSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStallNameSelectedListener");
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
    public interface OnStallNameSelectedListener {
        void onStallNameSelected(Stall stall);
    }
}
