package com.example.nimmakhaate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomersRVAdapter extends RecyclerView.Adapter<CustomersRVAdapter.ViewHolder>{
    private Context context;
    private Activity activity;
    private ArrayList custnames, amttogive, amttotake, id;


    CustomersRVAdapter(Activity activity, Context context, ArrayList custnames,ArrayList amttotake, ArrayList amttogive, ArrayList id){
//        this.activity = activity;
        this.activity = activity;
        this.context = context;
        this.custnames = custnames;
        this.amttogive = amttogive;
        this.amttotake = amttotake;
        this.id = id;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cust_row,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  final int position) {
        holder.idtext.setText(String.valueOf(id.get(position)));
        holder.customernametext.setText(String.valueOf(custnames.get(position)));
        holder.amttotaketext.setText(String.valueOf(amttotake.get(position)));
        holder.amttogivetext.setText(String.valueOf(amttogive.get(position)));
        holder.Mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id",String.valueOf(id.get(position)));
                intent.putExtra("pendingamt",String.valueOf(amttotake.get(position)));
                activity.startActivityForResult(intent,1);


            }
        });

    }

    @Override
    public int getItemCount() {

        return custnames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView customernametext, amttotaketext, amttogivetext, idtext;
        LinearLayout Mainlayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            customernametext = itemView.findViewById(R.id.customername);
            amttotaketext = itemView.findViewById(R.id.textView6);
            amttogivetext = itemView.findViewById(R.id.textView4);
            Mainlayout = itemView.findViewById(R.id.Mainlayout);
            idtext = itemView.findViewById(R.id.textView5);


        }
    }
}
