package com.mahmoudsehsah.ghaithDriver.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.acitivities.OrderSingleActivity;
import com.mahmoudsehsah.ghaithDriver.acitivities.ShowOrderActivity;
import com.mahmoudsehsah.ghaithDriver.models.GetOrder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by mahmoud on 3/19/2018.
 */

public class DataAdapterGetMyOrders extends RecyclerView.Adapter<DataAdapterGetMyOrders.ViewHolder> {
    private ArrayList<GetOrder> android;
    private Context context;


    public DataAdapterGetMyOrders(Context context, ArrayList<GetOrder> android ) {
        this.android = android;
        this.context = context;


    }

    @Override
    public DataAdapterGetMyOrders.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Calligrapher calligrapher = new Calligrapher(viewGroup.getContext());
        calligrapher.setFont((Activity) viewGroup.getContext(), "font/jf.ttf", true);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.container_get_orders, viewGroup, false);
        return new DataAdapterGetMyOrders.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataAdapterGetMyOrders.ViewHolder viewHolder, int i) {


        viewHolder.name.setText(android.get(i).getName_user());
        viewHolder.description.setText(android.get(i).getDescription());
        viewHolder.dist.setText(android.get(i).getKm());
        viewHolder.id.setText(android.get(i).getId());
        viewHolder.time.setText(android.get(i).getTime());
        viewHolder.lat_user.setText(android.get(i).getLat());
        viewHolder.lng_user.setText(android.get(i).getLng());
        viewHolder.lat_market.setText(android.get(i).getLat_market());
        viewHolder.lng_market.setText(android.get(i).getLng_market());


        int id = Integer.parseInt(android.get(i).getIdUser());
        String url = "http://yaqeensa.com/android/ghaith/GetClientInformation?id="+id;
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String customers_photo =jsonObject1.getString("customers_photo");
                        if(customers_photo.isEmpty()){
                            Picasso.with(context).load(R.drawable.user_default).placeholder(R.drawable.loading).into(viewHolder.profile);
                        }else{
                            Picasso.with(context).load(customers_photo).placeholder(R.drawable.loading).into(viewHolder.profile);
                        }
                    }
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);




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

            Intent intent = new Intent(context, ShowOrderActivity.class);
            Bundle bundle = new Bundle();
            String tv_id_pass = ((TextView) view.findViewById(R.id.id)).getText().toString();
            String tv_lat_user_pass = ((TextView) view.findViewById(R.id.lat_user)).getText().toString();
            String tv_lng_user_pass = ((TextView) view.findViewById(R.id.lng_user)).getText().toString();
            String tv_lat_market_pass = ((TextView) view.findViewById(R.id.lat_market)).getText().toString();
            String tv_lng_market_pass = ((TextView) view.findViewById(R.id.lng_market)).getText().toString();
            String tv_description_val = ((TextView) view.findViewById(R.id.description)).getText().toString();

            bundle.putSerializable("id_item",tv_id_pass);
            bundle.putSerializable("lat_user",tv_lat_user_pass);
            bundle.putSerializable("lng_user",tv_lng_user_pass);
            bundle.putSerializable("lat_market",tv_lat_market_pass);
            bundle.putSerializable("lng_market",tv_lng_market_pass);
            bundle.putSerializable("description_val",tv_description_val);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }


}
