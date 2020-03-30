package com.mytutor.mytutorteacher.ui.dashboard.ongoing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mytutor.mytutorteacher.R;
import com.mytutor.mytutorteacher.adapter.recyclerview.OnGoingListAdapter;

import java.util.ArrayList;

/*
@Author cr7
@CreatedOn 3/28/2020
*/
public class OnGoingFragment extends Fragment {
    public static final String FRAGMENT_TYPE = "fragment_type";
    private RecyclerView mRecyclerview;

    private OnGoingListAdapter onGoingListAdapter;
    public static OnGoingFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TYPE, type);
        OnGoingFragment fragment = new OnGoingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onGoingListAdapter=new OnGoingListAdapter(new ArrayList());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_ongoing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerview = view.findViewById(R.id.ongoing_recyclerview);
        mRecyclerview.setAdapter(onGoingListAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
