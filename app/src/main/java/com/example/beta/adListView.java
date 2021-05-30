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
import static com.example.beta.FBref.refUsersP;

public class adListView extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvp;
    private FirebaseAuth mPRAuth;

    ArrayList<String> date = new ArrayList<>();
    ArrayList<Integer> time = new ArrayList<>();
    ArrayList<Integer> p = new ArrayList<>();
    String idP;

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

        Query shaked = refJobOffer.orderByChild("uidJP").equalTo(idP);
        shaked.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                time.clear();
                date.clear();
                p.clear();
                //offers.clear();
                for (DataSnapshot data : dS.getChildren()) {

                    Query l = refJobOffer.orderByChild("propose").startAt(0);
                    l.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dS) {
                            for (DataSnapshot data : dS.getChildren()) {

                                propose pb = data.getValue(propose.class);

                                date.add(pb.getbUid());
                                time.add(pb.getbPrice());
                                p.add(pb.getbPrice());
                            }
                            adpBoff bof = new adpBoff(adListView.this, date, time, p);
                            lvp.setAdapter(bof);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


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