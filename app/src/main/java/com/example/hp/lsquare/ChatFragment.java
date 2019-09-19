package com.example.hp.lsquare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;


public class ChatFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        TextView romeoTextview=(TextView)v.findViewById(R.id.romeo);
        TextView julietTextview=(TextView)v.findViewById(R.id.juliet);
        romeoTextview.setText(ParseUser.getCurrentUser().getString("Romeo"));
        julietTextview.setText(ParseUser.getCurrentUser().getString("Juliet"));
        return v;
    }



}
