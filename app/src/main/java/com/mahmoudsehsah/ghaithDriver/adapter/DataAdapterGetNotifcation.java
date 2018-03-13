package com.mahmoudsehsah.ghaithDriver.adapter;

/**
 * Created by mahmoud on 3/13/2018.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.models.MyNotfcation;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by mahmoud on 3/12/2018.
 */

public class DataAdapterGetNotifcation extends RecyclerView.Adapter<DataAdapterGetNotifcation.ViewHolder> {
    private ArrayList<MyNotfcation> android;
    private Context context;


    public DataAdapterGetNotifcation(Context context, ArrayList<MyNotfcation> android) {
        this.android = android;
        this.context = context;


    }

    @Override
    public DataAdapterGetNotifcation.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Calligrapher calligrapher = new Calligrapher(viewGroup.getContext());
        calligrapher.setFont((Activity) viewGroup.getContext(), "font/jf.ttf", true);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.container_notifcation, viewGroup, false);
        return new DataAdapterGetNotifcation.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterGetNotifcation.ViewHolder viewHolder, int i) {
        viewHolder.text.setText(android.get(i).gettext());
        viewHolder.id_driver.setText(android.get(i).getid_driver());
        viewHolder.name_user.setText(android.get(i).getname_user());
        viewHolder.id.setText(android.get(i).getid_order());
        Picasso.with(context).load(android.get(i).getphoto_user()).placeholder(R.drawable.loading).into(viewHolder.photo_user);

    }


    @Override
    public int getItemCount() {

        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Typeface customTypeOne = Typeface.createFromAsset(itemView.getContext().getAssets(), "font/jf.ttf");
        private TextView text,id_driver,name_user,price,id;
        public ImageView photo_user;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            text = view.findViewById(R.id.text);
            photo_user = view.findViewById(R.id.photo_user);
            name_user = view.findViewById(R.id.name_user);
            id = view.findViewById(R.id.id);
            text.setTypeface(customTypeOne);
        }

        @Override
        public void onClick(View view) {
            final Context context = view.getContext();


        }

    }


}