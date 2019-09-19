package com.example.hp.lsquare;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bitmap> arrayList;
    static LayoutInflater inflater;
    ArrayList<String> info;
    ArrayList<Integer> loves;
    ArrayList<String> comments;
    ArrayList<String> objectId;
    CustomAdapter(Context context,ArrayList<Bitmap> arrayList,ArrayList<String> info,ArrayList<Integer> loves,ArrayList<String> comments,ArrayList<String> objectId){
        this.context=context;
        this.arrayList=arrayList;
        this.info=info;
        this.loves=loves;
        this.comments=comments;
        this.objectId=objectId;
    }

    @Override
    public int getCount() {
        return info.size();
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
        View view=convertView;
        if(view==null){
            inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.timelinelayout,null);
        }
        ImageView imageView=(ImageView) view.findViewById(R.id.imageView);
        TextView textView=(TextView) view.findViewById(R.id.textView);
        TextView noofloves=(TextView)view.findViewById(R.id.nooflikes);
        TextView commentsTextView=(TextView) view.findViewById(R.id.comment);
        imageView.setImageBitmap(arrayList.get(position));
        textView.setText(info.get(position));
        noofloves.setText(Integer.toString(loves.get(position))+" loves");
        ImageView loveimg=(ImageView)view.findViewById(R.id.loveimg);
        loveimg.setTag(objectId.get(position));
        ImageView commentimg=(ImageView)view.findViewById(R.id.commentimg);
        commentimg.setTag(objectId.get(position));
 /*       ImageView sendcomment=(ImageView)view.findViewById(R.id.sendcomment);
        sendcomment.setEnabled(false);
        sendcomment.animate().alpha(0);*/
        EditText editText=(EditText)view.findViewById(R.id.back);
        editText.setTag(objectId.get(position));




        return view;
    }
}
