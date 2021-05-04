package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.beta.FBref.refJobOffer;
import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class BMain extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lV2;
    UserB userB;
    String stplace="";
    int bPrice=0;
    String uidB,uidp;
    private FirebaseAuth mBRAuth, mPDAuth;
    ArrayAdapter<String> adapter;
    boolean pressed= false;

    TextView tvname, tvnum, tvage, tvdes;

    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<UserP> usersP = new ArrayList<>();
    ArrayList<propose> proposeB = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_main);

        tvage=(TextView) findViewById(R.id.tvage);
        tvdes=(TextView) findViewById(R.id.tvdes);
        tvname=(TextView) findViewById(R.id.tvname);
        tvnum=(TextView) findViewById(R.id.tvnum);

        lV2 = (ListView) findViewById(R.id.lV2);
        lV2.setOnItemClickListener(this);
        lV2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void updatePDUI(FirebaseUser currentUser){

    }

    @Override
    public void onStart() {
        super.onStart();
            mBRAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mBRAuth.getCurrentUser();
            if(user != null)
                uidB = user.getUid();

            Query query = refUsersB.orderByKey().equalTo(uidB).limitToFirst(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot DS) {
                    for (DataSnapshot data: DS.getChildren()) {
                        userB = data.getValue(UserB.class);
                        stplace=userB.getAddress();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            ValueEventListener pListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {
                date.clear();
                time.clear();
                for(DataSnapshot data : dS.getChildren()) {
                    UserP uP = data.getValue(UserP.class);
                    OfferJob jobOff = data.getValue(OfferJob.class);
                    if ((uP.getpaddress().equals(stplace))){
                        time.add(jobOff.getTime());
                        date.add(jobOff.getDate());
                        images.add(R.drawable.facebook);
                        usersP.add(uP);
                        uidp=uP.getpuid();
                    }
                }
                MyAdapter myadp = new MyAdapter(BMain.this, date, time, images);
                lV2.setAdapter(myadp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        refUsersP.addListenerForSingleValueEvent(pListener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pressed=true;
        tvname.setText(usersP.get(position).getpname());
        tvnum.setText(Integer.toString(usersP.get(position).getPknum())+"kids");
        //tvnum.setText(usersP.get(position).getPknum());
        tvage.setText(usersP.get(position).getpmail());
        tvdes.setText(usersP.get(position).getpdesc());
        uidp=usersP.get(position).getpuid();
    }

    public void offer(View view) {
        if(pressed){
            AlertDialog.Builder adoffer;
            adoffer=new AlertDialog.Builder(this);

            adoffer.setTitle("Enter how much you charge per hour");
            final EditText et=new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setSingleLine(false);

            adoffer.setView(et);
            adoffer.setPositiveButton("offer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (et.getText().toString().isEmpty())
                        Toast.makeText(BMain.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                    else {
                        bPrice=Integer.parseInt(et.getText().toString());
                        proposeB.clear();
//VEL, after onDataChange add lines
                        DatabaseReference refOfferJob = refJobOffer.child(uidp).child("propose");

                        refOfferJob.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot ds) {
                                if (ds.exists()) {
                                    for (DataSnapshot data : ds.getChildren()) {
                                        propose pr = data.getValue(propose.class);
                                        proposeB.add(pr);
                                    }
                                    // adp=new ArrayAdapter<String>(PPersonal.this,R.layout.lvklayout, kidBdayList);
                                    //adp = new ArrayAdapter<String>(PPersonal.this, R.layout.support_simple_spinner_dropdown_item, kidBdayList);
                                    //lvK.setAdapter(adp);
                                }
                                propose props = new propose(uidB,bPrice);
                                proposeB.add(props);
                                refJobOffer.child(uidp).child("propose").setValue(proposeB);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                            }
                        });
                        Intent si = new Intent(BMain.this, BfirstAct.class);
                        startActivity(si);
                        finish();
                    }
                }
            });
            adoffer.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog adk=adoffer.create();
            adk.show();
        }
        else{
            Toast.makeText(BMain.this, "an Offer Must Be Pressed first", Toast.LENGTH_LONG).show();
        }
    }
}