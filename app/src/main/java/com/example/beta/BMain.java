package com.example.beta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class BMain extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lV2;

    //ArrayList<String> kidBdayList = new ArrayList<String>();
    //ArrayAdapter<String> adp;
    //private FirebaseAuth mPRAuth;

    String mTitle[]= {"facebook", "whatsapp", "twitter", "instagram"};
    String mDescription[]= {"facebook description","whatsapp description","twitter description","instagram description"};
    int images[]={R.drawable.facebook, R.drawable.whatsapp, R.drawable.twitter, R.drawable.instagram11};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_main);

        lV2 = (ListView) findViewById(R.id.lV2);

       /* adp =new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, kidBdayList);
        lV2.setAdapter(adp);
*/


        MyAdapter myadp = new MyAdapter(this, mTitle, mDescription, images);
        lV2.setOnItemClickListener(this);
        lV2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //MyAdapter adapter= new MyAdapter(BMain.this, mTitle, mDescription, images);
        lV2.setAdapter(myadp);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==0)
            Toast.makeText(BMain.this, "facebook description", Toast.LENGTH_SHORT).show();
        if(position==1)
            Toast.makeText(BMain.this, "whatsapp description", Toast.LENGTH_SHORT).show();
        if(position==2)
            Toast.makeText(BMain.this, "twitter description", Toast.LENGTH_SHORT).show();
        if(position==3)
            Toast.makeText(BMain.this, "instagram description", Toast.LENGTH_SHORT).show();
    }
}