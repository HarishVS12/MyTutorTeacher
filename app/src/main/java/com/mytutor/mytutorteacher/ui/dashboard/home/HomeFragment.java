package com.mytutor.mytutorteacher.ui.dashboard.home;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mytutor.mytutorteacher.R;
import com.mytutor.mytutorteacher.adapter.recyclerview.TeacherListAdapter;
import com.mytutor.mytutorteacher.ui.utils.AppointmentMap;
import com.mytutor.mytutorteacher.ui.utils.Collection;
import com.mytutor.mytutorteacher.ui.utils.TeacherMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        final HashMap<String, Object> map = teacherArrayList.get(position);

        Map<String, Object> appointment = new HashMap<>();

        appointment.put(AppointmentMap.PREFFERED_TIME, map.get(TeacherMap.PREFFERED_TIME));
        appointment.put(AppointmentMap.COST_PER_SESSION, map.get(TeacherMap.COST_PER_SESSION));
        appointment.put(AppointmentMap.SPECIALISED_IN, map.get(TeacherMap.SPECIALISED_IN));
        appointment.put(AppointmentMap.STUDENT_ID, auth.getUid());
        appointment.put(AppointmentMap.TEACHER_ID, map.get(TeacherMap.UUID));
        appointment.put(AppointmentMap.RATING, map.get(TeacherMap.RATING));
        appointment.put(AppointmentMap.TEACHER_NAME, map.get(TeacherMap.NAME));
        appointment.put(AppointmentMap.STATUS_CODE, 0);
        firebaseFirestore.collection(Collection.APPOINTMENTS).document(auth.getUid() + map.get(TeacherMap.UUID)).set(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                firebaseFirestore.collection(Collection.TEACHER).whereEqualTo(TeacherMap.UUID, map.get(TeacherMap.UUID)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                queryDocumentSnapshot.getReference().update(TeacherMap.IS_APPOINTMENT, true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            onResume();
                                        }
                                    }
                                });


                            }
                        }
                    }
                });
            }

        });
    }


}
