package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.beta.FBref.refUsersB;

public class BDetails extends AppCompatActivity {
    String BName="", BAdd="", BDes="", Buid="";
    EditText etBName,etBAdd,etBDes;
    private FirebaseAuth mBDAuth;
    Boolean  BDisUID=false;
    //public static FirebaseDatabase BDFBDB = FirebaseDatabase.getInstance();
    //public static DatabaseReference refUserB= BDFBDB.getReference("UserB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_details);

         /*
        give each UI variable a value
         */
        etBName=(EditText)findViewById(R.id.etBName);
        etBAdd=(EditText)findViewById(R.id.etBAdd);
        etBDes=(EditText)findViewById(R.id.etBDes);
        mBDAuth = FirebaseAuth.getInstance();

    }


    private void updatePDUI(FirebaseUser currentUser){
    }

    /**
     * when register button is pressed, check if both fields are correct.
     * make a toast accordingly.
     * @param view
     */
    public void connect(View view) {
       // BName=etBName.getText().toString();
      //  BAdd=etBAdd.getText().toString();
      //  BDes=etBDes.getText().toString();
        if (etBName.getText().toString().isEmpty()||etBAdd.getText().toString().isEmpty()||etBDes.getText().toString().isEmpty()){
            Toast.makeText(this, "please fill all the necessary details", Toast.LENGTH_SHORT).show();
        }
        else {
            refUsersB.child(Buid).child("Pname").removeValue();
            refUsersB.child(Buid).child("Pname").setValue(etBName.getText().toString());

            refUsersB.child(Buid).child("Paddress").removeValue();
            refUsersB.child(Buid).child("Paddress").setValue(etBAdd.getText().toString());

            refUsersB.child(Buid).child("Pdesc").removeValue();
            refUsersB.child(Buid).child("Pdesc").setValue(etBDes.getText().toString());
            // Sign in success, update UI with the signed-in user's information
            //   Log.d(TAG, "createUserWithEmail:success");
            Intent si = new Intent(BDetails.this, BMain.class);
            startActivity(si);
        }

    }
}