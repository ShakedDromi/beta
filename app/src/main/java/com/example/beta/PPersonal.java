package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.beta.FBref.refUsersP;

public class PPersonal extends AppCompatActivity {

    TextView tvTitll, tvPMail, tvPAdd, tvPDes;
    UserP UserP;
    String uidP;
    ListView lvK;
    ArrayList<String> kidBdayList = new ArrayList<String>();
    ArrayAdapter<String> adp;
  //  Boolean newuser;
    private FirebaseAuth mPRAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_personal);
        tvTitll = (TextView) findViewById(R.id.tvTitll);
        tvPMail = (TextView) findViewById(R.id.tvParentsMail);
        tvPAdd = (TextView) findViewById(R.id.tvPAdd);
        tvPDes = (TextView) findViewById(R.id.tvPDes);
        lvK = (ListView) findViewById(R.id.lvK);

        /*(Intent gi=getIntent();
        newuser=gi.getBooleanExtra("newuser",false);
        refUsersP.addListenerForSingleValueEvent(VELUpdateSNum);*/

        adp=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,kidBdayList);
        lvK.setAdapter(adp);
    }

    private void updatePDUI(FirebaseUser currentUser){
    }

    @Override
    public void onStart() {
        super.onStart();
        mPRAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mPRAuth.getCurrentUser();
        if(user != null)
            uidP = user.getUid();

        DatabaseReference refKidsBday = refUsersP.child(uidP).child("kidsBday");

        refKidsBday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                if (ds.exists()) {
                    kidBdayList.clear();
                    for (DataSnapshot data : ds.getChildren()) {
                        String tmp = data.getValue(String.class);
                        kidBdayList.add(tmp);

                    }
                    // adp=new ArrayAdapter<String>(PPersonal.this,R.layout.lvklayout, kidBdayList);
                    adp = new ArrayAdapter<String>(PPersonal.this, R.layout.support_simple_spinner_dropdown_item, kidBdayList);
                    lvK.setAdapter(adp);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        Query query = refUsersP.orderByKey().equalTo(uidP).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DS) {
               // UserP userp = new UserP();
                for (DataSnapshot data: DS.getChildren()) {
                    UserP = data.getValue(UserP.class);
                    tvTitll.setText("Hi," + UserP.getpname() + "!");
                    tvPMail.setText(UserP.getpmail());
                    tvPAdd.setText(UserP.getpaddress());
                    tvPDes.setText(UserP.getpdesc());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("UserP");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    if(ds.getKey().equals(uidP))
                    {
                        UserP = ds.getValue(com.example.beta.UserP.class);
                        tvTitll.setText("Hi,"+UserP.getpname()+"!");
                        tvPMail.setText(UserP.getpmail());
                        tvPAdd.setText(UserP.getpaddress());
                        tvPDes.setText(UserP.getpdesc());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        // PPAss=user.get
        updatePDUI(user);

      //  refUsersP.child(uidP).child("pname").removeValue();


    }

    public void AddKidsNum(View view) {
    }

    public void descriptionChnge(View view) {
    }

    public void AddressChnge(View view) {
    }
}