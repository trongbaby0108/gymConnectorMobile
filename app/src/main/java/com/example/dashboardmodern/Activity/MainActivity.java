package com.example.dashboardmodern.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dashboardmodern.Fragment.Admin.FragmentUserAdmin;
import com.example.dashboardmodern.Fragment.Client.FragmentCombo;
import com.example.dashboardmodern.Fragment.Client.FragmentGym;
import com.example.dashboardmodern.Fragment.Client.FragmentHome;
import com.example.dashboardmodern.Fragment.Admin.FragmentNewCombo;
import com.example.dashboardmodern.Fragment.Admin.FragmentNewGym;
import com.example.dashboardmodern.Fragment.Client.FragmentPtInfo;
import com.example.dashboardmodern.Fragment.Client.FragmentTrainer;
import com.example.dashboardmodern.Fragment.Client.FragmentUserByPt;
import com.example.dashboardmodern.Fragment.Client.FragmentUserInfo;
import com.example.dashboardmodern.R;
import com.example.lib.Model.Response.PTInfoResponse;
import com.example.lib.Model.Response.userInfoResponse;
import com.google.android.material.navigation.NavigationView;

import vn.momo.momo_partner.AppMoMoLib;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public Toolbar toolbar;
    public userInfoResponse acc;
    public PTInfoResponse pt;
    public String jwt;
    boolean isGoogle;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout  = findViewById(R.id.drawer_layout);
        navigationView  = findViewById(R.id.nav_view);
        toolbar  = findViewById(R.id.toolbar);

        fragment = new FragmentHome();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            acc = (userInfoResponse) bundle.getSerializable("data");
            pt = (PTInfoResponse) bundle.getSerializable("pt");
            jwt = bundle.getString("jwt");
            isGoogle = bundle.getBoolean("isGoogle");
        }

        if(pt != null){
            if(pt.getRole().equals("PERSONAL_TRAINER")){
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.pt_menu);
                fragment = new FragmentPtInfo(pt);
            }
        }

        if(acc != null && !isGoogle) {
            if(acc.getRole().equals("ADMIN")){
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.admin_menu);
                fragment = new FragmentUserAdmin();
            }
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.addToBackStack("Fragment home");
        fragmentTransaction.commit();
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                    this,
                    drawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
                );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

    }


    @Override
    public void onBackPressed() { //To avoid closing app on Back pressed
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                fragment = new FragmentHome();
                break;
            case R.id.Combo:
                fragment = new FragmentCombo();
                break;
            case R.id.nav_gym:
                fragment = new FragmentGym();
                break;
            case R.id.nav_trainers:
                fragment = new FragmentTrainer();
                break;
            case R.id.nav_profile:
                if(acc == null && pt != null)
                    fragment = new FragmentPtInfo(pt);
                else
                    fragment = new FragmentUserInfo(acc);
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_newCombo:
                fragment = new FragmentNewCombo();
                break;

            case R.id.nav_newGym:
                fragment = new FragmentNewGym();
                break;

            case R.id.nav_User:
                fragment = new FragmentUserAdmin();
                break;
            case R.id.nav_trainer_info:
                fragment = new FragmentPtInfo(pt);
                break;
            case R.id.nav_Users:
                fragment = new FragmentUserByPt(pt);
                break;
        }
        if(fragment != null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
            fragmentTransaction.addToBackStack("Fragment home");
            fragmentTransaction.commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    ShowMessage("Thành công");
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {

                    } else {
                        //ShowMessage("Không thành công");
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    //ShowMessage("message: " + message);
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    //ShowMessage("Không thành công");
                } else {
                    //TOKEN FAIL
                    //ShowMessage("Không thành công");
                }
            } else {
                //ShowMessage("Không thành công");
            }
        } else {
            //ShowMessage("Không thành công");
        }
    }

    public void ShowMessage(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cám ơn bạn");
        builder.setTitle(text);
        builder.setPositiveButton("OK", null);
        builder.setCancelable(true);
        builder.create().show();
    }
}