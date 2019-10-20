package com.example.chatactivity2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class recyclerviewadapter extends RecyclerView.Adapter<recyclerviewadapter.myviewholder> {

    Context context;
    List<user> data;
    String lastmessage;
    public recyclerviewadapter(Context context, List<user> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
       view= LayoutInflater.from(context).inflate(R.layout.itemcon,parent,false);
       myviewholder myviewholder= new myviewholder(view);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, final int position) {
        final user user=data.get(position);
        holder.tname.setText(user.getUsername());
        last_message(user.getId(),holder.pname);
        if(user.getImage().equals("default")){
            holder.img.setImageResource(R.mipmap.ic_launcher);
        }
        else
        {
            Glide.with(context).load(user.getImage()).into(holder.img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MessageActivity.class);
                com.example.chatactivity2.user user1=data.get(position);
                intent.putExtra("userid",user1.getId());

                //Toast.makeText(recyclerviewadapter.this, user.getId(),Toast.LENGTH_SHORT).show();

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder{
        public TextView tname;
        public TextView pname;
        public CircleImageView img;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            tname= itemView.findViewById(R.id.conname);
            pname=itemView.findViewById(R.id.connum);
            img=itemView.findViewById(R.id.imgcon);
        }
    }
    private void last_message(final String userid, final TextView last){
        lastmessage="";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat;
                    chat = snapshot.getValue(Chat.class);
                    if(chat.getReciever().equals(firebaseUser.getUid())&&chat.getSender().equals(userid)||chat.getReciever().equals(userid)&&chat.getSender().equals(firebaseUser.getUid())){
                            lastmessage=chat.getMessage();
                    }

                }
                last.setText(lastmessage);
                lastmessage="";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
