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

public class adpBoff extends ArrayAdapter<String> {
    Context contextp;
    ArrayList<String> bname;
    ArrayList<Integer> bage;
    ArrayList<Integer> bprice;

    adpBoff(Context c, ArrayList<String> nameb, ArrayList<Integer> ageb, ArrayList<Integer> priceb){
        super(c, R.layout.boffrow, R.id.textView1, nameb);
        this.contextp =c;
        this.bname =nameb;
        this.bage =ageb;
        this.bprice=priceb;
    }

    public adpBoff(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) contextp.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bOffrow= layoutInflater.inflate(R.layout.boffrow, parent, false);
        TextView name= bOffrow.findViewById(R.id.textView1);
        TextView age= bOffrow.findViewById(R.id.textView2);
        TextView price= bOffrow.findViewById(R.id.textView3);

        name.setText(bname.get(position));
        age.setText(bage.get(position)+"");
        price.setText(bprice.get(position)+"");

        return bOffrow;
    }
}
