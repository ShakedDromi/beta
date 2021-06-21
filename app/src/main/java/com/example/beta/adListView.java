package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.beta.FBref.refJobOffer;
import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class adListView extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvp;
    private FirebaseAuth mPRAuth;

    private String newId, sitterid;
    ArrayList<propose> props = new ArrayList<>();

    //Class<? extends ArrayList> p=new ArrayList<>();
    String idP;
    adpBoff bof;
    ArrayList<OfferJob> offers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_list_view);

        lvp = (ListView) findViewById(R.id.lvp);
        lvp.setOnItemClickListener(this);
        lvp.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        newId=getIntent().getStringExtra("uid");

    }

    /**
     * this method get all the proposes matching to the chosen job offer from firebase.
     */
    protected void onStart() {
        super.onStart();

        mPRAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mPRAuth.getCurrentUser();
        if (user != null)
            idP = user.getUid();

        Query refpr=refJobOffer.child(newId).child("propose");
        refpr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                props.clear();

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    propose pr=data.getValue(propose.class);
                    props.add(pr);
                }
                bof = new adpBoff(adListView.this, props);
                lvp.setAdapter(bof);
                bof.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * this method opens an alert dialog in which the parent has to decide if he wants to choose this babysitter or not.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sitterid=((adpBoff)lvp.getAdapter()).getItem(position).getbUid();

        AlertDialog.Builder adchoose;
        adchoose=new AlertDialog.Builder(this);

        adchoose.setTitle("want to choose this babysitter?");

        adchoose.setPositiveButton("choose", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query refpro=refJobOffer.child(newId).child("propose");
                refpro.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            propose pr=data.getValue(propose.class);
                            if(pr.getbUid().equals(sitterid)){
                                refJobOffer.child(newId).child("propose").child(String.valueOf(position)).child("picked").setValue(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent si = new Intent(adListView.this, PfirstAct.class);
                startActivity(si);
                finish();
            }
        });
        adchoose.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog ad=adchoose.create();
        ad.show();

    }
}