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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import static com.example.beta.FBref.refJobOffer;
import static com.example.beta.FBref.refStor;
import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class BMain extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lV2;
    UserB userB;
    String stplace = "", bname;
    int bPrice = 0, bage,x;
    String uidB, uidp, JPid;
    private FirebaseAuth mBRAuth;
    boolean pressed = false, picked=false;

    ImageView priv;
    TextView tvname, tvnum, tvage, tvdes;

    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<UserP> usersP = new ArrayList<>();
    ArrayList<propose> proposeB = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_main);

        tvage = (TextView) findViewById(R.id.tvage);
        tvdes = (TextView) findViewById(R.id.tvdes);
        tvname = (TextView) findViewById(R.id.tvname);
        tvnum = (TextView) findViewById(R.id.tvnum);
        priv = (ImageView) findViewById(R.id.priv);

        lV2 = (ListView) findViewById(R.id.lV2);
        lV2.setOnItemClickListener(this);
        lV2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void updatePDUI(FirebaseUser currentUser) {

    }


    /**
     * when activity starts- this method fills the list view with the according data- date and time of the jobs offers
     * it shows only the offers of the parents who are in the same neighborhood as the babysitter.
     */
    @Override
    public void onStart() {
        super.onStart();
            mBRAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mBRAuth.getCurrentUser();
            if(user != null)
                uidB = user.getUid();

            Query query = refUsersB.orderByKey().equalTo(uidB).limitToFirst(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot DS) {
                    for (DataSnapshot data: DS.getChildren()) {
                        userB = data.getValue(UserB.class);
                        stplace=userB.getAddress();
                        bname=userB.getName();
                        bage=userB.getAge();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        Query quer = refJobOffer.orderByKey();
        quer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DS) {
                // UserP userp = new UserP();
                date.clear();
                time.clear();
                for (DataSnapshot data: DS.getChildren()) {
                    OfferJob jobOff = data.getValue(OfferJob.class);
                    if(jobOff.getMakom().equals(stplace)){
                        time.add(jobOff.getTime());
                        date.add(jobOff.getDate());
                        uidp=jobOff.getUidJP();


                        Query querr = refUsersP.orderByChild("puid").equalTo(uidp);
                        querr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot DS) {
                                for (DataSnapshot data: DS.getChildren()) {
                                    UserP puser = data.getValue(UserP.class);
                                    usersP.add(puser);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                MyAdapter myadp = new MyAdapter(BMain.this, date, time);
                lV2.setAdapter(myadp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * this method downloads the user's image from firebase storage according to the image's link.
     * @throws IOException
     */
    public void Download() throws IOException {
        StorageReference refImages=refStor.child(uidp+".jpg");
        final File localFile;
        localFile = File.createTempFile(uidp,"jpg");
        refImages.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                String filePath = localFile.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                priv.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(BMain.this, "Image download failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * this method presents the parent's data from Firebase to the babysitter.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pressed=true;
        tvname.setText(usersP.get(position).getpname());
        tvnum.setText(Integer.toString(usersP.get(position).getPknum())+"kids");
        //tvnum.setText(usersP.get(position).getPknum());
        tvage.setText(usersP.get(position).getpmail());
        tvdes.setText(usersP.get(position).getpdesc());
        uidp=usersP.get(position).getpuid();

        try {
            Download();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Query quer = refJobOffer.orderByChild("uidJP").equalTo(uidp);
        quer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                if (ds.exists()) {
                    for (DataSnapshot data : ds.getChildren()) {
                        JPid=data.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /**
     * this method opens an alert dialog in which the babysitter needs to insert how much she charges per hour.
     * @param view
     */
    public void offer(View view) {
        if(pressed){
            AlertDialog.Builder adoffer;
            adoffer=new AlertDialog.Builder(this);

            adoffer.setTitle("Enter how much you charge per hour");
            final EditText et=new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setSingleLine(false);

            adoffer.setView(et);
            adoffer.setPositiveButton("offer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (et.getText().toString().isEmpty())
                        Toast.makeText(BMain.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                    else {
                        bPrice=Integer.parseInt(et.getText().toString());
                        proposeB.clear();
//VEL, after onDataChange add lines
                        //DatabaseReference refOfferJob = refJobOffer.getKey(JPid).child("propose");

                        DatabaseReference refOfferJob = refJobOffer.child(JPid).child("propose");
                        //Query queri = refJobOffer.orderByKey().equalTo(JPid);


                         refOfferJob.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot ds) {
                                if (ds.exists()) {
                                    for (DataSnapshot data : ds.getChildren()) {
                                        propose pr = data.getValue(propose.class);
                                        proposeB.add(pr);
                                    }
                                }
                                propose props = new propose(uidB, bname, bPrice, bage, picked);
                                proposeB.add(props);

                                refJobOffer.child(JPid).child("propose").setValue(proposeB);

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                            }
                        });
                        Intent si = new Intent(BMain.this, BfirstAct.class);
                        startActivity(si);
                        finish();
                    }
                }
            });
            adoffer.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog adk=adoffer.create();
            adk.show();
        }
        else{
            Toast.makeText(BMain.this, "an Offer Must Be Pressed first", Toast.LENGTH_LONG).show();
        }
    }
}