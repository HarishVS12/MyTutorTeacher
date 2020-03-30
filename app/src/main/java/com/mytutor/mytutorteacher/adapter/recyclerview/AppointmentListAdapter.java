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
import com.mytutor.mytutorstudent.ui.utils.AppointmentMap;
import com.mytutor.mytutorteacher.ui.utils.TeacherMap;

import java.util.ArrayList;
import java.util.HashMap;

/*
@Author cr7
@CreatedOn 3/29/2020
*/public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.AppointmentViewHolder> {
    private ArrayList<HashMap<String, Object>> appointmentList;
    private AppointmentInteractionListener appointmentInteractionListener;


    public AppointmentListAdapter(ArrayList<HashMap<String, Object>> appointmentList, AppointmentInteractionListener appointmentInteractionListener) {
        this.appointmentList = appointmentList;
        this.appointmentInteractionListener = appointmentInteractionListener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppointmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.container_appointments, parent, false), appointmentInteractionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, final int position) {
        HashMap<String, Object> hashMap = appointmentList.get(position);
        holder.studentName.setText((String) hashMap.get(AppointmentMap.STUDENT_NAME));
        holder.studentMail.setText((String) hashMap.get(AppointmentMap.STUDENT_MAIL));
        holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointmentInteractionListener.onAppointmentAccepted(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView studentName;
        private MaterialButton AcceptButton, RejectButton;
        private TextView studentMail;
        private AppointmentInteractionListener appointmentInteractionListener;

        public AppointmentViewHolder(@NonNull View itemView, AppointmentInteractionListener appointmentInteractionListener) {
            super(itemView);
            this.appointmentInteractionListener = appointmentInteractionListener;
            studentName = itemView.findViewById(R.id.container_appointment_name);
            AcceptButton = itemView.findViewById(R.id.container_appointment_accept_appointment);
            RejectButton = itemView.findViewById(R.id.container_appointment_reject_appointment);
            studentMail = itemView.findViewById(R.id.container_appointment_mail);
            AcceptButton.setOnClickListener(this);
            RejectButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            appointmentInteractionListener.onAppointmentCancelled(getAdapterPosition());
//            appointmentInteractionListener.onAppointmentAccepted(getAdapterPosition());

        }
    }

    public interface AppointmentInteractionListener {
        void onAppointmentCancelled(int position);
        void onAppointmentAccepted(int position);
    }

}
