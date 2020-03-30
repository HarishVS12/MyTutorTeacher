package com.mytutor.mytutorteacher.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mytutor.mytutorstudent.ui.utils.Collection;
import com.mytutor.mytutorstudent.ui.utils.TeacherMap;
import com.mytutor.mytutorteacher.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.mytutor.mytutorteacher.adapter.viewpager.DashboardPagerAdapter;
import com.mytutor.mytutorteacher.ui.authentication.signin.LoginActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        findViewById(R.id.menu).setOnClickListener(this);

        TabLayout tableLayout = findViewById(R.id.dashboard_tabLayout);
        ViewPager viewPager = findViewById(R.id.dashboard_viewpager);

        DashboardPagerAdapter dashboardPagerAdapter = new DashboardPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(dashboardPagerAdapter);
        tableLayout.setupWithViewPager(viewPager);

        invalidateOptionsMenu();
    }


    private void onLogout() {
        auth.signOut();
        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        PopupMenu popup = new PopupMenu(DashboardActivity.this, v);

        popup.getMenuInflater()
                .inflate(R.menu.dashboard_menu,  popup.getMenu());
        popup.setOnMenuItemClickListener(this);

        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.dashboard_logout) {
            onLogout();
            return true;
        }
        else if(item.getItemId() == R.id.dashboard_wallet){
            ShowWalletCash();
            return true;
        }
        else
            return false;
    }

    private void ShowWalletCash(){
        final DocumentReference docRef = firebaseFirestore
                .collection("teacher")
                .document(auth.getUid());

            docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            Toast.makeText(DashboardActivity.this,"" + auth.getUid(), Toast.LENGTH_SHORT).show();
                            long wallet = document.getLong("Wallet Amount");
                            Toast.makeText(DashboardActivity.this, "Wallet Amount: " + wallet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
