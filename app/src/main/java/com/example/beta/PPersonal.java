package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;

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

        adp =new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, kidBdayList);
        lvK.setAdapter(adp);

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
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
        adb.setMessage(" ");

        DatePicker picker = new DatePicker(this);
        picker.setCalendarViewShown(false);

        adb.setTitle("Add your kid's birth day");
        adb.setView(picker);
        adb.setView(picker);

        adb.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strDate = "" + picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear();
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int currentDate = Calendar.getInstance().get(Calendar.DATE);

                if (((currentYear - picker.getYear()) < 0))
                    Toast.makeText(PPersonal.this, "Date Picked is Invalid", Toast.LENGTH_LONG).show();
                else {
                    if (((currentYear - picker.getYear()) == 0)  && (currentMonth- picker.getMonth()<6))
                        Toast.makeText(PPersonal.this, "Date Picked is Invalid", Toast.LENGTH_LONG).show();
                    else {
                        if ((currentYear - picker.getYear() == 1) && (12 + (currentMonth - 6) <= picker.getMonth()))
                            Toast.makeText(PPersonal.this, "Date Picked is Invalid", Toast.LENGTH_LONG).show();
                        else {
                            kidBdayList.add(strDate);
                            adp.notifyDataSetChanged();
                            refUsersP.child(uidP).child("kidsBday").setValue(kidBdayList);
                        }
                    }

                }
            }
        });

        adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        android.app.AlertDialog ad=adb.create();
        ad.show();
    }

    public void descriptionChnge(View view) {
        AlertDialog.Builder adDes;
        adDes=new AlertDialog.Builder(this);

        FirebaseUser user = mPRAuth.getCurrentUser();
        uidP = user.getUid();


        adDes.setTitle("Change Description");
        final EditText et=new EditText(this);
        et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et.setSingleLine(false);
        et.setText(tvPDes.getText());

        adDes.setView(et);
        adDes.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if((et.length()<100)&&(et.length()>20)) {
                    tvPDes.setText(et.getText());
                    refUsersP.child(uidP).child("pdesc").setValue(tvPDes.getText().toString());
                }
                else
                    Toast.makeText(PPersonal.this, "description must be between 20-100 chars", Toast.LENGTH_LONG).show();
                //    Toast.makeText(this, "description must be under 100 char", Toast.LENGTH_SHORT).show();
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

    public void AddressChnge(View view) {
        AlertDialog.Builder adAdd;
        adAdd=new AlertDialog.Builder(this);

        FirebaseUser user = mPRAuth.getCurrentUser();
        uidP = user.getUid();

        adAdd.setTitle("Change Your Address");
        final EditText et=new EditText(this);
        adAdd.setView(et);
        adAdd.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvPAdd.setText(et.getText());
                refUsersP.child(uidP).child("paddress").setValue(tvPAdd.getText().toString());
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
}