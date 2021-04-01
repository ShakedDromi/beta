package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class BPersonal extends AppCompatActivity {

    TextView tvTitleB, tvMailB, tvAddB, tvDesB, tvDateB;
    UserB userB;
    String uidB;
    private FirebaseAuth mBRAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_personal);

        tvTitleB = (TextView) findViewById(R.id.tvTitleB);
        tvMailB = (TextView) findViewById(R.id.tvMailB);
        tvAddB = (TextView) findViewById(R.id.tvAddB);
        tvDesB = (TextView) findViewById(R.id.tvDesB);
        tvDateB = (TextView) findViewById(R.id.tvDateB);

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
                // UserP userp = new UserP();
                for (DataSnapshot data: DS.getChildren()) {
                    userB = data.getValue(UserB.class);
                    tvTitleB.setText("Hi," + userB.getName() + "!");
                    tvMailB.setText(userB.getMail());
                    tvAddB.setText(userB.getAddress());
                    tvDesB.setText(userB.getDescription());
                    tvDateB.setText(userB.getBirthDate());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updatePDUI(user);
    }

    public void AddressChngeB(View view) {
        AlertDialog.Builder adAdd;
        adAdd=new AlertDialog.Builder(this);

        FirebaseUser user = mBRAuth.getCurrentUser();
        uidB = user.getUid();

        adAdd.setTitle("Change Your Address");
        final EditText et=new EditText(this);
        adAdd.setView(et);
        adAdd.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvAddB.setText(et.getText());
                refUsersB.child(uidB).child("address").setValue(tvAddB.getText().toString());
            }
        });
        adAdd.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog adk=adAdd.create();
        adk.show();
    }

    public void descriptionChngeB(View view) {
        AlertDialog.Builder adDes;
        adDes=new AlertDialog.Builder(this);

        FirebaseUser user = mBRAuth.getCurrentUser();
        uidB = user.getUid();


        adDes.setTitle("Change Description");
        final EditText et=new EditText(this);
        et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et.setSingleLine(false);
        et.setText(tvDesB.getText());

        adDes.setView(et);
        adDes.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if((et.length()<100)&&(et.length()>20)) {
                    tvDesB.setText(et.getText());
                    refUsersB.child(uidB).child("description").setValue(tvDesB.getText().toString());
                }
                else
                    Toast.makeText(BPersonal.this, "description must be between 20-100 chars", Toast.LENGTH_LONG).show();
            }
        });



        adDes.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        adDes.setNegativeButton("clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adDes.setCancelable(false);
                et.setText("");
                //  et.toString().replace(et.toString()," ");
            }
        });

        AlertDialog adk=adDes.create();
        adk.show();
    }
}