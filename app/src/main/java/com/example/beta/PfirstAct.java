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

        /*       Query quer = refJobOffer.orderByKey();
        quer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DS) {
                // UserP userp = new UserP();
                date.clear();
                time.clear();

                bname.clear();
                bprice.clear();
                bage.clear();
                for (DataSnapshot data: DS.getChildren()) {
                    OfferJob jobOff = data.getValue(OfferJob.class);
                    if(jobOff.getUidJP().equals(idP)){
                        time.add(jobOff.getTime());
                        date.add(jobOff.getDate());
                        uidp=jobOff.getUidJP();


                        Query querr = refUsersP.orderByChild("puid").equalTo(uidp);
                        querr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot DS) {
                                for (DataSnapshot data: DS.getChildren()) {
                                    UserP puser = data.getValue(UserP.class);
                                    //uidp=jobOff.getUidJP();
                                    usersP.add(puser);

                                    ofer.add(jobOff);


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Query q=refJobOffer.child("propose").startAt(0);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot DS) {
                                for (DataSnapshot data : DS.getChildren()) {
                                    propose p=data.getValue(propose.class);
                                    bname.add("a");
                                    bage.add(5);
                                    bprice.add(p.getbPrice());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }

                //adpBoff bof = new adpBoff(PfirstAct.this, bname, bage, bprice);
                //lvpicks.setAdapter(bof);

                padapter padp = new padapter(PfirstAct.this, date, time);
                lvjobs.setAdapter(padp);

                //adpBoff adB = new adpBoff(PfirstAct.this, bname, bage, bprice);
                //lvpicks.setAdapter(adB);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

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



       /* Query querj = refJobOffer.orderByChild("uidJP").equalTo(uidp);
        querj.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    OfferJob oj=data.getValue(OfferJob.class);
                    if ((date.get(position).compareTo(oj.getDate())==0)){
                        Query querr = refJobOffer.orderByChild("propose").startAt(0);
                        querr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot DS) {
                                for (DataSnapshot data: DS.getChildren()) {
                                    propose pr = data.getValue(propose.class);
                                    idB=pr.getbUid();
                                    bprice.add(pr.getbPrice());


                                    Query querri = refUsersB.orderByChild("uid").equalTo(idB);
                                    querri.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot DS) {
                                            for (DataSnapshot data: DS.getChildren()) {
                                                UserB ub = data.getValue(UserB.class);
                                                bname.add(ub.getName());
                                                int ch = 0;
                                                int num=0;
                                                int x=1000;
                                                for (int j=0; j<ub.getBirthDate().length(); j++){
                                                    num++;
                                                }
                                                for (int i=num-4; i<ub.getBirthDate().length(); i++){
                                                    //ch=Integer.parseInt(((int) ub.getBirthDate().charAt(i)))
                                                    ch=ch+(int)ub.getBirthDate().charAt(i)*x;
                                                    x=x/10;
                                                }
                                                int age= Calendar.getInstance().get(Calendar.YEAR)-ch;
                                                bage.add(age);
                                            }
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
                    }



                }
                adpBoff adB = new adpBoff(PfirstAct.this, bname, bage, bprice);
                adbb.add(adB);
                //lvpicks.setAdapter(adB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
/*
        LayoutInflater inflater = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View customView = inflater.inflate(R.layout.activity_ad_list_view, null, false);
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);

*/
        /*builder.setTitle("Select an item")
                .setItems(R.array.your_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        Toast.makeText(PfirstAct.this,"Selected item on position \" + which",Toast.LENGTH_SHORT).show();
                    }
                });*/
      //  builder.create().show();
        //Intent si = new Intent(PfirstAct.this, parentsOffersB.class);
        //si.putExtra("infouid",usersP.get(position).getpuid());
        //si.putExtra("infodate",date.get(position));
            //si.putExtra("nameb", bname);
            //si.putExtra("ageb", bage);
            //si.putExtra("priceb", bprice);
        //startActivity(si);

        //transfer to another activity
    }
}