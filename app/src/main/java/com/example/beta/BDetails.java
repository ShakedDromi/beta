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
        setContentView(R.layout.activity_b_details);

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
    public void connectB(View view) {
        BName=etBName.getText().toString();
        BAdd=etBAdd.getText().toString();
        BDes=etBDes.getText().toString();

        FirebaseUser user = mBDAuth.getCurrentUser();
        Buid = user.getUid();

        updatePDUI(user);

        if (BName.isEmpty()||BAdd.isEmpty()||BDes.isEmpty()){
            Toast.makeText(this, "please fill all the necessary details", Toast.LENGTH_SHORT).show();
        }
        else {
            refUsersB.child(Buid).child("name").removeValue();
            refUsersB.child(Buid).child("name").setValue(BName);

            refUsersB.child(Buid).child("address").removeValue();
            refUsersB.child(Buid).child("address").setValue(BAdd);

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