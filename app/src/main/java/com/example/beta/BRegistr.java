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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        tvBRegister = (TextView) findViewById(R.id.tvRegister);
        tvBTitle = (TextView) findViewById(R.id.tvTitle);
        cBstayconnect = (CheckBox) findViewById(R.id.cBstayconnect);
        stayConnect = false;
        registered = true;
        regOption();

        //  mAuth = FirebaseAuth.getInstance();


        /**
         * this method checks if this is the first run on the user's device
         * if so, it sends the user strait to the registration activity(main activity)
         * if not, it sends him to the location activity
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
        Intent si = new Intent(BRegistr.this, BDetails.class);
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
    private void regOption() {
        SpannableString ss = new SpannableString("Don't Have an Account? Register Here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                tvBTitle.setText("Register");
                etBmail.setVisibility(View.VISIBLE);
                etBpass.setVisibility(View.VISIBLE);
                btnB.setText("Register");
                registered = false;
                logOption();
            }
        };
        ss.setSpan(span, 24, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBRegister.setText(ss);
        tvBRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * this function is called when the user is in the register option but he needs to log in.
     * the function "changes" the screen for the login option
     * It is also the default option (the Login activity) unless the user is entering the app for the first time
     */
    private void logOption() {
        SpannableString ss = new SpannableString("Already Have an Account? Login Here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                tvBTitle.setText("Login");
                etBmail.setVisibility(View.INVISIBLE);
                etBpass.setVisibility(View.VISIBLE);
                btnB.setText("Login");
                registered = true;
                regOption();
            }
        };
        ss.setSpan(span, 26, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            if ((!BMail.contains("@") || !BMail.endsWith(".il")) && (!BMail.endsWith(".com") || !BMail.contains("@"))) {
                etBmail.setError("Mail is Invalid!");
            }
            if (BPass.length() < 6) {
                etBpass.setError("Password Needs To Be At Least 5 Characters!");
            } else {
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
                                Intent si = new Intent(BRegistr.this, BMain.class);
                                si.putExtra("UserB", false);
                                startActivity(si);
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
                                Intent si = new Intent(BRegistr.this, BDetails.class);
                                si.putExtra("UesrB", true);
                                startActivity(si);
                                finish();
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(BRegistr.this, "User With Email Alreasy Exist!", Toast.LENGTH_SHORT).show();
                                else {
                                    Log.w("BRegistr", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(BRegistr.this, "User Creat Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        }
    }
}