package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
}