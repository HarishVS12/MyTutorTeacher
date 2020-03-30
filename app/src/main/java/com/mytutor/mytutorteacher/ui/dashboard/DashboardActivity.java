package com.mytutor.mytutorteacher.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.mytutor.mytutorteacher.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.menu).setOnClickListener(this);

        TabLayout tableLayout = findViewById(R.id.dashboard_tabLayout);
        ViewPager viewPager = findViewById(R.id.dashboard_viewpager);
        DashboardPagerAdapter dashboardPagerAdapter = new DashboardPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(dashboardPagerAdapter);
        tableLayout.setupWithViewPager(viewPager);
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
        } else
            return false;
    }
}
