package com.example.chatactivity2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText name,pass;
    Button log;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        name=findViewById(R.id.namelog);
        pass=findViewById(R.id.passlog);
        log=findViewById(R.id.buttonlog);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=name.getText().toString();
                String password=pass.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(login.this,chatdb.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                             startActivity(intent);
                             finish();

                        }
                        else{
                            Toast.makeText(login.this,"Check all fields",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
