package com.example.beta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class sitteradp extends ArrayAdapter<OfferJob> {
    Context contextb;
    ArrayList<OfferJob> titl;

    sitteradp(Context c, ArrayList<OfferJob> title){
        super(c, R.layout.sitterrow, R.id.textView1, title);
        this.contextb =c;
        this.titl =title;
    }

    public sitteradp(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) contextb.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sitterrow= layoutInflater.inflate(R.layout.sitterrow, parent, false);
        TextView myTitle= sitterrow.findViewById(R.id.textView1);
        TextView myDescription= sitterrow.findViewById(R.id.textView2);

        myTitle.setText(titl.get(position).getDate()+"");
        myDescription.setText(titl.get(position).getTime()+"");

        return sitterrow;
    }
}
