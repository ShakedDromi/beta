package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class BfirstAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfirst);
    }

    public void search(View view) {
        Intent si = new Intent(BfirstAct.this, BMain.class);
        startActivity(si);
        finish();
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
            Intent si = new Intent(BfirstAct.this, about.class);
            startActivity(si);
        }
        if (st.equals("BPersonal")) {
            Intent si = new Intent(BfirstAct.this, BPersonal.class);
            startActivity(si);
        }
        if (st.equals("BfirstAct")) {
            Intent si = new Intent(BfirstAct.this, BfirstAct.class);
            startActivity(si);
        }

        return true;
    }
}