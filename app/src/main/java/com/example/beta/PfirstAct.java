package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.beta.FBref.refJobOffer;
import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class PfirstAct extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvjobs;
    String idP;
    private FirebaseAuth mPRAuth;

    ArrayList<OfferJob> offers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pfirst);

        /**
         * give each UI variable a value
         */
        lvjobs = (ListView) findViewById(R.id.lvjobs);
        lvjobs.setOnItemClickListener(this);
        lvjobs.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void updatePDUI(FirebaseUser currentUser) {

    }


    /*
    this method gets the user's id from database, and fills the list view with the jobs offers the parent created
     */
    @Override
    protected void onStart() {
        super.onStart();

        mPRAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mPRAuth.getCurrentUser();
        if(user != null)
            idP = user.getUid();

        Query shaked = refJobOffer.orderByChild("uidJP").equalTo(idP);
        shaked.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                offers.clear();
                for (DataSnapshot data: dS.getChildren()) {
                    OfferJob jobOff = data.getValue(OfferJob.class);
                    offers.add(jobOff);
                }

                padapter padp = new padapter(PfirstAct.this,offers);
                lvjobs.setAdapter(padp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /*
    this method sends the user to the job offer activity
     */
    public void JOff(View view) {
        Intent si = new Intent(PfirstAct.this, JobOffer.class);
        startActivity(si);
        finish();
    }

    /*
    this method opens an activity in which there are all the babysitters proposes according to the chosen job.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent si = new Intent(PfirstAct.this, adListView.class);
        si.putExtra("uid",((padapter)lvjobs.getAdapter()).getItem(position).getNewId());
        startActivity(si);
    }


    /**
     * this method creats the menu
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent si = new Intent(PfirstAct.this, about.class);
            startActivity(si);
        }
        if (st.equals("PPersonal")) {
            Intent si = new Intent(PfirstAct.this, PPersonal.class);
            startActivity(si);
        }
        if (st.equals("PfirstAct")) {
            Intent si = new Intent(PfirstAct.this, PfirstAct.class);
            startActivity(si);
        }
        return true;
    }
}