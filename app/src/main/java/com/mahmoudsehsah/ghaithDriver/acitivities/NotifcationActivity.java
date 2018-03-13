package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.Toast;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.adapter.DataAdapterGetNotifcation;
import com.mahmoudsehsah.ghaithDriver.adapter.JSONResponseGetNotfcation;
import com.mahmoudsehsah.ghaithDriver.custom.CustomTypefaceSpan;
import com.mahmoudsehsah.ghaithDriver.models.MyNotfcation;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifcationActivity extends AppCompatActivity {

    private Toolbar mTopToolbar;
    private ArrayList<MyNotfcation> data;
    private DataAdapterGetNotifcation adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifcation);

        // change font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);


        //toolbar
        Toolbar mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }
    private void initViews(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void loadJSON(){
        //get pass data
        final String city= getIntent().getStringExtra("category");
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.create();
        SpannableString ss=  new SpannableString("جاري  جلب البيانات");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/jf.ttf");
        ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", typeFace), 0, ss.length(), 0);
        dialog.setMessage(ss);
        dialog.show();
        //////////////////
        SessionManager sessionManager = new SessionManager(NotifcationActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String uid = user.get(SessionManager.USER_ID);


        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<JSONResponseGetNotfcation> call = APIRequests.getJSONNotfcation(uid);
        call.enqueue(new Callback<JSONResponseGetNotfcation>() {
            @Override
            public void onResponse(Call<JSONResponseGetNotfcation> call, Response<JSONResponseGetNotfcation> response) {
                dialog.dismiss();
                JSONResponseGetNotfcation JSONResponseGetNotfcation = response.body();
                data = new ArrayList<>(Arrays.asList(JSONResponseGetNotfcation.getAndroid()));
                adapter = new DataAdapterGetNotifcation(getApplicationContext(),data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponseGetNotfcation> call, Throwable t) {
                Log.d("Error",t.getMessage());
                dialog.dismiss();
                Toast.makeText(NotifcationActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });

    }

}
