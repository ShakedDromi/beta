package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.beta.FBref.refUsersB;
import static com.example.beta.FBref.refUsersP;

public class BMain extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lV2;
    UserB userB;
    String stplace="";
    String uidB;
    private FirebaseAuth mBRAuth;

    TextView tvname, tvnum, tvage, tvdes;

    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> mails = new ArrayList<>();
    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<UserP> usersP = new ArrayList<>();

    //ArrayList<String> kidBdayList = new ArrayList<String>();
    //ArrayAdapter<String> adp;
    //private FirebaseAuth mPRAuth;

    //String mTitle[]= {"facebook", "whatsapp", "twitter", "instagram"};
    //String mDescription[]= {"facebook description","whatsapp description","twitter description","instagram description"};
    //int images[]={R.drawable.facebook, R.drawable.whatsapp, R.drawable.twitter, R.drawable.instagram11};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_main);

        tvage=(TextView) findViewById(R.id.tvage);
        tvdes=(TextView) findViewById(R.id.tvdes);
        tvname=(TextView) findViewById(R.id.tvname);
        tvnum=(TextView) findViewById(R.id.tvnum);


        lV2 = (ListView) findViewById(R.id.lV2);
        lV2.setOnItemClickListener(this);
        lV2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
       /* adp =new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, kidBdayList);
        lV2.setAdapter(adp);
*/



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
                @Override
                public void onDataChange(@NonNull DataSnapshot DS) {
                    // UserP userp = new UserP();
                    for (DataSnapshot data: DS.getChildren()) {
                        userB = data.getValue(UserB.class);
                        stplace=userB.getAddress();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //updatePDUI(user);

            ValueEventListener pListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {
                names.clear();
                mails.clear();
                for(DataSnapshot data : dS.getChildren()) {
                    UserP uP = data.getValue(UserP.class);
                    if (uP.getpaddress().equals(stplace)){
                        mails.add(uP.getpmail());
                        names.add(uP.getpname());
                        images.add(R.drawable.facebook);
                        usersP.add(uP);
                    }

                }
                //adp = new ArrayAdapter<String>(StuDisAct.this,R.layout.support_simple_spinner_dropdown_item, names);
                //lV2.setAdapter(adp);
                MyAdapter myadp = new MyAdapter(BMain.this, names, mails, images);

                //MyAdapter adapter= new MyAdapter(BMain.this, mTitle, mDescription, images);
                lV2.setAdapter(myadp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        refUsersP.addListenerForSingleValueEvent(pListener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       /* ValueEventListener pListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {
                for(DataSnapshot data : dS.getChildren()) {
                    UserP uP = data.getValue(UserP.class);
                    mails.add(uP.getpmail());
                    names.add(uP.getpname());
                    images.add(R.drawable.facebook);
                }
                //adp = new ArrayAdapter<String>(StuDisAct.this,R.layout.support_simple_spinner_dropdown_item, names);
                //lV2.setAdapter(adp);
                MyAdapter myadp = new MyAdapter(BMain.this, names, mails, images);

                //MyAdapter adapter= new MyAdapter(BMain.this, mTitle, mDescription, images);
                lV2.setAdapter(myadp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        refUsersP.addListenerForSingleValueEvent(pListener);*/


      //  String st= ((String) usersP.get(position).getPknum());
        tvname.setText(usersP.get(position).getpname());
        tvnum.setText(Integer.toString(usersP.get(position).getPknum())+"kids");
        //tvnum.setText(usersP.get(position).getPknum());
        tvage.setText(usersP.get(position).getpmail());
        tvdes.setText(usersP.get(position).getpdesc());


        /*if(position==0)
            Toast.makeText(BMain.this, "facebook description", Toast.LENGTH_SHORT).show();
        if(position==1)
            Toast.makeText(BMain.this, "whatsapp description", Toast.LENGTH_SHORT).show();
        if(position==2)
            Toast.makeText(BMain.this, "twitter description", Toast.LENGTH_SHORT).show();
        if(position==3)
            Toast.makeText(BMain.this, "instagram description", Toast.LENGTH_SHORT).show();*/
    }

    public void offer(View view) {
       // if(onItemClick) only if the pressed a person, they will be able to press the offer button


        AlertDialog.Builder adoffer;
        adoffer=new AlertDialog.Builder(this);

        //FirebaseUser user = mPRAuth.getCurrentUser();
       // uidP = user.getUid();


        adoffer.setTitle("Enter how much you charge per hour");
        final EditText et=new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setSingleLine(false);
        //et.setText(tvPDes.getText());


        adoffer.setView(et);
        adoffer.setPositiveButton("offer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                /*if((et.length()<100)&&(et.length()>20)) {
                    tvPDes.setText(et.getText());
                    refUsersP.child(uidP).child("pdesc").setValue(tvPDes.getText().toString());
                }
                else
                    Toast.makeText(PPersonal.this, "description must be between 20-100 chars", Toast.LENGTH_LONG).show();
                //    Toast.makeText(this, "description must be under 100 char", Toast.LENGTH_SHORT).show();*/
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
}