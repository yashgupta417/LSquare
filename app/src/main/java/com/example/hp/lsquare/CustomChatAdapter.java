package com.example.hp.lsquare;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.lsquare.R;
import com.parse.ParseUser;

import java.util.ArrayList;

public class CustomChatAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> message;
    ArrayList<String> sender;
    static LayoutInflater inflater;
    CustomChatAdapter(Context context,ArrayList<String> message,ArrayList<String> sender){
        this.context=context;
        this.message=message;
        this.sender=sender;
    }
    @Override
    public int getCount() {
        return sender.size();
    }

    @Override
    public Object getItem(int position) {
        return getItemId(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view1=convertView;
        if(view1==null) {
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(R.layout.messagelayout, null);
        }
        TextView messagetextview=(TextView)view1.findViewById(R.id.message);
        TextView messagetextview1=(TextView)view1.findViewById(R.id.message1);
        if(sender.get(position).equals(newsfeedActivity.user)){
            messagetextview1.animate().alpha(1);
            messagetextview1.setText(message.get(position));


        }else{

            messagetextview.setText(message.get(position));
            messagetextview.animate().alpha(1);
        }

        return  view1;
    }
}
