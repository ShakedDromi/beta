package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PDetails extends AppCompatActivity {

    String PName="", PAdd="", PDes="", Puid="", PMAil="", PPAss="";
    EditText etPName,etPAdd,etPDes;
    private FirebaseAuth mPDAuth;
    Boolean  PDisUID=false;
    public static FirebaseDatabase PDFBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refUserP= PDFBDB.getReference("UserP");

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
        mPDAuth = FirebaseAuth.getInstance();

    }


    private void updatePDUI(FirebaseUser currentUser){
    }

    /**
     * when register button is pressed, check if both fields are correct.
     * make a toast accordingly.
     * @param view
     */


    public void connect(View view) {
        PName=etPName.getText().toString();
        PAdd=etPAdd.getText().toString();
        PDes=etPDes.getText().toString();

        FirebaseUser user = mPDAuth.getCurrentUser();
        Puid = user.getUid();

       // PPAss=user.get
        updatePDUI(user);

        if (PName.isEmpty()||PAdd.isEmpty()||PDes.isEmpty()){
            Toast.makeText(this, "please fill all the necessary details", Toast.LENGTH_SHORT).show();
        }
        else {


            /*PMAil=refUserP.child(Puid).child("PMail").get();
            PMAil.equals(refUserP.child(Puid).child("PMAil").get(PMAil));*/
           // PMAil=user.getEmail();


            refUserP.child(Puid).child("name").removeValue();
            refUserP.child(Puid).child("name").setValue(PName);

            refUserP.child(Puid).child("address").removeValue();
            refUserP.child(Puid).child("address").setValue(PAdd);

            refUserP.child(Puid).child("desc").removeValue();
            refUserP.child(Puid).child("desc").setValue(PDes);

            //if (!PDisUID) {
            //    UserB userdb=new UserB(PName,PMAil,"", PAdd, "", PDes, Puid, "");
         //   refUserP.child(Puid).setValue(userdb);
          //  }
           // Intent si = new Intent(BRegistr.this, BDetails.class);
       //     startActivity(si);
      //  } else {
            // If sign in fails, display a message to the user.
            //    Log.w(TAG, "createUserWithEmail:failure", task.getException());
          //  Toast.makeText(BRegistr.this, "Authentication failed.",
          //          Toast.LENGTH_SHORT).show();
      //      updateUI(null);

            // Sign in success, update UI with the signed-in user's information
            //   Log.d(TAG, "createUserWithEmail:success");
            Intent si = new Intent(PDetails.this, PMain.class);
            startActivity(si);
            finish();
        }
    }

    public void AddKNum(View view) {
    }

}
