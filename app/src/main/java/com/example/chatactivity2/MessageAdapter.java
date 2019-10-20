package com.example.chatactivity2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.myviewholder> {

    private Context context;
    //List<user> data;
    private List<Chat> chats;
    private String imgurl;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    public int k;
    FirebaseUser firebaseUser;
    public MessageAdapter(Context context, List<Chat> chats,String imgurl) {
        this.context = context;
        this.chats = chats;
        this.imgurl=imgurl;
    }

    @NonNull
    @Override
    public MessageAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        k=viewType;
        if(viewType==MSG_TYPE_RIGHT) {
            View view;
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            MessageAdapter.myviewholder myviewholder = new MessageAdapter.myviewholder(view);
            return myviewholder;
        }
        else{
            View view;
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            MessageAdapter.myviewholder myviewholder = new MessageAdapter.myviewholder(view);
            return myviewholder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.myviewholder holder, final int position) {
        Chat chat;
        chat=chats.get(position);
        holder.show_msg.setText(chat.getMessage());
        if(imgurl.equals("default")){
            holder.img.setImageResource(R.mipmap.ic_launcher);
        }
        else if(k==MSG_TYPE_LEFT){
            Glide.with(context).load(imgurl).into(holder.img);
        }

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder{
        public TextView show_msg;
        public TextView pname;
        public CircleImageView img;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            show_msg= itemView.findViewById(R.id.showmsg);
            //pname=itemView.findViewById(R.id.connum);
            img=itemView.findViewById(R.id.proffimg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
