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
    ListView lvpicks;
    UserP usP;
    String idP, idB, uidp;
    private FirebaseAuth mPRAuth;

    ArrayList<String> boff = new ArrayList<String>();
    ArrayAdapter<String> adp;

    ArrayList<adpBoff> adbb = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> bname=new ArrayList<>();
    ArrayList<Integer> bage=new ArrayList<>();
    ArrayList<Integer> bprice=new ArrayList<>();
    ArrayList<UserP> usersP = new ArrayList<>();
    ArrayList<OfferJob> ofer = new ArrayList<>();

    ArrayList<OfferJob> offers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pfirst);

        lvjobs = (ListView) findViewById(R.id.lvjobs);
        lvjobs.setOnItemClickListener(this);
        lvjobs.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

      //  lvpicks = (ListView) findViewById(R.id.lvpicks);

        //lvpicks.setOnItemClickListener(this);
        //lvpicks.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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

        Query shaked = refJobOffer.orderByChild("uidJP").equalTo(idP);
        shaked.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                time.clear();
                date.clear();
                offers.clear();
                for (DataSnapshot data: dS.getChildren()) {
                    OfferJob jobOff = data.getValue(OfferJob.class);
                    time.add(jobOff.getTime());
                    date.add(jobOff.getDate());
                    offers.add(jobOff);
                }

                padapter padp = new padapter(PfirstAct.this, date, time);
                lvjobs.setAdapter(padp);
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


    //@SuppressLint("ResourceType")
    @SuppressLint("ResourceType")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent si = new Intent(PfirstAct.this, adListView.class);
        startActivity(si);
    }

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