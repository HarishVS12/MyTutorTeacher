package com.mytutor.mytutorteacher.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mytutor.mytutorteacher.R;
import com.mytutor.mytutorteacher.ui.utils.AppointmentMap;
import com.mytutor.mytutorteacher.ui.utils.TeacherMap;

import java.util.ArrayList;
import java.util.HashMap;


public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.TeacherViewHolder> {
    private ArrayList<HashMap<String, Object>> teacherArrayList;
    private TeacherViewHolder.TeacherInteractionListner teacherInteractionListner;

    public TeacherListAdapter(ArrayList<HashMap<String, Object>> teacherArrayList, TeacherViewHolder.TeacherInteractionListner teacherInteractionListner) {
        this.teacherArrayList = teacherArrayList;
        this.teacherInteractionListner = teacherInteractionListner;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeacherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.container_teacher, parent, false), teacherInteractionListner);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        HashMap<String, Object> hashMap = teacherArrayList.get(position);
        holder.studentName.setText((String) hashMap.get(AppointmentMap.STUDENT_NAME));
        holder.studentMail.setText((String) hashMap.get(AppointmentMap.STUDENT_MAIL));
    }

    @Override
    public int getItemCount() {
        return teacherArrayList.size();
    }

    public static class TeacherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView studentName;
        private TextView studentMail;
        private MaterialButton joinCall;

        private TeacherInteractionListner teacherInteractionListner;

        public TeacherViewHolder(@NonNull View itemView, TeacherInteractionListner teacherInteractionListner) {
            super(itemView);
            this.teacherInteractionListner = teacherInteractionListner;

            studentName = itemView.findViewById(R.id.container_appointment_name);
            studentMail = itemView.findViewById(R.id.container_appointment_mail);
            joinCall = itemView.findViewById(R.id.container_appointment_JOIN_CALL);
            joinCall.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            teacherInteractionListner.onAppointed(getAdapterPosition());
        }

        public interface TeacherInteractionListner {
            void onAppointed(int position);
        }
    }
}
