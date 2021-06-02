package com.example.beta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> rTitle;
    ArrayList<String> rDescription;

    MyAdapter(Context c, ArrayList<String> title, ArrayList<String> description){
        super(c, R.layout.row, R.id.textView1, title);
        this.context=c;
        this.rTitle=title;
        this.rDescription=description;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row= layoutInflater.inflate(R.layout.row, parent, false);
        TextView myTitle= row.findViewById(R.id.textView1);
        TextView myDescription= row.findViewById(R.id.textView2);

        myTitle.setText(rTitle.get(position));
        myDescription.setText(rDescription.get(position));

        return row;
    }
}
