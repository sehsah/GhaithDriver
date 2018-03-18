package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.mahmoudsehsah.ghaithDriver.adapter.DataAdapterGetMessage;
import com.mahmoudsehsah.ghaithDriver.adapter.JSONResponseGetMasseges;
import com.mahmoudsehsah.ghaithDriver.models.ChatList;
import com.mahmoudsehsah.ghaithDriver.models.SendMessage;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;

public class MessageActivity extends AppCompatActivity {

    LinearLayout layout;
    RelativeLayout layout_2;
    Button sendButton;
    EditText messageArea;
    ScrollView scrollView;
    private Toolbar mTopToolbar;
    TextView driver_name_text;
    ImageView driver_photo;
    private ArrayList<ChatList> data;
    private DataAdapterGetMessage adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        sendButton = findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        driver_name_text = findViewById(R.id.driver_name);
        driver_photo = findViewById(R.id.driver_photo);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // change font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);

        String driver_name = getIntent().getStringExtra("client_name");
        String price = getIntent().getStringExtra("price");
        TextView drivername = findViewById(R.id.drivername);
        TextView pricc = findViewById(R.id.price);
        drivername.setText(driver_name);
        pricc.setText(price +"التكلفه :");

        //toolbar
        Toolbar mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        String id = getIntent().getStringExtra("client_id");
        String url = "http://yaqeensa.com/android/ghaith/GetClientInformation?id="+id;
        Log.d("url",url);
        RequestQueue requestQueue= Volley.newRequestQueue(MessageActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        final String driver_telephone =jsonObject1.getString("customers_telephone");
                        ImageView call = findViewById(R.id.call);
                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + driver_telephone));
                                startActivity(intent);
                            }
                        });

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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    SendNewMessagr();
                    messageArea.setText("");
                }
            }
        });
        initViews();

        ImageView bottomSheetButton = findViewById(R.id.img_attachment);
        bottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });
    }

    public void openBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.botton_sheet, null);
        Button sendImg = view.findViewById(R.id.sendImg);
        final Dialog mBottomSheetDialog = new Dialog(MessageActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

    }
    private void SendNewMessagr() {

        SessionManager sessionManager = new SessionManager(MessageActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String driver_id  = user.get(SessionManager.USER_ID);
        String client_id = getIntent().getStringExtra("client_id");
        String message = messageArea.getText().toString();

        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<SendMessage> call = APIRequests.SendMessagee(message, client_id,driver_id,driver_id);
        call.enqueue(new Callback<SendMessage>() {
            @Override
            public void onResponse(Call<SendMessage> call, retrofit2.Response<SendMessage> response) {
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onFailure(Call<SendMessage> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }

        });
    }

    private void initViews(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }

    private void loadJSON() {
        SessionManager sessionManager = new SessionManager(MessageActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String uid = user.get(SessionManager.USER_ID);
        String client_id = getIntent().getStringExtra("client_id");
        String id_order = getIntent().getStringExtra("id_order");
        Log.d("id_order",id_order);
        Log.d("client_id",client_id);
        Log.d("id_driver",uid);
        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<JSONResponseGetMasseges> call = APIRequests.getJSONMasseges(client_id,uid,id_order);
        call.enqueue(new Callback<JSONResponseGetMasseges>() {
            @Override
            public void onResponse(Call<JSONResponseGetMasseges> call, retrofit2.Response<JSONResponseGetMasseges> response) {
                JSONResponseGetMasseges JSONResponseGetMasseges = response.body();
                data = new ArrayList<>(Arrays.asList(JSONResponseGetMasseges.getAndroid()));
                adapter = new DataAdapterGetMessage(getApplicationContext(),data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponseGetMasseges> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MessageActivity.this, OrdersActivity.class));
        finish();

    }

}
