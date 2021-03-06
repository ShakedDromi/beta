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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.beta.FBref.refAuth;
import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class PRegister extends AppCompatActivity {

    TextView tvPTitle, tvPRegister;
    String PMail ="", PPass ="", Puid ="";
    EditText etPmail, etPpass;
    // private FirebaseAuth mAuth;
    Boolean PstayConnect, PfirstRun, Pregistered; //isUID=false,mVerificationProgress=false;
    Button btnP;
    UserP userPdb;
    CheckBox PcBstayconnect;
    boolean userAlreadyExistsAsBabysitter=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_register);

        /**
         * this conects between the xml components and the java variable.
         */
        etPmail =(EditText)findViewById(R.id.Pmail);
        etPpass =(EditText)findViewById(R.id.Ppass);
        btnP =(Button) findViewById(R.id.btnP);
        tvPRegister =(TextView) findViewById(R.id.tvPRegister);
        tvPTitle=(TextView) findViewById(R.id.tvPTitle);
        PcBstayconnect =(CheckBox) findViewById(R.id.PcBstayconnect);
        PstayConnect =false;
        Pregistered =true;
        regOption();
    }


    /**
     * on activity start - if the user exists & asked to  be remembered -transfer to the next activity (Main Screen)
     */
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        Intent si=new Intent(PRegister.this, PfirstAct.class);
        if((refAuth.getCurrentUser()!=null) && (isChecked)){
            PstayConnect =true;
            si.putExtra("UserP",false);
            startActivity(si);
        }
    }

    /**
     * On activity pause - If logged in & asked to be remembered - kill activity.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (PstayConnect) finish();
    }

    /**
     * this function is called when the user is in the login option but he needs to register
     * OR
     * when the application is running for the first time in the user's device
     * the function "changes" the screen for the register option.
     */
    private void regOption() {
        SpannableString ss=new SpannableString("Don't Have an Account? Register Here!");
        ClickableSpan span=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                tvPTitle.setText("Register");
                etPmail.setVisibility(View.VISIBLE);
                etPpass.setVisibility(View.VISIBLE);
                btnP.setText("Register");
                Pregistered =false;
                logOption();
            }
        };
        ss.setSpan(span,24,36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPRegister.setText(ss);
        tvPRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * this function is called when the user is in the register option but he needs to log in.
     * the function "changes" the screen for the login option
     * It is also the default option (the Login activity) unless the user is entering the app for the first time
     */
    private void logOption() {
        SpannableString ss=new SpannableString("Already Have an Account? Login Here!");
        ClickableSpan span=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                tvPTitle.setText("Login");
                etPmail.setVisibility(View.INVISIBLE);
                etPpass.setVisibility(View.VISIBLE);
                btnP.setText("Login");
                Pregistered =true;
                regOption();
            }
        };
        ss.setSpan(span,26,34,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPRegister.setText(ss);
        tvPRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * when register button is pressed, check if both fields are correct.
     * make a toast accordingly.
     * @param view
     */
    public void LOrR(View view) {
        PMail = etPmail.getText().toString();
        PPass = etPpass.getText().toString();
        if(PMail.isEmpty()||PPass.isEmpty())
            Toast.makeText(this, "please fill all the necessary fileds", Toast.LENGTH_SHORT).show();
        else{
            if (!isEmailValid(PMail))
                etPmail.setError("Mail is Invalid!");
            else {
                if (PPass.length() < 5)
                    etPpass.setError("Password Needs To Be At Least 5 Characters!");
                else {
                    if (!PPass.matches("[a-zA-Z0-9]*"))
                        etPpass.setError("Password must contain only English letters and numbers");
                    else {
                        refUsersB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                UserB userbcheck = dataSnapshot.getValue(UserB.class);
                                if (userbcheck.getMail().equals(PMail)) {
                                    //   Toast.makeText(PRegister.this, "User With Email Alreasy Exist!", Toast.LENGTH_SHORT).show();
                                    userAlreadyExistsAsBabysitter=true;
                                }
                                else userAlreadyExistsAsBabysitter=false;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                        if (!userAlreadyExistsAsBabysitter) {
                        if (Pregistered) {
                            PMail = etPmail.getText().toString();
                            PPass = etPpass.getText().toString();



                            final ProgressDialog pd = ProgressDialog.show(this, "Login", "Connecting...", true);
                            refAuth.signInWithEmailAndPassword(PMail, PPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    pd.dismiss();
                                   if (task.isSuccessful()) {
                                        SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putBoolean("stayConnect", PcBstayconnect.isChecked());
                                        editor.commit();
                                        Log.d("PRegistr", "signinUserWithEmail:success");
                                        Toast.makeText(PRegister.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        Intent si = new Intent(PRegister.this, PfirstAct.class);
                                        si.putExtra("UserP", false);
                                        startActivity(si);
                                        finish();
                                    } else {
                                        Log.d("PRegistr", "signinUserWithEmail:fail");
                                        Toast.makeText(PRegister.this, "email or password are wrong!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            PMail = etPmail.getText().toString();
                            PPass = etPpass.getText().toString();
                            final ProgressDialog pd = ProgressDialog.show(this, "Register", "Registering...", true);
                            refAuth.createUserWithEmailAndPassword(PMail, PPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    pd.dismiss();
                                    if (task.isSuccessful()) {
                                        SharedPreferences settings = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putBoolean("stayConnect", PcBstayconnect.isChecked());
                                        editor.commit();
                                        Log.d("PRegistr", "createUserWithEmail:success");
                                        FirebaseUser UserP = refAuth.getCurrentUser();
                                        Puid = UserP.getUid();
                                        userPdb = new UserP("", PPass, PMail, "", "", Puid, 0);
                                        refUsersP.child(Puid).setValue(userPdb);
                                        Toast.makeText(PRegister.this, "Successful Registration", Toast.LENGTH_SHORT).show();
                                        Intent si = new Intent(PRegister.this, PDetails.class);
                                        si.putExtra("UesrP", true);
                                        startActivity(si);
                                        finish();
                                    } else {
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                                Toast.makeText(PRegister.this, "User With Email Alreasy Exist!", Toast.LENGTH_SHORT).show();

                                            else {
                                                Log.w("PRegistr", "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(PRegister.this, "User Creat Failed", Toast.LENGTH_LONG).show();
                                            }
                                    }
                                }
                            });
                        }
                        }
                        else{
                            Toast.makeText(PRegister.this, "User With Email Already Exist as a Babysitter!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    /**
     * this method checks if the email is valid
     * @param email
     * @return
     */
    boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        Intent goBackFromPRegister= new Intent(this, MainActivity.class);
        startActivity(goBackFromPRegister);

    }
}