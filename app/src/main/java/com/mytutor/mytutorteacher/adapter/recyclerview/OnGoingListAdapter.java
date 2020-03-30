package com.mytutor.mytutorteacher.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mytutor.mytutorteacher.R;
import com.mytutor.mytutorteacher.model.Teacher;

import java.util.ArrayList;

/*
@Author cr7
@CreatedOn 3/29/2020
*/public class OnGoingListAdapter extends RecyclerView.Adapter<OnGoingListAdapter.OnGoingViewHolder> {

    private ArrayList<Teacher> teacherArrayList;

    public OnGoingListAdapter(ArrayList<Teacher> teacherArrayList) {
        this.teacherArrayList = teacherArrayList;
    }

    @NonNull
    @Override
    public OnGoingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnGoingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.container_teacher, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnGoingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return teacherArrayList.size();
    }

    static class OnGoingViewHolder extends RecyclerView.ViewHolder {

        public OnGoingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
