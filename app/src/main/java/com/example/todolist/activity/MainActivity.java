package com.example.todolist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.fragment.BottomNavigationAdapter;
import com.example.todolist.model.Account;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationAdapter adapter;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnAddHome:
                Intent t = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(t);
                break;
            case R.id.mToDo:
                viewPager.setCurrentItem(0);
                break;
            case R.id.mExit:
                finish();
                System.exit(0);
                break;
            case R.id.mSignout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent t = new Intent(getApplicationContext(), SplashActivity.class);
                                t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(t);
                                finish();
                            }
                        });

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Intent t = getIntent();
        Account acc = (Account) t.getSerializableExtra("account");

        String str = (String) t.getSerializableExtra("msg");
        if(acc != null){
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
        }


        adapter = new BottomNavigationAdapter(getSupportFragmentManager(), BottomNavigationAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mToDo:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.mNews:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.mUtil:
                        viewPager.setCurrentItem(2);
                        break;
                }

                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0: bottomNavigationView.getMenu().findItem(R.id.mToDo).setChecked(true);
                        break;
                    case 1: bottomNavigationView.getMenu().findItem(R.id.mNews).setChecked(true);
                        break;
                    case 2: bottomNavigationView.getMenu().findItem(R.id.mUtil).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void init(){
        viewPager = findViewById(R.id.viewPagerHome);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }
}