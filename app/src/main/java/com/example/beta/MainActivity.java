package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.beta.FBref.refAuth;

public class MainActivity extends AppCompatActivity {

    boolean PstayConnect, BstayConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PstayConnect =false;

    }


    /**
     * on activity start - if the user exists & asked to  be remembered -transfer to the next activity (Main Screen)
     */
   /* @Override
    protected void onnStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        Boolean isChecked = settings.getBoolean("stayConnect", false);
        Intent si = new Intent(MainActivity.this, BMain.class);
        if ((refAuth.getCurrentUser() != null) && (isChecked)) {
            BstayConnect = true;
            si.putExtra("UserB", false);
            startActivity(si);
        }
    }
    */
    /**
     * on activity start - if the user exists & asked to  be remembered -transfer to the next activity (Main Screen)
     */
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        Intent si=new Intent(MainActivity.this, PRegister.class);
        if((refAuth.getCurrentUser()!=null) && (isChecked)){
            PstayConnect =true;
            si.putExtra("UserP",false);
            startActivity(si);
        }
    }

    public void sitter(View view) {
        Intent si = new Intent(MainActivity.this, BRegistr.class);
        startActivity(si);
        finish();
    }

    public void parent(View view) {
        Intent si = new Intent(MainActivity.this, PRegister.class);
        startActivity(si);
        finish();
    }


}