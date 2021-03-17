package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.beta.FBref.refUsersP;

public class PDetails extends AppCompatActivity {

    String PName="", PAdd="", PDes="", Puid="", PMAil="", PPAss="";
    ArrayList<String> kidsBday;
    EditText etPName,etPAdd,etPDes;
    private FirebaseAuth mPDAuth;
    Boolean  PDisUID=false;

    TextView tvPbday;
    ListView lv;
    ArrayAdapter<String> adapter;

    //public static FirebaseDatabase PDFBDB = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_details);

         /*
        give each UI variable a value
         */
        etPName=(EditText)findViewById(R.id.etPName);
        etPAdd=(EditText)findViewById(R.id.etPAdd);
        etPDes=(EditText)findViewById(R.id.etPDes);
        lv=(ListView)findViewById(R.id.lv);
        mPDAuth = FirebaseAuth.getInstance();
        kidsBday = new ArrayList<String>();
         adapter =new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, kidsBday);
        lv.setAdapter(adapter);
    }

    public void AddKNum(View view) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage(" ");

        DatePicker picker = new DatePicker(this);
        picker.setCalendarViewShown(false);

        adb.setTitle("Choose your kid's birth day");
        adb.setView(picker);
        adb.setView(picker);

        adb.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strDate = ""+picker.getDayOfMonth()+"/"+(picker.getMonth()+1)+"/"+picker.getYear();
                int currentYear= Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth= Calendar.getInstance().get(Calendar.MONTH);
                int currentDay= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int currentDate= Calendar.getInstance().get(Calendar.DATE);

               //problem with if- how can i know the date the user chose hasnt been yet, like- 2.10.2022
                //how do i make the date the original- instead of 2.2.2021-- 2.3.2021
                if(!strDate.equals(String.valueOf(currentDate))&&((picker.getDayOfMonth()<currentDay)&&(picker.getMonth()<currentMonth)&&(picker.getYear()<currentYear))){
                    kidsBday.add(strDate);
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(PDetails.this, "Date Picked is Invalid",Toast.LENGTH_LONG).show();
                }

            }
        });

        adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog ad=adb.create();
        ad.show();
    }

    private void updatePDUI(FirebaseUser currentUser){
    }

    /**
     * when register button is pressed, check if both fields are correct.
     * make a toast accordingly.
     * @param view
     */


    public void connectP(View view) {
        PName=etPName.getText().toString();
        PAdd=etPAdd.getText().toString();
        PDes=etPDes.getText().toString();

        FirebaseUser user = mPDAuth.getCurrentUser();
        Puid = user.getUid();

       // PPAss=user.get
        updatePDUI(user);

        if (PName.isEmpty()||PAdd.isEmpty()||PDes.isEmpty()||kidsBday.isEmpty()){
            Toast.makeText(this, "please fill all the necessary details", Toast.LENGTH_SHORT).show();
        }
        else {
            refUsersP.child(Puid).child("pname").removeValue();
            refUsersP.child(Puid).child("pname").setValue(PName);

            refUsersP.child(Puid).child("paddress").removeValue();
            refUsersP.child(Puid).child("paddress").setValue(PAdd);

            refUsersP.child(Puid).child("pdesc").removeValue();
            refUsersP.child(Puid).child("pdesc").setValue(PDes);

            if (kidsBday != null){
                refUsersP.child(Puid).child("kidsBday").removeValue();
                refUsersP.child(Puid).child("kidsBday").setValue(kidsBday);
            }

            if(etPDes.length()>100)
                etPDes.setError("Description Is Too Long");
            else{

            Intent si = new Intent(PDetails.this, PPersonal.class);
            startActivity(si);
            finish();}
        }
    }


}
