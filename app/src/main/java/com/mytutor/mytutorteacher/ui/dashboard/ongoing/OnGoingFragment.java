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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mytutor.mytutorteacher.R;
import com.mytutor.mytutorteacher.adapter.recyclerview.OnGoingListAdapter;
import com.mytutor.mytutorteacher.ui.utils.AppointmentMap;
import com.mytutor.mytutorteacher.ui.utils.Collection;

import java.util.ArrayList;
import java.util.HashMap;

/*
@Author cr7
@CreatedOn 3/28/2020
*/
public class OnGoingFragment extends Fragment {
    public static final String FRAGMENT_TYPE = "fragment_type";
    private RecyclerView mRecyclerview;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private ArrayList<HashMap<String,Object>> ongoingList = new ArrayList<>();

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

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        onGoingListAdapter=new OnGoingListAdapter(ongoingList);
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

    @Override
    public void onResume() {
        super.onResume();

        firebaseFirestore.collection(Collection.APPOINTMENTS)
                .whereEqualTo(AppointmentMap.STATUS_CODE, -1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!ongoingList.isEmpty()) {
                                ongoingList.clear();
                            }
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                HashMap<String, Object> map = (HashMap<String, Object>) queryDocumentSnapshot.getData();
                                ongoingList.add(map);
                            }
                            onGoingListAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

}
