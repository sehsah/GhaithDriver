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
import android.net.Uri;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.acitivities.OrdersActivity;
import com.mahmoudsehsah.ghaithDriver.acitivities.ShowOrderActivity;
import com.mahmoudsehsah.ghaithDriver.models.MyNotfcation;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void onBindViewHolder(final DataAdapterGetNotifcation.ViewHolder viewHolder, int i) {
        viewHolder.text.setText(android.get(i).gettext());
        viewHolder.id_user.setText(android.get(i).getid_user());
        viewHolder.name_user.setText(android.get(i).getname_user());
        viewHolder.id.setText(android.get(i).getid_order());
        viewHolder.id_market.setText(android.get(i).getid_market());
        viewHolder.name_market.setText(android.get(i).getname_market());

        String url = "http://yaqeensa.com/android/ghaith/GetClientInformation?id="+android.get(i).getid_user();
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        final String customers_photo =jsonObject1.getString("customers_photo");
                        Picasso.with(context).load(customers_photo).placeholder(R.drawable.loading).into(viewHolder.photo_user);


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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Typeface customTypeOne = Typeface.createFromAsset(itemView.getContext().getAssets(), "font/jf.ttf");
        private TextView text,id_user,name_user,id,id_market,name_market;
        public ImageView photo_user;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            text = view.findViewById(R.id.text);
            photo_user = view.findViewById(R.id.photo_user);
            name_user = view.findViewById(R.id.name_user);
            id = view.findViewById(R.id.id);
            id_user =  view.findViewById(R.id.id_user);
            id_market =  view.findViewById(R.id.id_market);
            name_market =  view.findViewById(R.id.name_market);
            text.setTypeface(customTypeOne);

            String tv_id_market = ((TextView) view.findViewById(R.id.id_market)).getText().toString();
            String tv_name_market = ((TextView) view.findViewById(R.id.name_market)).getText().toString();
            Log.e("data to rder",tv_id_market+tv_name_market);
        }

        @Override
        public void onClick(View view) {

            Context context = view.getContext();
            Intent intent = new Intent(context, OrdersActivity.class);
            Bundle bundle = new Bundle();
            String idMarket = ((TextView) view.findViewById(R.id.id_market)).getText().toString();
            String tv_name_market = ((TextView) view.findViewById(R.id.name_market)).getText().toString();
            bundle.putSerializable("idMarket",idMarket);
            bundle.putSerializable("name",tv_name_market);
            Log.e("data to idMarket",idMarket);
            intent.putExtras(bundle);
            context.startActivity(intent);

        }

    }


}