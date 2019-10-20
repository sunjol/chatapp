package com.example.chatactivity2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatdb extends AppCompatActivity {
    CircleImageView imageView;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextView textView;
    SharedPreferences sharedPreferences;
    String saved;
    user usera=new user();
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    Button logout;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatdb);
        textView=findViewById(R.id.user);
        imageView=findViewById(R.id.prof);
//        toolbar=findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        logout=findViewById(R.id.logoutbutton);
        sharedPreferences=getSharedPreferences("userid",0);
        saved=sharedPreferences.getString("id","");
       firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
       saved=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("users").child(saved);
        //Toast.makeText(chatdb.this, firebaseUser.getUid(),Toast.LENGTH_SHORT).show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usera=dataSnapshot.getValue(user.class);
                //Toast.makeText(chatdb.this, usera.getUsername(),Toast.LENGTH_SHORT).show();
                textView.setText(usera.getUsername());
                if(usera.getImage().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }
                else {
                    Glide.with(chatdb.this).load(usera.getImage()).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tabLayout=findViewById(R.id.tab);
        viewPager=findViewById(R.id.viewoager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragmet(new ChatFragment(),"Profile");
        viewPagerAdapter.addFragmet(new UserFragment(),"Users");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(chatdb.this,MainActivity.class));
                finish();
            }
        });
    }
    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragmet(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
