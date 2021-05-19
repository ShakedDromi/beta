package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.beta.FBref.refJobOffer;
import static com.example.beta.FBref.refUsersP;

public class PfirstAct extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvjobs;
    UserP usP;
    String idP;
    private FirebaseAuth mPRAuth;

    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pfirst);

        lvjobs = (ListView) findViewById(R.id.lvjobs);
        lvjobs.setOnItemClickListener(this);
        lvjobs.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void updatePDUI(FirebaseUser currentUser) {

    }


    @Override
    protected void onStart() {
        super.onStart();

        mPRAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mPRAuth.getCurrentUser();
        if(user != null)
            idP = user.getUid();


        Query quer = refJobOffer.orderByKey();
        quer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DS) {
                // UserP userp = new UserP();
                date.clear();
                time.clear();
                for (DataSnapshot data: DS.getChildren()) {
                    OfferJob jobOff = data.getValue(OfferJob.class);
                    if(jobOff.getUidJP().equals(idP)){
                        time.add(jobOff.getTime());
                        date.add(jobOff.getDate());

                        /*Query querr = refUsersP.orderByChild("puid").equalTo(uidp);
                        querr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot DS) {
                                for (DataSnapshot data: DS.getChildren()) {
                                    UserP puser = data.getValue(UserP.class);
                                    //uidp=jobOff.getUidJP();
                                    usersP.add(puser);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });*/
                    }
                }
                padapter myPadp = new padapter(PfirstAct.this, date, time);
                lvjobs.setAdapter(myPadp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void JOff(View view) {
        Intent si = new Intent(PfirstAct.this, JobOffer.class);
        startActivity(si);
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        
    }
}