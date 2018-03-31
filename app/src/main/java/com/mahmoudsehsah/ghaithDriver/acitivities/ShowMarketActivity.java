package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.FCMRegistrationService;
import com.mahmoudsehsah.ghaithDriver.custom.CustomListViewAdapter;
import com.mahmoudsehsah.ghaithDriver.custom.CustomTypefaceSpan;
import com.mahmoudsehsah.ghaithDriver.custom.GPSTracker;
import com.mahmoudsehsah.ghaithDriver.custom.GetJsonFromUrlTask;
import com.mahmoudsehsah.ghaithDriver.models.Places;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ShowMarketActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mTopToolbar;
    // Declare Variables
    private ListView listview;
    private ArrayList<Places> books;
    private ArrayAdapter<Places> adapter;
    private final static String TAG = OrdersActivity.class.getSimpleName();
    GPSTracker gps;
    int d;
    private float distance ,distance2;
    double latitude,longitude;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_market);

        // change font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);

        startService(new Intent(this,FCMRegistrationService.class));


        //toolbar
        Toolbar mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dehaze_white_24dp, this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });




        NavigationView navigationViewv = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationViewv.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SessionManager sessionManager = new SessionManager(ShowMarketActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String USER_ID  = user.get(SessionManager.USER_ID);
        String KEY_NAME = user.get(SessionManager.KEY_NAME);
        String AVATAR =  user.get(SessionManager.AVATAR);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userName);
        ImageView navphoto =  headerView.findViewById(R.id.photo);
        navUsername.setText(KEY_NAME);
        Picasso.with(ShowMarketActivity.this).load(AVATAR).placeholder(R.drawable.loading).into(navphoto);


        gps = new GPSTracker(ShowMarketActivity.this);
        if (gps.canGetLocation()) {
            if (ContextCompat.checkSelfPermission(ShowMarketActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                Log.d("latitude", String.valueOf(latitude));
                Log.d("longitude", String.valueOf(longitude));
                Log.d(" origin", String.valueOf(new LatLng(latitude,longitude)));

            }
        }

        listview = findViewById(R.id.listview);
        setListViewAdapter();
        getDataFromInternet();

//        final EditText inputSearch = findViewById(R.id.inputSearch);
////        Button searchBtn = findViewById(R.id.searchBtn);
////        searchBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                String searchVal = inputSearch.getText().toString();
////                Log.d("searchVal",searchVal);
////
////            }
////        });


    }

    private void getDataFromInternet() {
        Log.d("latitude", String.valueOf(latitude));
        Log.d("longitude", String.valueOf(longitude));
        String types = "supermarket|cafe|restaurant|electronics_store|shoe_store";
        final String url;
        try {
            String nextStr ="" ;
            url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + String.valueOf(latitude) + ","+String.valueOf(longitude)+"&radius=1500&rankBy=distance&types="+ URLEncoder.encode(types,"UTF-8")+"&sensor=true&key=AIzaSyAt5xYgHIP6G5q9oSWzv67sBjzeobi55eU&"+nextStr;
            new GetJsonFromUrlTask(this, url).execute();
            Log.d("url",url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void setListViewAdapter() {
        books = new ArrayList<Places>();
        adapter = new CustomListViewAdapter(this ,this, R.layout.list_item_places, books);
        listview.setAdapter(adapter);
    }

    //parse response data after asynctask finished
    public void parseJsonResponse(String result) {
        Log.i(TAG, result);
        try {


            JSONObject json = new JSONObject(result);
            JSONArray jArray = new JSONArray(json.getString("results"));
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                Places mgooglePlace = new Places();
                mgooglePlace.setName(jObject.getString("name"));
                mgooglePlace.setIcon(jObject.getString("icon"));
                mgooglePlace.setId(jObject.getString("id"));
                JSONObject geometry = jObject.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");

                Location locationA = new Location("point A");
                locationA.setLatitude(latitude);
                locationA.setLongitude(longitude);
                Location locationB = new Location("point B");
                locationB.setLatitude(Double.parseDouble(location.getString("lat")));
                locationB.setLongitude(Double.parseDouble(location.getString("lng")));
                mgooglePlace.setlat(location.getString("lat"));
                mgooglePlace.setlng(location.getString("lng"));
                Log.d("lat,lng",location.getString("lat") + location.getString("lng"));
                distance = locationA.distanceTo(locationB);
                Log.d("distance", String.valueOf(distance/1000));
                String resulttt = String.format(Locale.ENGLISH, "%.2f", distance/1000);
                mgooglePlace.setDistance(String.valueOf(resulttt));
                books.add(mgooglePlace);

            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "اضفط مره اخري لغلق البرنامج", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent mainIntent = new Intent(ShowMarketActivity.this,OrdersActivity.class);
            ShowMarketActivity.this.startActivity(mainIntent);
            ShowMarketActivity.this.finish();

        } else if (id == R.id.nav_orders) {
            Intent mainIntent = new Intent(ShowMarketActivity.this,MyOrdersActivity.class);
            ShowMarketActivity.this.startActivity(mainIntent);
            ShowMarketActivity.this.finish();

        } else if (id == R.id.nav_settings) {
            Intent mainIntent = new Intent(ShowMarketActivity.this,MySettingsActivity.class);
            ShowMarketActivity.this.startActivity(mainIntent);
            ShowMarketActivity.this.finish();

        } else if (id == R.id.nav_notifcation) {
            Intent mainIntent = new Intent(ShowMarketActivity.this,NotifcationActivity.class);
            ShowMarketActivity.this.startActivity(mainIntent);
            ShowMarketActivity.this.finish();

        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "font/jf.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void logout() {
        SessionManager sessionManagerr = new SessionManager(ShowMarketActivity.this);
        sessionManagerr.logoutUser();
        startActivity(new Intent(ShowMarketActivity.this, LoginActivity.class));
        finish();
    }
}
