package com.example.hp.lsquare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class profileActivity extends AppCompatActivity {
    ImageView imageView;
    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<Bitmap> arrayList=new ArrayList<Bitmap>();
    ArrayList<String> info=new ArrayList<String>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri=data.getData();
        if( resultCode==RESULT_OK && data!=null){

            Bitmap bitmap= null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);


                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] bytes=stream.toByteArray();
                ParseFile file=new ParseFile("profileimage.png",bytes);
                if(requestCode==1){
                    ParseUser.getCurrentUser().put("profileimage",file);
                    imageView.setImageBitmap(bitmap);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(profileActivity.this, "Profile picture Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else if(requestCode==2){
                    ParseObject object=new ParseObject("Pictures");
                    object.put("uploadedimg",file);
                    object.put("username",ParseUser.getCurrentUser().getUsername());
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(profileActivity.this, "Photo uploaded successfully", Toast.LENGTH_SHORT).show();

                        }
                    });

                }




            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void newpic(View view){
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
    public void uploadimg(View view){
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageView=(ImageView)findViewById(R.id.profilepic);
        TextView textView=(TextView) findViewById(R.id.handle);
        textView.setText(ParseUser.getCurrentUser().getUsername());

        final ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Pictures");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        arrayList.clear();
         info.clear();
        listView=(ListView)findViewById(R.id.listView);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0) {

                    for (final ParseObject object : objects) {
                        ParseFile file = (ParseFile) object.get("uploadedimg");

                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {


                                Log.i("msg", "inside");
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                arrayList.add(bitmap);
                                info.add(ParseUser.getCurrentUser().getUsername());
                                customAdapter = new CustomAdapter(profileActivity.this, arrayList, info);
                                listView.setAdapter(customAdapter);

                            }
                        });

                    }


                    ParseFile file=(ParseFile)ParseUser.getCurrentUser().get("profileimage");
                    if(file!=null) {
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    imageView.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                }
            }
        });



    }
}
