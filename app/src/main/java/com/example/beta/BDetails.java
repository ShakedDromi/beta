package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.beta.FBref.refPlaces;
import static com.example.beta.FBref.refUsersB;

public class BDetails extends AppCompatActivity {
    String BName="", BAdd="", BDes="", Buid="", date="";
    EditText etBName,etBDes;
    private FirebaseAuth mBDAuth;
    Boolean  BDisUID=false;
    TextView tvBDate;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    //public static FirebaseDatabase BDFBDB = FirebaseDatabase.getInstance();
    //public static DatabaseReference refUserB= BDFBDB.getReference("UserB");

    Spinner spPlacesB;
    List<String> placesB = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_details);

         /*
        give each UI variable a value
         */
        etBName=(EditText)findViewById(R.id.etBName);
        spPlacesB =(Spinner) findViewById(R.id.spPlacesB);
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

        /**
         * this function uploads the information from the firebase tree - Places - to a spinner
         * using the reference - refPlaces and a Value event listener.
         */
        refPlaces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                placesB.clear();

                for (DataSnapshot data : ds.getChildren()){
                    String info=data.getValue().toString();
                    placesB.add(info);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BDetails.this, android.R.layout.simple_spinner_item, placesB);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPlacesB.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BDetails.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
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
        BAdd= spPlacesB.getSelectedItem().toString();
        BDes=etBDes.getText().toString();

        FirebaseUser user = mBDAuth.getCurrentUser();
        Buid = user.getUid();

        updatePDUI(user);

        if (BName.isEmpty()||BAdd.equals("Choose Neighborhood")||BDes.isEmpty()||date.isEmpty()){
            Toast.makeText(this, "please fill all the necessary details", Toast.LENGTH_SHORT).show();
        }
        else {
            refUsersB.child(Buid).child("name").setValue(BName);
            refUsersB.child(Buid).child("address").setValue(BAdd);
            refUsersB.child(Buid).child("birthDate").setValue(date);
            refUsersB.child(Buid).child("description").setValue(BDes);
            // Sign in success, update UI with the signed-in user's information
            //   Log.d(TAG, "createUserWithEmail:success");
            if((etBDes.length()>=100)||(etBDes.length()<20))
                etBDes.setError("Description must be between 20-100 chars");
            else{
            Intent si = new Intent(BDetails.this, BfirstAct.class);

            startActivity(si);
            finish();
            }
        }

    }
}