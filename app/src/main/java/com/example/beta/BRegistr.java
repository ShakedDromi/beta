package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.beta.FBref.refAuth;
import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class BRegistr extends AppCompatActivity {

    TextView tvBTitle, tvBRegister;
    String BMail = "", BPass = "", Buid = "";
    EditText etBmail, etBpass;
    // private FirebaseAuth mAuth;
    Boolean stayConnect, firstRun, registered; //isUID=false,mVerificationProgress=false;
    Button btnB;
    UserB userBdb;
    CheckBox cBstayconnect;
    boolean userAlreadyExists=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_registr);

        /*
        give each UI variable a value
         */
        etBmail = (EditText) findViewById(R.id.Bmail);
        etBpass = (EditText) findViewById(R.id.Bpass);
        btnB = (Button) findViewById(R.id.btnB);
        tvBRegister = (TextView) findViewById(R.id.tvBRegister);
        tvBTitle = (TextView) findViewById(R.id.tvBTitle);
        cBstayconnect = (CheckBox) findViewById(R.id.cBstayconnect);
        stayConnect = false;
        registered = true;
        regOptionB();

        //  mAuth = FirebaseAuth.getInstance();


        /**
         * this method checks if this is the first run on the user's device
         * if so, it sends the user strait to the registration activity(main activity)
         * if not, it sends him to the connection activity
         */
        /*if (firstRun) {
            isUID=false;
            onVerificationStateChanged();
            regOption();
        }
        else {
            isUID=true;
            registered = true;
            onVerificationStateChanged();
            Intent si = new Intent(BRegistr.this, BDetails.class);
            startActivity(si);        }*/
    }


    /**
     * on activity start - if the user exists & asked to  be remembered -transfer to the next activity (Main Screen)
     */
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        Boolean isChecked = settings.getBoolean("stayConnect", false);
        Intent si = new Intent(BRegistr.this, BfirstAct.class);
        if ((refAuth.getCurrentUser() != null) && (isChecked)) {
            stayConnect = true;
            si.putExtra("UserB", false);
            startActivity(si);
        }
    }

    /**
     * On activity pause - If logged in & asked to be remembered - kill activity.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (stayConnect) finish();
    }

    /**
     * this function is called when the user is in the login option but he needs to register
     * OR
     * when the application is running for the first time in the user's device
     * the function "changes" the screen for the register option.
     */
    private void regOptionB() {
        SpannableString s = new SpannableString("Don't Have an Account? Register Here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                tvBTitle.setText("Register");
                etBmail.setVisibility(View.VISIBLE);
                etBpass.setVisibility(View.VISIBLE);
                btnB.setText("Register");
                registered = false;
                logOptionB();
            }
        };
        s.setSpan(span, 23, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBRegister.setText(s);
        tvBRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * this function is called when the user is in the register option but he needs to log in.
     * the function "changes" the screen for the login option
     * It is also the default option (the Login activity) unless the user is entering the app for the first time
     */
    private void logOptionB() {
        SpannableString ss = new SpannableString("Already Have an Account? Login Here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                tvBTitle.setText("Login");
                etBmail.setVisibility(View.VISIBLE);
                etBpass.setVisibility(View.VISIBLE);
                btnB.setText("Login");
                registered = true;
                regOptionB();
            }
        };
        ss.setSpan(span, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBRegister.setText(ss);
        tvBRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * when register button is pressed, check if both fields are correct.
     * make a toast accordingly.
     *
     * @param view
     */
    public void logOrReg(View view) {
        BMail = etBmail.getText().toString();
        BPass = etBpass.getText().toString();

        if (BMail.isEmpty() || BPass.isEmpty())
            Toast.makeText(this, "please fill all the necessary fields", Toast.LENGTH_SHORT).show();
        else {
            if (!isEmailValid(BMail))
                etBmail.setError("Mail is Invalid!");
            else {
                if (BPass.length() < 5)
                    etBpass.setError("Password Needs To Be At Least 5 Characters!");
                else {
                    if (!BPass.matches("[a-zA-Z0-9]*"))
                        etBpass.setError("Password must contain only English letters and numbers");
                    else {

                        refUsersP.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotbsitter) {
                                for (DataSnapshot dataSnapshotb : snapshotbsitter.getChildren()) {
                                    UserP userPcheck = dataSnapshotb.getValue(UserP.class);
                                    if (userPcheck.getpmail().equals(BMail)) {
                                       // Toast.makeText(BRegistr.this, "User With Email Alreasy Exist!", Toast.LENGTH_SHORT).show();
                                        userAlreadyExists=true;
                                    }
                                    else    userAlreadyExists=false;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if (!userAlreadyExists) {
                            if (registered) {
                                BMail = etBmail.getText().toString();
                                BPass = etBpass.getText().toString();

                                final ProgressDialog pd = ProgressDialog.show(this, "Login", "Connecting...", true);
                                refAuth.signInWithEmailAndPassword(BMail, BPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            pd.dismiss();

                                            if (task.isSuccessful()) {
                                                SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = settings.edit();
                                                editor.putBoolean("stayConnect", cBstayconnect.isChecked());
                                                editor.commit();
                                                Log.d("BRegistr", "signinUserWithEmail:success");
                                                Toast.makeText(BRegistr.this, "Login Success", Toast.LENGTH_SHORT).show();
                                                Intent si = new Intent(BRegistr.this, BfirstAct.class);
                                                si.putExtra("UserB", false);
                                                startActivity(si);
                                                finish();
                                            } else {
                                                Log.d("BRegistr", "signinUserWithEmail:fail");
                                                Toast.makeText(BRegistr.this, "email or password are wrong!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                            } else {
                                BMail = etBmail.getText().toString();
                                BPass = etBpass.getText().toString();
                                final ProgressDialog pd = ProgressDialog.show(this, "Register", "Registering...", true);
                                refAuth.createUserWithEmailAndPassword(BMail, BPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        pd.dismiss();
                                        if (task.isSuccessful()) {
                                            SharedPreferences settings = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.putBoolean("stayConnect", cBstayconnect.isChecked());
                                            editor.commit();
                                            Log.d("BRegistr", "createUserWithEmail:success");
                                            FirebaseUser UserB = refAuth.getCurrentUser();
                                            Buid = UserB.getUid();
                                            userBdb = new UserB("", BMail, "", "", "", "", Buid, BPass);
                                            refUsersB.child(Buid).setValue(userBdb);
                                            Toast.makeText(BRegistr.this, "Successful Registration", Toast.LENGTH_SHORT).show();
                                            Intent si = new Intent(BRegistr.this, BfirstAct.class);
                                            si.putExtra("UesrB", true);
                                            startActivity(si);
                                            finish();
                                        } else {
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                                Toast.makeText(BRegistr.this, "User With Email Alreasy Exist!", Toast.LENGTH_SHORT).show();
                                            else {
                                                Log.w("BRegistr", "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(BRegistr.this, "User Create Failed", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(BRegistr.this, "User With Email Already Exist as a Parent!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    @Override
    public void onBackPressed() {
        Intent goBackFromBRegister= new Intent(this, MainActivity.class);
        startActivity(goBackFromBRegister);

    }
}