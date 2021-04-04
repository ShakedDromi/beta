package com.example.beta;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static com.example.beta.FBref.refJobOffer;
import static com.example.beta.FBref.refUsersB;

public class JobOffer extends AppCompatActivity {
    String date = "", time = "", description = "", note = "", uidJP = "", uidJB = "";
    int payment = 0;
    TextView tvDate, tvTime, tvDescription, tvBPayment, tvBNote;
    Button btnsetDate, btnsetTime, btnsetDes, btnsetPayment, btnsetNotes;
    DatePickerDialog.OnDateSetListener mJDateSetListener;
    private FirebaseAuth mPDAuth;
    boolean userSitter = false;
    boolean userParent = true;




    OfferJob JO = new OfferJob(date, time, description, note, uidJP, uidJB, payment);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_offer);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvBPayment = (TextView) findViewById(R.id.tvBPayment);
        tvBNote = (TextView) findViewById(R.id.tvBNote);
        btnsetDate = (Button) findViewById(R.id.btnsetDate);
        btnsetTime = (Button) findViewById(R.id.btnsetTime);
        btnsetDes = (Button) findViewById(R.id.btnsetDes);
        btnsetPayment = (Button) findViewById(R.id.btnsetPayment);
        btnsetNotes = (Button) findViewById(R.id.btnsetNotes);


        mPDAuth = FirebaseAuth.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        refUsersB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserB userbcheck = dataSnapshot.getValue(UserB.class);
                    if (userbcheck.getUid().equals(currentFirebaseUser.getUid())) {
                        //   Toast.makeText(PRegister.this, "User With Email Alreasy Exist!", Toast.LENGTH_SHORT).show();
                        uidJB = currentFirebaseUser.getUid();
                        userSitter = true;
                        userParent=false;
                        Intent gi=getIntent();
                        String uidpr = gi.getStringExtra("");
                    } else {
                        uidJP = currentFirebaseUser.getUid();
                        userParent = true;
                        userSitter=false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


        if (userParent) {
            btnsetDate.setVisibility(View.VISIBLE);
            btnsetTime.setVisibility(View.VISIBLE);
            btnsetDes.setVisibility(View.VISIBLE);
            tvBPayment.setVisibility(View.INVISIBLE);
            tvBNote.setVisibility(View.INVISIBLE);
            btnsetPayment.setVisibility(View.INVISIBLE);
            btnsetNotes.setVisibility(View.INVISIBLE);
        } else {
            //tvDate.setText(refJobOffer.child(uidJP).child("date").toString());

            btnsetDate.setVisibility(View.INVISIBLE);
            btnsetTime.setVisibility(View.INVISIBLE);
            btnsetDes.setVisibility(View.INVISIBLE);
            tvBPayment.setVisibility(View.VISIBLE);
            tvBNote.setVisibility(View.VISIBLE);
            btnsetPayment.setVisibility(View.VISIBLE);
            btnsetNotes.setVisibility(View.VISIBLE);


        }
    }

    public void submit(View view) {
        description=tvDescription.getText().toString();
        if (userParent) {
            if (tvDate.getText().toString().equals("") || tvTime.getText().toString().equals("") || tvDescription.getText().toString().equals("")) {
                Toast.makeText(JobOffer.this, "All Fields Must Be Filled", Toast.LENGTH_SHORT).show();
            } else {
                OfferJob JO = new OfferJob(date, time, description, "", uidJP, "", 0);
                refJobOffer.child(uidJP).setValue(JO);
            }
        } else {
            if (tvBPayment.getText().toString().equals("") || tvBNote.getText().toString().equals("")) {
                Toast.makeText(JobOffer.this, "All Fields Must Be Filled", Toast.LENGTH_SHORT).show();
            } else {
                OfferJob JO = new OfferJob("", "", "", note, "", uidJB, payment);
                refJobOffer.child(uidJB).setValue(JO);
            }


        /*description=tvDescription.getText().toString();
        //note=etBNote.getText().toString();
        OfferJob JO = new OfferJob(date, time, description, note, uidJP, uidJB, payment);
        refJobOffer.child(uidJP).setValue(JO);
*/
        }
    }

    public void setDate (View view){
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
        adb.setMessage(" ");

        DatePicker picker = new DatePicker(this);
        picker.setCalendarViewShown(false);

        adb.setTitle("Choose Date");
        adb.setView(picker);
        adb.setView(picker);

        adb.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                date = "" + picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear();
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int currentDate = Calendar.getInstance().get(Calendar.DATE);

                if (((currentYear - picker.getYear()) > 0))
                    Toast.makeText(JobOffer.this, "Date Picked is Invalid", Toast.LENGTH_LONG).show();
                else {
                    if (((currentYear - picker.getYear()) == 0) && (currentMonth - picker.getMonth() > 0))
                        Toast.makeText(JobOffer.this, "Date Picked is Invalid", Toast.LENGTH_LONG).show();
                    else {
                        if (((currentYear - picker.getYear()) == 0) && (currentMonth - picker.getMonth() == 0) && (currentDay - picker.getDayOfMonth() >= 0))
                            Toast.makeText(JobOffer.this, "Date Picked is Invalid", Toast.LENGTH_LONG).show();
                        else {
                            tvDate.setText(date);
                            //adp.notifyDataSetChanged();
                            //refJobOffer.child(uidJP).child("date").setValue(tvDate);

                            //refJobOffer.child(uidJP).child("date").setValue(date);
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

        android.app.AlertDialog ad = adb.create();
        ad.show();
    }

    public void setTime (View view){
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
        adb.setMessage(" ");

        TimePicker picker = new TimePicker(this);
        picker.setEnabled(false);
        //picker.setCalendarViewShown(false);

        adb.setTitle("Choose Time");
        adb.setView(picker);
        adb.setView(picker);

        adb.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                time = "" + picker.getHour() + ":" + (picker.getMinute());
                tvTime.setText(time);
                //adp.notifyDataSetChanged();
                //refJobOffer.child(uidJP).child("date").setValue(tvDate);

                //refJobOffer.child(uidJP).child("date").setValue(date);
            }
        });

        adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        android.app.AlertDialog ad = adb.create();
        ad.show();
    }

    public void setDescription (View view){
        AlertDialog.Builder adDes;
        adDes = new AlertDialog.Builder(this);


        adDes.setTitle("Wright Description");
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et.setSingleLine(false);

        adDes.setView(et);
        adDes.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ((et.length() < 100) && (et.length() > 20)) {
                    tvDescription.setText(et.getText());
                } else
                    Toast.makeText(JobOffer.this, "description must be between 20-100 chars", Toast.LENGTH_LONG).show();
                //    Toast.makeText(this, "description must be under 100 char", Toast.LENGTH_SHORT).show();
            }
        });


        adDes.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog adk = adDes.create();
        adk.show();
    }

    public void setPayment (View view){
        AlertDialog.Builder adPayment;
        adPayment = new AlertDialog.Builder(this);


        adPayment.setTitle("Money Per Hour");
        final EditText et = new EditText(this);

        adPayment.setView(et);
        adPayment.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (et.getText().toString().matches("[0-9]*")) {
                    tvBPayment.setText(et.getText());
                } else
                    Toast.makeText(JobOffer.this, "Payment Must Contain Only Numbers ", Toast.LENGTH_SHORT).show();
                //    Toast.makeText(this, "description must be under 100 char", Toast.LENGTH_SHORT).show();
            }
        });

        adPayment.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog ad=adPayment.create();

        //    android.app.AlertDialog ad = adPayment.create();
        ad.show();
    }

    public void setNotes (View view){
        AlertDialog.Builder adNotes;
        adNotes = new AlertDialog.Builder(this);


        adNotes.setTitle("Notes");
        final EditText et = new EditText(this);

        adNotes.setView(et);
        adNotes.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ((et.length() < 50) && (et.length() > 5)) {
                    tvDescription.setText(et.getText());
                } else
                    Toast.makeText(JobOffer.this, "description must be between 5-50 chars", Toast.LENGTH_LONG).show();
            }
        });


        adNotes.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog adk = adNotes.create();
        adk.show();
    }
}