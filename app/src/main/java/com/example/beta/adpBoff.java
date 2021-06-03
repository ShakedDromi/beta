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

public class adpBoff extends ArrayAdapter<propose> {
    Context contextp;
    ArrayList<propose> pr;


    adpBoff(Context c, ArrayList<propose> pr){
        super(c, R.layout.boffrow, R.id.textView1, pr);
        this.contextp =c;
        this.pr =pr;
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

        name.setText(pr.get(position).getBname());
        age.setText(pr.get(position).getBage()+"");
        price.setText(pr.get(position).getbPrice()+"");

        return bOffrow;
    }
}
