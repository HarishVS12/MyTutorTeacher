package com.mytutor.mytutorteacher.ui.dashboard.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mytutor.mytutorstudent.ui.utils.AppointmentMap;
import com.mytutor.mytutorteacher.R;
import com.mytutor.mytutorteacher.adapter.recyclerview.TeacherListAdapter;
import com.mytutor.mytutorteacher.ui.classroom.VideoChatViewActivity;
import com.mytutor.mytutorteacher.ui.utils.Collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
@Author cr7
@CreatedOn 3/28/2020
*/
public class HomeFragment extends Fragment implements TeacherListAdapter.TeacherViewHolder.TeacherInteractionListner {
    public static final String FRAGMENT_TYPE = "fragment_type";
    private RecyclerView mRecyclerview;
    private TeacherListAdapter teacherListAdapter;
    private ArrayList<HashMap<String, Object>> teacherArrayList = new ArrayList<HashMap<String, Object>>();
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    public static HomeFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TYPE, type);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        teacherListAdapter = new TeacherListAdapter(teacherArrayList, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        firebaseFirestore.collection(Collection.APPOINTMENTS)
                .whereEqualTo("teacher_id",auth.getUid())
                .whereEqualTo("status_code", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!teacherArrayList.isEmpty()) {
                        teacherArrayList.clear();
                    }
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) queryDocumentSnapshot.getData();
                        teacherArrayList.add(map);
                    }
                    teacherListAdapter.notifyDataSetChanged();

                }
            }
        });



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerview = view.findViewById(R.id.home_recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview.setAdapter(teacherListAdapter);

    }

    @Override
    public void onAppointed(int position) {
        HashMap<String, Object> map = teacherArrayList.get(position);
        Intent intent = new Intent(getContext(), VideoChatViewActivity.class);
        intent.putExtra(com.mytutor.mytutorstudent.ui.utils.AppointmentMap.TEACHER_ID, (String) map.get(com.mytutor.mytutorstudent.ui.utils.AppointmentMap.TEACHER_ID));
        intent.putExtra(com.mytutor.mytutorstudent.ui.utils.AppointmentMap.COST_PER_SESSION, (String) map.get(com.mytutor.mytutorstudent.ui.utils.AppointmentMap.COST_PER_SESSION));
        intent.putExtra(com.mytutor.mytutorstudent.ui.utils.AppointmentMap.APPOINTMENT_ID, (String) map.get(AppointmentMap.APPOINTMENT_ID));
        startActivity(intent);
    }


}
