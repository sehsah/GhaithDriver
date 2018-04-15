package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.Server.ConnectionReceiver;
import com.mahmoudsehsah.ghaithDriver.Server.FCMRegistrationService;
import com.mahmoudsehsah.ghaithDriver.Server.WebService;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.adapter.DirectionsJSONParser;
import com.mahmoudsehsah.ghaithDriver.custom.CustomTypefaceSpan;
import com.mahmoudsehsah.ghaithDriver.custom.GPSTracker;
import com.mahmoudsehsah.ghaithDriver.custom.MyApplication;
import com.mahmoudsehsah.ghaithDriver.models.AcceptTrips;
import com.mahmoudsehsah.ghaithDriver.models.CancelTrip;
import com.mahmoudsehsah.ghaithDriver.models.ChatList;
import com.mahmoudsehsah.ghaithDriver.models.FinishTrip;
import com.mahmoudsehsah.ghaithDriver.models.NewTripe;
import com.mahmoudsehsah.ghaithDriver.models.RejectTrips;
import com.mahmoudsehsah.ghaithDriver.models.UpdateSatus;
import com.mahmoudsehsah.ghaithDriver.models.updateLocation;
import com.mahmoudsehsah.ghaithDriver.models.updateUserId;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeActivity extends AppCompatActivity implements  OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener, NavigationView.OnNavigationItemSelectedListener {

    GPSTracker gps;
    private GoogleApiClient mGoogleApiClient;
    //maps main
    private GoogleMap mMap;
    // Your Location
    private Location mLastLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //Change Location
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_DISTANCE = 10.0f;
    private Double currentLatitude;
    private Double currentLongitude;
    static double latitude ,longitude,workLatitude, workLongitude;
    Button btnFindMe;
    Marker mcurrent;
    private LocationManager locationManager;

    private String customerId = "", destination;
    private LatLng destinationLatLng, pickupLatLng;
    private float rideDistance;
    Button goOffilne , goOnline;
    boolean doubleBackToExitPressedOnce = false;
    boolean isFirstTime = true;
    static double Mylatitude ,Mylongitude;

    private List<NewTripe> tripes;
    private BroadcastReceiver messageReceiver;
    private BroadcastReceiver BillReceiver,cancelReceiver;
    LinearLayout ClientInfo;

    ///
    TextView pickup_location;
    TextView drop_location;
    TextView time_arrival;
    LinearLayout mapBox;
    LinearLayout mapOff;
    //
    Button accepted_requests,cancceled_requests,end_trip_button,start_trip_button,arrive_to_client,cancel_trip;
    TextView lng ,id ,lat,lng_user,lat_user,id_user,price,id_trip;
    ImageView directionToClient,directionToPlace;
    int received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
         setContentView(R.layout.activity_home);
        checkConnection();

        startService(new Intent(this, FCMRegistrationService.class));

            Calligrapher calligrapher = new Calligrapher(this);
            calligrapher.setFont(this, "font/jf.ttf", true);
            //font awesome
            Typeface font = Typeface.createFromAsset(getAssets(), "font/fontawesome-webfont.ttf");
            TextView icon_location = findViewById(R.id.icon_locationn);
            icon_location.setTypeface(font);
            TextView icon_location1 = findViewById(R.id.icon_location1);
            icon_location1.setTypeface(font);
            TextView time = findViewById(R.id.time);
            time.setTypeface(font);
            TextView icon_money = findViewById(R.id.icon_money);
            icon_money.setTypeface(font);
            /////
            final RelativeLayout header = findViewById(R.id.header);
            pickup_location = findViewById(R.id.pickup_location);
            drop_location = findViewById(R.id.drop_location);
            mapBox = findViewById(R.id.mapBox);
            mapOff = findViewById(R.id.mapOff);
            accepted_requests = findViewById(R.id.accepted_requests);
            cancceled_requests = findViewById(R.id.cancceled_requests);
            id_user = findViewById(R.id.id_user);
            lat_user = findViewById(R.id.lat_user);
            lng_user = findViewById(R.id.lng_user);
            lat = findViewById(R.id.lat);
            lng = findViewById(R.id.lng);
            id = findViewById(R.id.id);
            price = findViewById(R.id.price);
            arrive_to_client= findViewById(R.id.arrive_to_client);
            directionToClient = findViewById(R.id.directionToClient);
            directionToPlace = findViewById(R.id.directionToPlace);
            start_trip_button = findViewById(R.id.start_trip);
            end_trip_button = findViewById(R.id.end_trip);
            ClientInfo =  findViewById(R.id.ClientInfo);
            cancel_trip = findViewById(R.id.cancel_trip);
             received = 1;
           final MediaPlayer mp = MediaPlayer.create(this, R.raw.notification);

            messageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // get trip sent from broadcast
                    NewTripe trip = intent.getParcelableExtra("tripe");
                    // check if trip is null
                    if (trip != null) {
                        mp.start();
                        ShowDirection(trip.getLat_user(), trip.getLng_user(), trip.getLat(), trip.getLng());
                        header.setVisibility(View.VISIBLE);
                        Log.e("tripe id_user", trip.getId_user());
                        Log.e("tripe price", trip.getPrice());
                        Log.e("tripe id order", trip.getId());

                        pickup_location.setText(trip.getPickup_location());
                        drop_location.setText(trip.getDrop_location());
                        id_user.setText(trip.getId_user());
                        String LatUser = String.valueOf(trip.getLat_user());
                        lat_user.setText(LatUser);
                        String LngUser = String.valueOf(trip.getLng_user());
                        lng_user.setText(LngUser);
                        String LngPlace = String.valueOf(trip.getLng());
                        lng.setText(LngPlace);
                        String LatPlace = String.valueOf(trip.getLat());
                        lat.setText(LatPlace);
                        id.setText(trip.getId());
                        price.setText(trip.getPrice());
                        Log.e("hi", "new trip");
                        if (received == 0){
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    startActivity(getIntent());
                                }
                            }, 30000);
                        }

                    }


                }
            };

            cancelReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                    final View subView = inflater.inflate(R.layout.dialog_cancel_trip, null);
                    final TextView txt1 = subView.findViewById(R.id.txt1);
                    final Button btn_home = subView.findViewById(R.id.btn_home);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    Typeface face=Typeface.createFromAsset(getAssets(),"font/jf.ttf");
                    txt1.setTypeface(face);
                    btn_home.setTypeface(face);
                    builder.setView(subView);
                    btn_home.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();
                    alertDialog.setCancelable(false);
                    alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        }
                    });

                }
            };

            final Handler handler = new Handler();
            final int delay = 10000; //milliseconds

            handler.postDelayed(new Runnable() {
                public void run() {

                    gps = new GPSTracker(HomeActivity.this);
                    if (gps.canGetLocation()) {
                        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();
                        }
                    }
                   // LatLng latLng = new LatLng(latitude, longitude);
                   // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    //mMap.animateCamera(cameraUpdate);

                    SessionManager sessionManager = new SessionManager(HomeActivity.this);
                    HashMap<String, String> user = sessionManager.getUserDetails();
                    String id_user = user.get(SessionManager.USER_ID);
                    APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
                    Call<updateLocation> call = APIRequests.updateLocation(id_user, latitude, longitude);
                    call.enqueue(new Callback<updateLocation>() {
                        @Override
                        public void onResponse(Call<updateLocation> call, retrofit2.Response<updateLocation> response) {
                            Log.d("suceess", "suceess");
                        }

                        @Override
                        public void onFailure(Call<updateLocation> call, Throwable t) {
                            Log.d("Error", "Error");
                        }

                    });

                    handler.postDelayed(this, delay);
                }
            }, delay);


            Toolbar mTopToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(mTopToolbar);
            getSupportActionBar().setTitle(null);
            mTopToolbar.setNavigationIcon(R.drawable.ic_reorder_white_24dp);

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
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);

                //for aapplying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }
                }

                //the method we have create in activity
                applyFontToMenuItem(mi);
            }
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            SessionManager sessionManager = new SessionManager(HomeActivity.this);
            HashMap<String, String> user = sessionManager.getUserDetails();
            String uid = user.get(SessionManager.USER_ID);
            String userName = user.get(SessionManager.KEY_NAME);
            String userphoto = user.get(SessionManager.AVATAR);

            Log.d("id_user", uid + " - " + userName + "- " + userphoto);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            View mapView = mapFragment.getView();
            mapFragment.getMapAsync(this);
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            rlp.setMargins(0, 180, 10, 0);


            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
                //**************************
                builder.setAlwaysShow(true); //this is the key ingredient
                //**************************

                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied. The client can initialize location
                                // requests here.
                                Log.d("open", "open");
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(HomeActivity.this, 1000);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                });
            }

            goOffilne = findViewById(R.id.goOffilne);
            goOffilne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goOffilne.setVisibility(View.GONE);
                    goOnline.setVisibility(View.VISIBLE);
                    StopLocationUpdate();
                    GoOffilne();
                }
            });

            goOnline = findViewById(R.id.goOnline);
            goOnline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goOnline.setVisibility(View.GONE);
                    goOffilne.setVisibility(View.VISIBLE);
                    setUpMap();
                    GoOnilne();
                }
            });

            final LinearLayout start_trip_box2 = findViewById(R.id.start_trip_box);
            final RelativeLayout end_trip_box = findViewById(R.id.end_trip_box);
            final LinearLayout arrive_to_client_box = findViewById(R.id.arrive_to_client_box);
            final LinearLayout header2 = findViewById(R.id.header2);
            final RelativeLayout box_client_info1 =  findViewById(R.id.box_client_info1);

            accepted_requests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    received = 1;
                    header2.setVisibility(View.GONE);
                    start_trip_box2.setVisibility(View.VISIBLE);
                    ClientInfo.setVisibility(View.VISIBLE);
                    box_client_info1.setVisibility(View.VISIBLE);
                    Log.e("accepted_requests", "accepted_requests");
                    Log.e("id_user", id_user.getText().toString());
                    Log.e("lang user", lng_user.getText().toString());
                    Log.e("id trip", id.getText().toString());
                    ShowInformationClient(id_user.getText().toString());
                    AcceptedRequest();
                }
            });

            start_trip_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    start_trip_box2.setVisibility(View.GONE);
                    arrive_to_client_box.setVisibility(View.VISIBLE);

                }
            });

            directionToClient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double latUser = Double.parseDouble(lat_user.getText().toString());
                    double lngUser = Double.parseDouble(lat_user.getText().toString());

                    DirectionToClient(latUser,lngUser);
                }
            });

            cancel_trip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CancelTrip();
                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));

                }
            });
            directionToPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double lat_place = Double.parseDouble(lat.getText().toString());
                    double lng_place = Double.parseDouble(lng.getText().toString());
                    DirectionToPlace(lat_place,lng_place);
                }
            });
            arrive_to_client.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arrive_to_client_box.setVisibility(View.GONE);
                    end_trip_box.setVisibility(View.VISIBLE);

                }
            });
            end_trip_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String IdUser = id_user.getText().toString();
                    String prcieVal = price.getText().toString();
                    String idTrip = id.getText().toString();
                    Log.e("dataa2", idTrip);
                    FinishTripNow(IdUser, prcieVal, idTrip);
                    ShowAlertDialogBill(prcieVal);
                }
            });

            cancceled_requests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CancceledRequest();
                    finish();
                    startActivity(getIntent());
                }
            });
        
    }

    private void CancelTrip() {
        String idUser = id_user.getText().toString();
        String idTrip = id.getText().toString();
        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<CancelTrip> call =APIRequests.CancelTrip(idUser, idTrip);
        call.enqueue(new Callback<CancelTrip>() {
            @Override
            public void onResponse(Call<CancelTrip> call, retrofit2.Response<CancelTrip> response) {
            }

            @Override
            public void onFailure(Call<CancelTrip> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }

        });
    }

    private void DirectionToClient(double lat_user, double lng_user) {

        gps = new GPSTracker(HomeActivity.this);
        if (gps.canGetLocation()) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Mylatitude = gps.getLatitude();
                Mylongitude = gps.getLongitude();
                Log.d("my location", String.valueOf(new LatLng(Mylatitude,Mylongitude)));
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW,

                Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
                        "&saddr="+Mylatitude+
                        ","+Mylongitude+"&daddr="+
                        lat_user+
                        ","+lng_user+
                        "&hl=zh&t=m&dirflg=d"));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        startActivityForResult(intent, 1);
    }

    private void DirectionToPlace(double lat_user, double lng_user) {

        gps = new GPSTracker(HomeActivity.this);
        if (gps.canGetLocation()) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Mylatitude = gps.getLatitude();
                Mylongitude = gps.getLongitude();
                Log.d("my location", String.valueOf(new LatLng(Mylatitude,Mylongitude)));
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW,

                Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
                        "&saddr="+Mylatitude+
                        ","+Mylongitude+"&daddr="+
                        lat_user+
                        ","+lng_user+
                        "&hl=zh&t=m&dirflg=d"));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        startActivityForResult(intent, 1);
    }

    private void ShowInformationClient(String customerId) {

        String url = "http://ghaithksa.com/admin/GetClientInformation?id="+customerId;
        Log.d("url",url);
        RequestQueue requestQueue= Volley.newRequestQueue(HomeActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        final String customers_telephone_v = jsonObject1.getString("customers_telephone");
                        final String customers_photo_v = jsonObject1.getString("customers_photo");
                        final String customers_username_v = jsonObject1.getString("customers_username");
                        TextView customers_telephone = findViewById(R.id.customers_telephone);
                        customers_telephone.setText(customers_telephone_v);
                        TextView customers_username = findViewById(R.id.customers_username);
                        customers_username.setText(customers_username_v);
                        ImageView customers_photo = findViewById(R.id.customers_photo);
                        Picasso.with(HomeActivity.this).load(customers_photo_v).placeholder(R.drawable.loading).into(customers_photo);

                        ImageView call = findViewById(R.id.call);
                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + customers_telephone_v));
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
    }


    private void checkConnection() {
        boolean isConnected = ConnectionReceiver.isConnected();
        if(!isConnected) {
            Toast.makeText(HomeActivity.this,"لا يوجد اتصال بالانترنت",Toast.LENGTH_LONG).show();
        }
    }

    private void ShowAlertDialogBill(String prcieVal) {
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View subView = inflater.inflate(R.layout.dialog_layout_bill, null);
        final TextView price = (TextView)subView.findViewById(R.id.price);
        final TextView pric1 = (TextView)subView.findViewById(R.id.price1);
        price.setText(prcieVal);
        final TextView txt1 = subView.findViewById(R.id.txt1);
        final Button btn_home = subView.findViewById(R.id.btn_home);
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        Typeface face=Typeface.createFromAsset(getAssets(),"font/jf.ttf");
        price.setTypeface(face);
        pric1.setTypeface(face);
        txt1.setTypeface(face);
        btn_home.setTypeface(face);
        builder.setView(subView);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(HomeActivity.this,HomeActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.finish();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
        });
//            f

    }

    private void FinishTripNow( String idUser, String prcieVal, String idTrip) {
        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        SessionManager sessionManager = new SessionManager(HomeActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String id_driver = user.get(SessionManager.USER_ID);
        Log.e("dataa",idUser+"-"+prcieVal+"="+idTrip);
        Call<FinishTrip> call =APIRequests.FinishTrip(idUser,idTrip,prcieVal, id_driver);
        call.enqueue(new Callback<FinishTrip>() {
            @Override
            public void onResponse(Call<FinishTrip> call, retrofit2.Response<FinishTrip> response) {

            }

            @Override
            public void onFailure(Call<FinishTrip> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }

        });
    }

    private void ShowDirection(double lat_user, double lng_user, double lat, double lng) {
        gps = new GPSTracker(HomeActivity.this);
        if (gps.canGetLocation()) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Mylatitude = gps.getLatitude();
                Mylongitude = gps.getLongitude();
                Log.d("my location", String.valueOf(new LatLng(Mylatitude,Mylongitude)));
            }
        }
        Log.d("Single my location", String.valueOf(new LatLng(Mylatitude,Mylongitude)));

        LatLng me = new LatLng(Mylatitude, Mylongitude);
        LatLng client = new LatLng(lat_user, lng_user);
        LatLng market = new LatLng(lat, lng);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(me);
        builder.include(client);
        builder.include(market);
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 9);
        mMap.animateCamera(cu);

        mMap.addMarker(new MarkerOptions().position(client).title("موقع العميل").icon(BitmapDescriptorFactory.fromResource(R.drawable.drop)));
        mMap.addMarker(new MarkerOptions().position(market).title("موقع الوصول").icon(BitmapDescriptorFactory.fromResource(R.drawable.place_market)));

        String url = getDirectionsUrl(me, market);
        HomeActivity.DownloadTask downloadTask = new HomeActivity.DownloadTask();
        downloadTask.execute(url);
        Log.d("client", String.valueOf(new LatLng(lat_user,lng_user)));

        String url2 = getDirectionsUrl(market, client);
        HomeActivity.DownloadTask downloadTask2 = new HomeActivity.DownloadTask();
        downloadTask2.execute(url2);
        Log.e("ShowDirection  ", "true");


    }

    private void CancceledRequest() {

        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<RejectTrips> call =APIRequests.RejectTrips("12","1");
        call.enqueue(new Callback<RejectTrips>() {
            @Override
            public void onResponse(Call<RejectTrips> call, retrofit2.Response<RejectTrips> response) {

            }

            @Override
            public void onFailure(Call<RejectTrips> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }

        });
    }

    private void AcceptedRequest() {
        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        String iduser = id_user.getText().toString();
        String idTrip = id.getText().toString();
        SessionManager sessionManager = new SessionManager(HomeActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String idDriver = user.get(SessionManager.USER_ID);
        Log.e("info",iduser+ "-" + idTrip + "-" +idDriver);
        Call<AcceptTrips> call =APIRequests.AcceptTrips(iduser,idTrip,idDriver);
        call.enqueue(new Callback<AcceptTrips>() {
            @Override
            public void onResponse(Call<AcceptTrips> call, retrofit2.Response<AcceptTrips> response) {
                Log.d("accepted","true" );

            }

            @Override
            public void onFailure(Call<AcceptTrips> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }

        });
    }

    private void GoOnilne() {
        mapBox.setVisibility(View.VISIBLE);
        mapOff.setVisibility(View.GONE);
        SessionManager sessionManager = new SessionManager(HomeActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String id_driver= user.get(SessionManager.USER_ID);
        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<UpdateSatus> call =APIRequests.UpdateSatus(id_driver,"1");
        call.enqueue(new Callback<UpdateSatus>() {
            @Override
            public void onResponse(Call<UpdateSatus> call, retrofit2.Response<UpdateSatus> response) {
                Log.d("suceess", "Update Status");
            }

            @Override
            public void onFailure(Call<UpdateSatus> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Log.d("Error", "Update Status");
            }

        });
    }

    private void GoOffilne() {
        mapBox.setVisibility(View.GONE);
        mapOff.setVisibility(View.VISIBLE);
        SessionManager sessionManager = new SessionManager(HomeActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String id_driver= user.get(SessionManager.USER_ID);
        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<UpdateSatus> call = APIRequests.UpdateSatus(id_driver,"0");
        call.enqueue(new Callback<UpdateSatus>() {
            @Override
            public void onResponse(Call<UpdateSatus> call, retrofit2.Response<UpdateSatus> response) {
                Log.d("suceess", "Update Status");
            }

            @Override
            public void onFailure(Call<UpdateSatus> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Log.d("Error", "Update Status");
            }

        });
    }

    private void StopLocationUpdate() {
        mapBox.setVisibility(View.GONE);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        gps = new GPSTracker(HomeActivity.this);
        if (gps.canGetLocation()) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }
        }
        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("موقعك  الحالي").icon(BitmapDescriptorFactory.fromResource(R.drawable.pic)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);


    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);


        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            // 3
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            // 4
            if (mLastLocation != null) {
               // LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
               // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent mainIntent = new Intent(HomeActivity.this,HomeActivity.class);
            HomeActivity.this.startActivity(mainIntent);
            HomeActivity.this.finish();

        } else if (id == R.id.nav_trip) {
            Intent mainIntent = new Intent(HomeActivity.this,HomeActivity.class);
            HomeActivity.this.startActivity(mainIntent);
            HomeActivity.this.finish();

        } else if (id == R.id.nav_settings) {
            Intent mainIntent = new Intent(HomeActivity.this,MySettingsActivity.class);
            HomeActivity.this.startActivity(mainIntent);
            HomeActivity.this.finish();

        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SessionManager sessionManagerr = new SessionManager(HomeActivity.this);
        sessionManagerr.logoutUser();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "font/jf.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageReceiver, new IntentFilter("ShowTripActivity"));
        registerReceiver(cancelReceiver, new IntentFilter("CancelTrip"));


        Log.e("onResume","now");
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(messageReceiver);
        unregisterReceiver(cancelReceiver);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }

    /** A method to download json data from url */
    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            HomeActivity.ParserTask parserTask = new HomeActivity.ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<LatLng>();
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.width(10);
            lineOptions.color(getResources().getColor(R.color.current_lolcation));
            MarkerOptions markerOptions = new MarkerOptions();
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);

            }
            // Drawing polyline in the Google Map for the i-th route
            if(points.size()!=0)mMap.addPolyline(lineOptions);//to avoid crash
        }
    }


    // Private class isNetworkAvailable
    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
