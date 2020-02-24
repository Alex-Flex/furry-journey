package com.alexflex.soft.itipgu.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.alexflex.soft.itipgu.R;
import com.alexflex.soft.itipgu.logic.NewsParserTask;

public class NewsFeedFragment extends Fragment {

    public NewsFeedFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = view.findViewById(R.id.layout_in_activity);
        new NewsParserTask(this.getContext(), frameLayout).execute();
    }
}
