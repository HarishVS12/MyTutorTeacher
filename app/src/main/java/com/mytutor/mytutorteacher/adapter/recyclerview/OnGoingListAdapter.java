package com.mytutor.mytutorteacher.adapter.recyclerview;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mytutor.mytutorteacher.R;
import com.mytutor.mytutorteacher.ui.utils.AppointmentMap;

import java.util.ArrayList;
import java.util.HashMap;

/*
@Author cr7
@CreatedOn 3/29/2020
*/public class OnGoingListAdapter extends RecyclerView.Adapter<OnGoingListAdapter.OnGoingViewHolder> {

    private ArrayList<HashMap<String, Object>> teacherArrayList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private View view;


    public OnGoingListAdapter(ArrayList<HashMap<String, Object>> teacherArrayList) {
        this.teacherArrayList = teacherArrayList;
    }


    @NonNull
    @Override
    public OnGoingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ongoing, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        return new OnGoingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.container_ongoing, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnGoingViewHolder holder, int position) {
        HashMap<String, Object> hashMap = teacherArrayList.get(position);
        holder.student_name.setText((String) hashMap.get(AppointmentMap.STUDENT_NAME));
        holder.student_mail.setText((String) hashMap.get(AppointmentMap.STUDENT_MAIL));
        holder.statusText.setText("Rejected");
        holder.statusText.setTextColor(view.getResources().getColor(R.color.red));
    }


    @Override
    public int getItemCount() {
        return teacherArrayList.size();
    }

    static class OnGoingViewHolder extends RecyclerView.ViewHolder {

        private TextView student_name, student_mail, statusText;

        public OnGoingViewHolder(@NonNull View itemView) {
            super(itemView);

            student_name = itemView.findViewById(R.id.container_appointment_name);
            student_mail = itemView.findViewById(R.id.container_appointment_mail);
            statusText = itemView.findViewById(R.id.container_appointment_STATUS);
        }

    }
}
