package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.beta.FBref.refJobOffer;
import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class about extends AppCompatActivity {

    private FirebaseAuth mPRAuth;
    String id;
    boolean p=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }


    @Override
    protected void onStart() {
        super.onStart();

        mPRAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mPRAuth.getCurrentUser();
        if(user != null)
            id = user.getUid();

        Query qu = refUsersP.orderByChild(id);
        qu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                for (DataSnapshot data : dS.getChildren()) {
                   UserP up=data.getValue(UserP.class);
                   if(up.getpuid().equals(id))
                       p=true;
                   else
                       p=false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }






    public boolean onCreateOptionsMenu(Menu menu){
        if(p){
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        else{
            getMenuInflater().inflate(R.menu.mainb, menu);
            return true;
        }

    }

    /**
     * this function gets the user's choice from the menu and sends him to the appropriate activity
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected (MenuItem item){
        if (p){
            String st = item.getTitle().toString();
            if (st.equals("about")) {
                Intent si = new Intent(about.this, about.class);
                startActivity(si);
            }
            if (st.equals("PPersonal")) {
                Intent si = new Intent(about.this, PPersonal.class);
                startActivity(si);
            }
            if (st.equals("PfirstAct")) {
                Intent si = new Intent(about.this, PfirstAct.class);
                startActivity(si);
            }
            return true;
        }
        else{
            String st = item.getTitle().toString();
            if (st.equals("about")) {
                Intent si = new Intent(about.this, about.class);
                startActivity(si);
            }
            if (st.equals("BPersonal")) {
                Intent si = new Intent(about.this, BPersonal.class);
                startActivity(si);
            }
            if (st.equals("BfirstAct")) {
                Intent si = new Intent(about.this, BfirstAct.class);
                startActivity(si);
            }

            return true;
        }
    }
}