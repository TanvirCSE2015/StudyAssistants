package com.ashik.justice.developer.studyassistants;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class Notification extends Fragment {

    public Notification() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  Toast.makeText(container.getContext(), "success", Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }


}
