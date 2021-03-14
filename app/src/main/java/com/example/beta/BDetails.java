package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static com.example.beta.FBref.refUsersB;

public class BDetails extends AppCompatActivity {
    String BName="", BAdd="", BDes="", Buid="", date="";
    EditText etBName,etBAdd,etBDes;
    private FirebaseAuth mBDAuth;
    Boolean  BDisUID=false;
    TextView tvBDate;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    //public static FirebaseDatabase BDFBDB = FirebaseDatabase.getInstance();
    //public static DatabaseReference refUserB= BDFBDB.getReference("UserB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_details);

         /*
        give each UI variable a value
         */
        etBName=(EditText)findViewById(R.id.etBName);
        etBAdd=(EditText)findViewById(R.id.etBAdd);
        etBDes=(EditText)findViewById(R.id.etBDes);
        tvBDate=(TextView)findViewById(R.id.tvBDate);
        mBDAuth = FirebaseAuth.getInstance();


        /**
        * date picker - in order to choose the user's birth day (to know his age).
        */
        tvBDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                year=year-13;
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BDetails.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        /**
         * after the dialog is opened, this function gets the date the user chose.
         * if the user is under the minimal age to use this app (under 13), the function sets a toast to tell him that
         */
        mDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                date = dayOfMonth +"/" + month +"/" +year;
                int currentYear=Calendar.getInstance().get(Calendar.YEAR);
                if ((currentYear-year)>=13)
                    tvBDate.setText(date);
                else
                    Toast.makeText(BDetails.this, "The Minimal Age For Using This App Is 13", Toast.LENGTH_SHORT).show();
            }
        };
    }




    private void updatePDUI(FirebaseUser currentUser){
    }

    /**
     * when register button is pressed, check if both fields are correct.
     * make a toast accordingly.
     * @param view
     */
    public void connectB(View view) {
        BName=etBName.getText().toString();
        BAdd=etBAdd.getText().toString();
        BDes=etBDes.getText().toString();

        FirebaseUser user = mBDAuth.getCurrentUser();
        Buid = user.getUid();

        updatePDUI(user);

        if (BName.isEmpty()||BAdd.isEmpty()||BDes.isEmpty()||date.isEmpty()){
            Toast.makeText(this, "please fill all the necessary details", Toast.LENGTH_SHORT).show();
        }
        else {
            refUsersB.child(Buid).child("name").removeValue();
            refUsersB.child(Buid).child("name").setValue(BName);

            refUsersB.child(Buid).child("address").removeValue();
            refUsersB.child(Buid).child("address").setValue(BAdd);

            refUsersB.child(Buid).child("birthDate").removeValue();
            refUsersB.child(Buid).child("birthDate").setValue(date);

            refUsersB.child(Buid).child("description").removeValue();
            refUsersB.child(Buid).child("description").setValue(BDes);
            // Sign in success, update UI with the signed-in user's information
            //   Log.d(TAG, "createUserWithEmail:success");
            if(etBDes.length()>50)
                etBDes.setError("Description Is Too Long");
            else{
            Intent si = new Intent(BDetails.this, BMain.class);
            startActivity(si);
            finish();
            }
        }

    }
}