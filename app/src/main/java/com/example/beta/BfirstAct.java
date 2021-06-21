package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class BfirstAct extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv2;
    ArrayList<OfferJob> offerss = new ArrayList<>();

    private FirebaseAuth mBRAuth;
    String uidB;

    int x=0;
    boolean b=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfirst);

        /**
         * give each UI variable a value
         */
        lv2 = (ListView) findViewById(R.id.lV2);
        lv2.setOnItemClickListener(this);
        lv2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * when activity opens, present to the user the list of her upcoming jobs.
     */
    @Override
    public void onStart() {
        super.onStart();
        mBRAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mBRAuth.getCurrentUser();
        if (user != null)
            uidB = user.getUid();

        Query query = refJobOffer.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                offerss.clear();
                for (DataSnapshot dataa: dS.getChildren()) {
                    OfferJob jobOff = dataa.getValue(OfferJob.class);

                    Query qy=refJobOffer.child(dataa.getKey()).child("propose").child(uidB);
                    qy.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data: snapshot.getChildren()){
                                propose pp=data.getValue(propose.class);
                                if ((pp.getPicked()==true)){
                                    offerss.add(jobOff);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                sitteradp sadp = new sitteradp(BfirstAct.this, offerss);
                lv2.setAdapter(sadp);
                sadp.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void search(View view) {
        Intent si = new Intent(BfirstAct.this, BMain.class);
        startActivity(si);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Query refpro=refJobOffer.child(newId).child("propose");
//maybe instead of on click- i will show her the mail and number of kids in a row in the list view
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainb, menu);
        return true;
    }

    /**
     * this function gets the user's choice from the menu and sends him to the appropriate activity
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected (MenuItem item){
        String st = item.getTitle().toString();
        if (st.equals("about")) {
            Intent si = new Intent(BfirstAct.this, about.class);
            startActivity(si);
        }
        if (st.equals("BPersonal")) {
            Intent si = new Intent(BfirstAct.this, BPersonal.class);
            startActivity(si);
        }
        if (st.equals("BfirstAct")) {
            Intent si = new Intent(BfirstAct.this, BfirstAct.class);
            startActivity(si);
        }

        return true;
    }
}