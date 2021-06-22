package com.example.beta;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.beta.FBref.refAuth;
import static com.example.beta.FBref.refJobOffer;
import static com.example.beta.FBref.refUsersP;

public class JobOffer extends AppCompatActivity {

    String date = "", time = "", description = "", makom, mail="";
    String uidp;
    int knum;

    OfferJob offJob;

    TextView tvDate, tvTime, tvDescription;
    ImageView btnsetDate, btnsetTime, btnsetDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_offer);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        btnsetDate = (ImageButton) findViewById(R.id.btnsetDate);
        btnsetTime = (ImageView) findViewById(R.id.btnsetTime);
        btnsetDes = (ImageButton) findViewById(R.id.btnsetDes);
    }


    /**
     * this method gets the current user's neighborhood from database.
     */
    @Override
    protected void onStart() {
        super.onStart();

        Query queri = refUsersP.orderByChild("puid").equalTo(refAuth.getCurrentUser().getUid()).limitToFirst(1);
        queri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DS) {
                for (DataSnapshot data: DS.getChildren()) {
                    UserP userP = data.getValue(UserP.class);
                    makom=userP.getpaddress();
                    mail=userP.getpmail();
                    knum=userP.getPknum();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * this method checks that all the fields are filled with data, and creats a new user id for the parent's job offer
     * @param view
     */
    public void submit(View view) {
        description=tvDescription.getText().toString();
        if (tvDate.getText().toString().equals("") || tvTime.getText().toString().equals("") || tvDescription.getText().toString().equals("")) {
            Toast.makeText(JobOffer.this, "All Fields Must Be Filled", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("JobOffer");
            final String JPid= mDatabase.push().getKey();

            FirebaseUser userOff = refAuth.getCurrentUser();
            uidp = userOff.getUid();


            offJob = new OfferJob(date, time, description, uidp, makom, JPid, mail, knum,null);
            refJobOffer.child(JPid).setValue(offJob);

            Intent si = new Intent(JobOffer.this, PfirstAct.class);
            startActivity(si);
            finish();
        }
    }


    /**
     * this method opens to the user an alert dialog of calendar.
     * @param view
     */
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

    /**
     * this method opens to the user a time picker alert dialog.
     * @param view
     */
    public void setTime (View view){
            android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
            adb.setMessage(" ");

            TimePicker picker = new TimePicker(this);
            picker.setEnabled(false);

            adb.setTitle("Choose Time");
            adb.setView(picker);
            adb.setView(picker);

            adb.setPositiveButton("set", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    time = "" + picker.getHour() + ":" + (picker.getMinute());
                    tvTime.setText(time);
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

    /**
     * this method opens to the user a multiline edit text alert dialog.
    */
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

    }
