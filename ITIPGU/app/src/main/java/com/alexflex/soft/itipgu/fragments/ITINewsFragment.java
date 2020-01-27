package com.alexflex.soft.itipgu.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alexflex.soft.itipgu.R;
import com.alexflex.soft.itipgu.logic.ITIParserTask;
import com.alexflex.soft.itipgu.logic.NewsParserTask;


public class ITINewsFragment extends Fragment {


    private View view;
    private FrameLayout frameLayout;

    public ITINewsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_itinews, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart(){
        super.onStart();
        frameLayout = view.findViewById(R.id.main_viewgroup);
    }

    @Override
    public void onResume(){
        super.onResume();
        new ITIParserTask(this.getContext(), frameLayout).execute();
    }

}
