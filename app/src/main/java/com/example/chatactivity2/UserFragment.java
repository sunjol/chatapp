package com.example.chatactivity2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {
    private RecyclerView RecyclerView;
    private recyclerviewadapter recyclerviewadapter;
    private List<user> users;
    EditText searchuser;
    private void searchUser(String s){
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("users").orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    user usera=snapshot.getValue(user.class);
                    if(!usera.getId().equals(firebaseUser.getUid())){
                        users.add(usera);
                    }
                }
                recyclerviewadapter=new recyclerviewadapter(getContext(),users);
                RecyclerView.setAdapter(recyclerviewadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        RecyclerView=view.findViewById(R.id.recycler);
        RecyclerView.setHasFixedSize(false);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        users=new ArrayList<>();
        onRead();
        searchuser=view.findViewById(R.id.search);
        searchuser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
    private void onRead(){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                if(searchuser.getText().toString().equals("")){
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    user usered=snapshot.getValue(user.class);
                    if(!usered.getId().equals(firebaseUser.getUid())){
                        users.add(usered);
                    }
                }
                recyclerviewadapter=new recyclerviewadapter(getContext(),users);
                RecyclerView.setAdapter(recyclerviewadapter);
            }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
