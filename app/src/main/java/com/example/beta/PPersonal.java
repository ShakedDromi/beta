package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.beta.FBref.refUsersP;

public class PPersonal extends AppCompatActivity {

    TextView tvTitll, tvPMail, tvPAdd, tvPDes, tvuidP;
    UserP UserP;
    String uidP;
  //  Boolean newuser;
    private FirebaseAuth mPRAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_personal);
        tvTitll = (TextView) findViewById(R.id.tvTitll);
        tvPMail = (TextView) findViewById(R.id.tvPMail);
        tvPAdd = (TextView) findViewById(R.id.tvPAdd);
        tvPDes = (TextView) findViewById(R.id.tvPDes);

        /*(Intent gi=getIntent();
        newuser=gi.getBooleanExtra("newuser",false);
        refUsersP.addListenerForSingleValueEvent(VELUpdateSNum);*/
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
                   //     tvPMail.setText(""+UserP.getPmail());
                        tvPMail.setText(UserP.getpmail());
                        tvPAdd.setText(UserP.getpaddress());
                        tvPDes.setText(UserP.getpdesc());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // PPAss=user.get
        updatePDUI(user);

      //  refUsersP.child(uidP).child("pname").removeValue();


    }

}