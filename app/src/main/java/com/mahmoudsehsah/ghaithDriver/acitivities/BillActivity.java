package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.models.SendMessage;
import com.mahmoudsehsah.ghaithDriver.models.UpdateBill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;

public class BillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        // change font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final String id_order = getIntent().getStringExtra("id_order");
        Log.e("id_order",id_order);
        String url2 = "http://yaqeensa.com/android/ghaith/ShowOrderInfo?id="+id_order;
        RequestQueue requestQueue2= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest2=new StringRequest(Request.Method.GET, url2, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        final String price2 = jsonObject1.getString("price2");
                        final EditText price2Text = findViewById(R.id.price2);
                        int numm1 = 0;
                        if (price2 != null){
                             numm1 = Integer.parseInt(String.valueOf(price2));
                            price2Text.setText(price2);
                        }else{
                             numm1 = 0;
                        }
                        final String price = jsonObject1.getString("price");
                        Log.e("price",price);
                        final EditText priceText = findViewById(R.id.price);
                        int numm2 = Integer.parseInt(String.valueOf(price));
                        if (price != null) {
                            priceText.setText(price);
                        }else{
                            priceText.setText(0);

                        }
                        TextView sum = findViewById(R.id.sum);
                        sum.setText(String.valueOf(numm1+numm2) +" ريال ");



                    }
                }catch (JSONException e){e.printStackTrace();}
            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout2 = 30000;
        RetryPolicy policy2 = new DefaultRetryPolicy(socketTimeout2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest2.setRetryPolicy(policy2);
        requestQueue2.add(stringRequest2);



        final EditText price = findViewById(R.id.price);
        final EditText price2 = findViewById(R.id.price2);
        final TextView sum = findViewById(R.id.sum);

        price2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                int num1 = 0;
                if(s.length() == 0){
                     num1 = 0;
                }else{
                     num1 = Integer.parseInt(price2.getText().toString());

                }
                 String price_val = price.getText().toString();
                int num2 = 0;
                if(price_val!=null && (!price_val.isEmpty())){
                    num2 = Integer.parseInt(price_val);
                }
                sum.setText(String.valueOf(num1+num2) +" ريال ");
            }
        });


        TextView send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
                TextView sum = findViewById(R.id.sum);
                TextView priceText = findViewById(R.id.price2);
                String sum_val = sum.getText().toString();
                String price2_val = priceText.getText().toString();
                final String price2  = sum_val.replaceAll("[^0-9]", "");

                Call<UpdateBill> call = APIRequests.UpdateBill(price2_val,id_order);
                call.enqueue(new Callback<UpdateBill>() {
                    @Override
                    public void onResponse(Call<UpdateBill> call, retrofit2.Response<UpdateBill> response) {
                        finish();

                    }

                    @Override
                    public void onFailure(Call<UpdateBill> call, Throwable t) {
                        finish();
                    }

                });
            }
        });


    }
}
