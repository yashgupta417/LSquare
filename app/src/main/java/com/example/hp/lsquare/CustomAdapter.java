package com.example.hp.lsquare;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bitmap> arrayList;
    static LayoutInflater inflater;
    ArrayList<String> info;
    CustomAdapter(Context context,ArrayList<Bitmap> arrayList,ArrayList<String> info){
        this.context=context;
        this.arrayList=arrayList;
        this.info=info;
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
        imageView.setImageBitmap(arrayList.get(position));
        textView.setText(info.get(position));
        return view;
    }
}
