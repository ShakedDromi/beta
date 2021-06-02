package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.beta.FBref.refPlaces;
import static com.example.beta.FBref.refStor;
import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class BPersonal extends AppCompatActivity {

    TextView tvTitleB, tvMailB, tvAddB, tvDesB, tvDateB;
    UserB userB;
    String uidB;
    private FirebaseAuth mBRAuth;

    ImageView biv;

    Spinner spPlaceB;
    List<String> placesB = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_personal);

        tvTitleB = (TextView) findViewById(R.id.tvTitleB);
        tvMailB = (TextView) findViewById(R.id.tvMailB);
        tvAddB = (TextView) findViewById(R.id.tvAddB);
        tvDesB = (TextView) findViewById(R.id.tvDesB);
        tvDateB = (TextView) findViewById(R.id.tvDateB);
        spPlaceB = (Spinner) findViewById(R.id.spPlaceB);
        biv = (ImageView) findViewById(R.id.biv);



        /**
         * this function uploads the information from the firebase tree - Places - to a spinner
         * using the reference - refPlaces and a Value event listener.
         */
        refPlaces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                placesB.clear();

                for (DataSnapshot data : ds.getChildren()){
                    String info=data.getValue().toString();
                    placesB.add(info);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BPersonal.this, android.R.layout.simple_spinner_item, placesB);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPlaceB.setAdapter(arrayAdapter);
                //spPlaceP.setOnItemSelectedListener(new OnSpinnerItemClicked());
                //tvPAdd.setText(spPlaceP.getSelectedItem().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BPersonal.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePDUI(FirebaseUser currentUser){

    }

    @Override
    public void onStart() {
        super.onStart();
        mBRAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mBRAuth.getCurrentUser();
        if(user != null)
            uidB = user.getUid();

        Query query = refUsersB.orderByKey().equalTo(uidB).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            class OnSpinnerItemClicked implements android.widget.AdapterView.OnItemSelectedListener {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    refUsersP.child(uidB).child("address").setValue(parent.getItemAtPosition(position).toString());
                    tvAddB.setText(parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            }

            @Override
            public void onDataChange(@NonNull DataSnapshot DS) {
                // UserP userp = new UserP();
                for (DataSnapshot data: DS.getChildren()) {
                    userB = data.getValue(UserB.class);
                    tvTitleB.setText("Hi," + userB.getName() + "!");
                    tvMailB.setText(userB.getMail());
                    tvAddB.setText(userB.getAddress());
                    tvDesB.setText(userB.getDescription());
                    tvDateB.setText(userB.getBirthDate());
                    try {
                        Download();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                spPlaceB.setOnItemSelectedListener(new OnSpinnerItemClicked());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updatePDUI(user);
    }

    public void Download() throws IOException {
        StorageReference refImages=refStor.child(uidB+".jpg");
        final File localFile;
        localFile = File.createTempFile(uidB,"jpg");
        refImages.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                String filePath = localFile.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                biv.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(BPersonal.this, "Image download failed", Toast.LENGTH_LONG).show();
            }
        });
    }



    public void AddressChngeB(View view) {
        AlertDialog.Builder adAdd;
        adAdd=new AlertDialog.Builder(this);

        FirebaseUser user = mBRAuth.getCurrentUser();
        uidB = user.getUid();

        adAdd.setTitle("Change Your Address");
        final EditText et=new EditText(this);
        adAdd.setView(et);
        adAdd.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvAddB.setText(et.getText());
                refUsersB.child(uidB).child("address").setValue(tvAddB.getText().toString());
            }
        });
        adAdd.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog adk=adAdd.create();
        adk.show();
    }

    public void descriptionChngeB(View view) {
        AlertDialog.Builder adDes;
        adDes=new AlertDialog.Builder(this);

        FirebaseUser user = mBRAuth.getCurrentUser();
        uidB = user.getUid();


        adDes.setTitle("Change Description");
        final EditText et=new EditText(this);
        et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et.setSingleLine(false);
        et.setText(tvDesB.getText());

        adDes.setView(et);
        adDes.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if((et.length()<100)&&(et.length()>20)) {
                    tvDesB.setText(et.getText());
                    refUsersB.child(uidB).child("description").setValue(tvDesB.getText().toString());
                }
                else
                    Toast.makeText(BPersonal.this, "description must be between 20-100 chars", Toast.LENGTH_LONG).show();
            }
        });



        adDes.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        adDes.setNegativeButton("clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adDes.setCancelable(false);
                et.setText("");
                //  et.toString().replace(et.toString()," ");
            }
        });

        AlertDialog adk=adDes.create();
        adk.show();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainb, menu);
        return true;
    }

    /**
     * this function gets the user's choice from the menu and sends him to the appropriate activity
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected (MenuItem item){
        String st = item.getTitle().toString();
        if (st.equals("about")) {
            Intent si = new Intent(BPersonal.this, about.class);
            startActivity(si);
        }
        if (st.equals("BPersonal")) {
            Intent si = new Intent(BPersonal.this, BPersonal.class);
            startActivity(si);
        }
        if (st.equals("BfirstAct")) {
            Intent si = new Intent(BPersonal.this, BfirstAct.class);
            startActivity(si);
        }

        return true;
    }

}