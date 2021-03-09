package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class PRegister extends AppCompatActivity {

    String PMail="", PPass="", Puid="";
    EditText Pmail,Ppass;
    private FirebaseAuth mPAuth;
    Boolean PstayConnect, PfirstRun=true,PisUID=false, Pregistered=false;
    public static FirebaseDatabase PFBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refUserP= PFBDB.getReference("UserP");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_register);

        /*
        give each UI variable a value
         */
        Pmail=(EditText)findViewById(R.id.Pmail);
        Ppass=(EditText)findViewById(R.id.Ppass);
        mPAuth = FirebaseAuth.getInstance();

        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        PfirstRun=settings.getBoolean("firstRun",true);
        PstayConnect = false;

        /**
         * this method checks if this is the first run on the user's device
         * if so, it sends the user strait to the registration activity(main activity)
         * if not, it sends him to the location activity
         */
        if (PfirstRun) {
            PisUID=false;
            onVerificationStateChanged();
            regOption();
        }
        else {
            PisUID=true;
            Pregistered = true;
            onVerificationStateChanged();
            Intent si = new Intent(PRegister.this, PDetails.class);
            startActivity(si);
            finish();
        }
    }

    private void regOption() {
        PisUID=false;
        Pregistered=false;
    }

    private void onVerificationStateChanged() {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mPAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser){
    }

    /**
     * when register button is pressed, check if both fields are correct.
     * make a toast accordingly.
     * @param view
     */
    public void register(View view) {
        PMail=Pmail.getText().toString();
        PPass=Ppass.getText().toString();
        if (PMail.isEmpty()||PPass.isEmpty()){
            Toast.makeText(this, "please fill all the necessary details", Toast.LENGTH_SHORT).show();
        }
        else {
            mPAuth.createUserWithEmailAndPassword(PMail, PPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(PRegister.this, "registration completed",
                                Toast.LENGTH_SHORT).show();

                        // Sign in success, update UI with the signed-in user's information
                        //   Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mPAuth.getCurrentUser();
                        Puid = user.getUid();
                        updateUI(user);
                        String image="empty";
                        //  userdb = new User
                        if (!PisUID) {
                            UserP userPdb=new UserP("",PPass, PMail, "", "", "", "", Puid);
                            refUserP.child(Puid).setValue(userPdb);
                        }
                        Intent si = new Intent(PRegister.this, PDetails.class);
                        startActivity(si);
                    } else {
                        // If sign in fails, display a message to the user.
                        //    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(PRegister.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }

    }
}