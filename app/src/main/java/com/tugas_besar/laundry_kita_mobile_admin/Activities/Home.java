package com.tugas_besar.laundry_kita_mobile_admin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tugas_besar.laundry_kita_mobile_admin.Fragments.HelpFragment;
import com.tugas_besar.laundry_kita_mobile_admin.Fragments.HomeFragment;
import com.tugas_besar.laundry_kita_mobile_admin.Fragments.MyOrderFragment;
import com.tugas_besar.laundry_kita_mobile_admin.Fragments.ProfileFragment;
import com.tugas_besar.laundry_kita_mobile_admin.Fragments.SettingFragment;
import com.tugas_besar.laundry_kita_mobile_admin.R;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomBar bottomBar;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ini
        mAuth       = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        bottomBar = (BottomBar) findViewById(R.id.bottombar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            Fragment fragment = null;
            @Override
            public void onTabSelected(int tabId) {
                if (tabId == R.id.tab_home){
                    fragment = new HomeFragment();
                } if (tabId == R.id.tab_my_order) {
                    fragment = new MyOrderFragment();
                } else if (tabId == R.id.tab_help) {
                    fragment = new HelpFragment();
                } else if (tabId == R.id.tab_profile) {
                    fragment = new ProfileFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content1, fragment)
                        .commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        } else if (id == R.id.nav_profile) {

            getSupportActionBar().setTitle("Profile");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();

        } else if (id == R.id.nav_setting) {

            getSupportActionBar().setTitle("Settings");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingFragment()).commit();

        } else if (id == R.id.nav_sign_out) {

            FirebaseAuth.getInstance().signOut();
            Intent signinActivity = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(signinActivity);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView         = navigationView.getHeaderView(0);
        TextView navUsername    = headerView.findViewById(R.id.nav_user_name);
        TextView navUserEmail   = headerView.findViewById(R.id.nav_user_email);
        ImageView navUserPhoto  = headerView.findViewById(R.id.nav_user_photo);

        navUserEmail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        // now we will use Glie to load user image
        // first we need to import the library
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }
}