package com.mahmoudsehsah.ghaithDriver.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.acitivities.OrderSingleActivity;
import com.mahmoudsehsah.ghaithDriver.models.GetOrder;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by mahmoud on 3/11/2018.
 */

public class DataAdapterGetOrders extends RecyclerView.Adapter<DataAdapterGetOrders.ViewHolder> {
    private ArrayList<GetOrder> android;
    private Context context;


    public DataAdapterGetOrders(Context context, ArrayList<GetOrder> android ) {
        this.android = android;
        this.context = context;


    }

    @Override
    public DataAdapterGetOrders.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Calligrapher calligrapher = new Calligrapher(viewGroup.getContext());
        calligrapher.setFont((Activity) viewGroup.getContext(), "font/jf.ttf", true);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.container_get_orders, viewGroup, false);
        return new DataAdapterGetOrders.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterGetOrders.ViewHolder viewHolder, int i) {


        viewHolder.name.setText(android.get(i).getName());
        viewHolder.description.setText(android.get(i).getDescription());
        viewHolder.dist.setText(android.get(i).getKm());
        viewHolder.id.setText(android.get(i).getId());
        viewHolder.time.setText(android.get(i).getTime());
        viewHolder.lat_user.setText(android.get(i).getLat());
        viewHolder.lng_user.setText(android.get(i).getLng());
        viewHolder.lat_market.setText(android.get(i).getLat_market());
        viewHolder.lng_market.setText(android.get(i).getLng_market());
        if(android.get(i).getImages().isEmpty()){
            Picasso.with(context).load(R.drawable.images).placeholder(R.drawable.loading).into(viewHolder.profile);

        }else{
            Picasso.with(context).load(android.get(i).getImages()).placeholder(R.drawable.loading).into(viewHolder.profile);
        }


    }



    @Override
    public int getItemCount() {

        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Typeface customTypeOne = Typeface.createFromAsset(itemView.getContext().getAssets(), "font/jf.ttf");
        private TextView name,id,description,dist,time,lat_user,lng_user,lat_market,lng_market;
        private ImageView profile;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            name = view.findViewById(R.id.name);
            id = view.findViewById(R.id.id);
            description = view.findViewById(R.id.description);
            time = view.findViewById(R.id.time);
            dist = view.findViewById(R.id.dist);
            profile = view.findViewById(R.id.profile);
            lat_user = view.findViewById(R.id.lat_user);
            lng_user = view.findViewById(R.id.lng_user);
            lat_market = view.findViewById(R.id.lat_market);
            lng_market = view.findViewById(R.id.lng_market);

            name.setTypeface(customTypeOne);
            description.setTypeface(customTypeOne);
            dist.setTypeface(customTypeOne);
            time.setTypeface(customTypeOne);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            Intent intent = new Intent(context, OrderSingleActivity.class);
            Bundle bundle = new Bundle();
            String tv_name_pass = ((TextView) view.findViewById(R.id.name)).getText().toString();
            String tv_id_pass = ((TextView) view.findViewById(R.id.id)).getText().toString();
            String tv_lat_user_pass = ((TextView) view.findViewById(R.id.lat_user)).getText().toString();
            String tv_lng_user_pass = ((TextView) view.findViewById(R.id.lng_user)).getText().toString();
            String tv_lat_market_pass = ((TextView) view.findViewById(R.id.lat_market)).getText().toString();
            String tv_lng_market_pass = ((TextView) view.findViewById(R.id.lng_market)).getText().toString();

            bundle.putSerializable("id_item",tv_id_pass);
            bundle.putSerializable("lat_user",tv_lat_user_pass);
            bundle.putSerializable("lng_user",tv_lng_user_pass);
            bundle.putSerializable("lat_market",tv_lat_market_pass);
            bundle.putSerializable("lng_market",tv_lng_market_pass);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }


}