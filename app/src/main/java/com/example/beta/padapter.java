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

public class padapter extends ArrayAdapter<OfferJob> {
    Context contextp;
    ArrayList<OfferJob> rpTitle;

    padapter(Context c, ArrayList<OfferJob> title){
        super(c, R.layout.prow, R.id.textView1, title);
        this.contextp =c;
        this.rpTitle =title;
    }

    public padapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) contextp.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View prow= layoutInflater.inflate(R.layout.prow, parent, false);
        TextView myTitle= prow.findViewById(R.id.textView1);
        TextView myDescription= prow.findViewById(R.id.textView2);

        myTitle.setText(rpTitle.get(position).getDate()+"");
        myDescription.setText(rpTitle.get(position).getTime()+"");

        return prow;
    }
}
