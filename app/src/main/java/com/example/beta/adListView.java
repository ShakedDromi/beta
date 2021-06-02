package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.beta.FBref.refJobOffer;

public class adListView extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvp;
    private FirebaseAuth mPRAuth;

    ArrayList<String> bname = new ArrayList<>();
    ArrayList<Integer> bprice = new ArrayList<>();
    ArrayList<Integer> bage = new ArrayList<>();
    //Class<? extends ArrayList> p=new ArrayList<>();
    String idP;
    adpBoff bof;
    ArrayList<OfferJob> offers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_list_view);

        lvp = (ListView) findViewById(R.id.lvp);
        lvp.setOnItemClickListener(this);
        lvp.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    protected void onStart() {
        super.onStart();

        mPRAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mPRAuth.getCurrentUser();
        if (user != null)
            idP = user.getUid();

        Query qi = refJobOffer.orderByChild(idP);
        qi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                for (DataSnapshot data : dS.getChildren()) {
                    //OfferJob oo = data.getValue(OfferJob.class);
                    String idp=data.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference refpr=refJobOffer.child(idP).child("propose");
        refpr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bprice.clear();
                bname.clear();
                bage.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    propose pr=data.getValue(propose.class);
                    bname.add(pr.getBname());
                    bage.add(pr.getBage());
                    bprice.add(pr.getbPrice());
                }
                bof = new adpBoff(adListView.this, bname, bage, bprice);
                lvp.setAdapter(bof);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



       /* Query shaked = refJobOffer.orderByChild(idP);
        shaked.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                bprice.clear();
                bname.clear();
                bage.clear();
                //offers.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    OfferJob oo=data.getValue(OfferJob.class);
                    propose pb = data.getValue(propose.class);
                    bname.add(pb.getBname());
                    bprice.add(pb.getbPrice());
                    bage.add(pb.getBage());
                    //bprice.add(oo.getProposeB().)

                }
                bof = new adpBoff(adListView.this, bname, bage, bprice);
                lvp.setAdapter(bof);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


       /* DatabaseReference ref = refJobOffer.child(idP).child("propose");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                //if (ds.exists()) {
                    date.clear();
                    p.clear();
                    time.clear();
                    for (DataSnapshot data : ds.getChildren()) {
                        propose pb = data.getValue(propose.class);

                        date.add(pb.getbUid());
                        time.add(pb.getbPrice());
                        p.add(pb.getbPrice());
                    }
                    // adp=new ArrayAdapter<String>(PPersonal.this,R.layout.lvklayout, kidBdayList);

                //}
                adpBoff bof = new adpBoff(adListView.this, date, time, p);
                lvp.setAdapter(bof);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });*/
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}