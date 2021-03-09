package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BRegistr extends AppCompatActivity {

    TextView tvTitle, tvRegister;
    String BMail="", BPass="", Buid="";
    EditText Bmail,Bpass;
    private FirebaseAuth mAuth;
    Boolean stayConnect, firstRun=true,isUID=false, registered=false, mVerificationProgress=false;
    ValueEventListener usersListenerl;
    Button btnB;

    FirebaseAuth refAuth;


    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refUserB= FBDB.getReference("UserB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_registr);

        /*
        give each UI variable a value
         */
        Bmail=(EditText)findViewById(R.id.Bmail);
        Bpass=(EditText)findViewById(R.id.Bpass);
        btnB=(Button) findViewById(R.id.btnB);
        tvRegister=(TextView) findViewById(R.id.tvRegister);
        tvTitle=(TextView) findViewById(R.id.tvTitle);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        firstRun=settings.getBoolean("firstRun",true);
        stayConnect = false;

        /**
         * this method checks if this is the first run on the user's device
         * if so, it sends the user strait to the registration activity(main activity)
         * if not, it sends him to the location activity
         */
        if (firstRun) {
            isUID=false;
            onVerificationStateChanged();
            regOption();
        }
        else {
            isUID=true;
            registered = true;
            onVerificationStateChanged();
            Intent si = new Intent(BRegistr.this, BDetails.class);
            startActivity(si);        }
    }


    /**
     * on activity start - if the user exists & asked to  be remembered -transfer to the next activity (Main Screen)
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

      /*  SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        Intent si = new Intent(BRegistr.this,about.class);
        if (refAuth.getCurrentUser()!=null && isChecked) {
            stayConnect=true;
            startActivity(si);
        }*/
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
        isUID=false;
        registered=false;

        tvTitle.setText("REGISTRATION");
        Bmail.setVisibility(View.VISIBLE);
        Bpass.setVisibility(View.VISIBLE);
        btnB.setText("REGISTER");

        //SpannableString spannableString = new SpannableString("Already have an account?  Login here!");
        SpannableString spannableString = new SpannableString("Already Have An Account? Click Here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                registered=true;
                isUID=true;
                logOption();
            }
        };
        spannableString.setSpan(span, 13, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // spannableString.setSpan(span, 26, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.setText(spannableString);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * this function is called when the user is in the register option but he needs to log in.
     * the function "changes" the screen for the login option
     * It is also the default option (the Login activity) unless the user is entering the app for the first time
     */
    private void logOption() {
        tvTitle.setText("CONNECTION");
        Bmail.setVisibility(View.INVISIBLE);
        Bpass.setVisibility(View.VISIBLE);
        btnB.setText("CONNECT");
        registered=true;

        SpannableString spannableString = new SpannableString("Don't Have An Account? Click Here!");
        //SpannableString spannableString = new SpannableString("Don't have an account?  Register here!");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                isUID=false;
                registered=false;
                regOption();
            }
        };
        spannableString.setSpan(span, 14, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(span, 24, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.setText(spannableString);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());


    }




  /*  private void regOption() {
        isUID=false;
        registered=false;
    }*/

    private void onVerificationStateChanged() {
    }

  /*  @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/

    private void updateUI(FirebaseUser currentUser){
    }

    /**
     * when register button is pressed, check if both fields are correct.
     * make a toast accordingly.
     * @param view
     */
    public void logOrReg(View view) {
        BMail=Bmail.getText().toString();
        BPass=Bpass.getText().toString();
        if(registered) {
            if (BPass.isEmpty())
                Toast.makeText(this, "please fill all the necessary details", Toast.LENGTH_SHORT).show();
            else {

                BPass = Bpass.getText().toString();
//if(BPass.contentEquals(refUserB.child(BPass)))
                // FirebaseUser UserB = refAuth.getCurrentUser();
                //Buid = UserB.getUid();
                if (BPass.contentEquals(refUserB.child(BPass).toString())) {
                    Intent si = new Intent(BRegistr.this, BDetails.class);
                    startActivity(si);
                    finish();
                } else {
                    Toast.makeText(this, "password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            if (BMail.isEmpty()||BPass.isEmpty()){
                Toast.makeText(this, "please fill all the necessary details", Toast.LENGTH_SHORT).show();
            }
            else {

                mAuth.createUserWithEmailAndPassword(BMail, BPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BRegistr.this, "registration completed",
                                    Toast.LENGTH_SHORT).show();

                            // Sign in success, update UI with the signed-in user's information
                            //   Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Buid = user.getUid();
                            updateUI(user);
                            String image="empty";
                            //  userdb = new User
                            if (!isUID) {
                                UserB userdb=new UserB("",BMail,"", "", "", "", Buid, BPass);
                                refUserB.child(Buid).setValue(userdb);
                            }
                            Intent si = new Intent(BRegistr.this, BDetails.class);
                            startActivity(si);
                        } else {
                            // If sign in fails, display a message to the user.
                            //    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(BRegistr.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
            }

        }

    }




    }