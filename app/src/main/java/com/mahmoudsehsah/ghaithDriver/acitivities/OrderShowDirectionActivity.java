package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.adapter.DirectionsJSONParser;
import com.mahmoudsehsah.ghaithDriver.custom.GPSTracker;
import com.mahmoudsehsah.ghaithDriver.models.RegisterNewMarket;
import com.mahmoudsehsah.ghaithDriver.models.SendMessage;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;


public class OrderShowDirectionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Button send;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    GPSTracker gps;
    private Double currentLatitude;
    private Double currentLongitude;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    static double latitude;
    static double longitude;
    static double lat, lng;
    private Location mLastLocation;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    TextView toolbar_title;
    private String finalresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_show_direction);

        // change font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);

        //toolbar
        Toolbar mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        lng = Double.parseDouble(getIntent().getStringExtra("lng"));
        final String name = getIntent().getStringExtra("name");
        final String km = getIntent().getStringExtra("km");
        final String idMarket = getIntent().getStringExtra("idMarket");
        Log.e("idMarket",idMarket);

        SessionManager sessionManager = new SessionManager(OrderShowDirectionActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String driver_id  = user.get(SessionManager.USER_ID);
        final TextView countDiver = findViewById(R.id.countDiver);


        TextView registerNow = findViewById(R.id.registerNow);
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
                Call<RegisterNewMarket> call = APIRequests.RegisterNewMarket(driver_id,idMarket);
                call.enqueue(new Callback<RegisterNewMarket>() {
                    @Override
                    public void onResponse(Call<RegisterNewMarket> call, retrofit2.Response<RegisterNewMarket> response) {

//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                                OrderShowDirectionActivity.this);
//
//                        // set title
//                        alertDialogBuilder.setTitle("تمت العمليه بنجاح");
//                        // set dialog message
//                        alertDialogBuilder
//                                .setMessage("تم تسجيلك كمندوب بنجاح سيتم ابلاغك حين يتم طلب جديد")
//                                .setCancelable(false)
//                                .setPositiveButton("موافق",new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//
//                                    }
//                                });
//
//                        // create alert dialog
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        // show it
//                        alertDialog.show();
                        LayoutInflater inflater = LayoutInflater.from(OrderShowDirectionActivity.this);
                        View subView = inflater.inflate(R.layout.dialog_subscrib_done, null);
                        final TextView text_dialog = (TextView)subView.findViewById(R.id.text_dialog);
                        final TextView btn_dialog = (TextView)subView.findViewById(R.id.btn_dialog);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(OrderShowDirectionActivity.this);
                        Typeface face=Typeface.createFromAsset(getAssets(),"font/jf.ttf");
                        text_dialog.setTypeface(face);
                        btn_dialog.setTypeface(face);
                        builder.setView(subView);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();
                        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            }
                        });
                        btn_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                        int s = countDiver.getText().length();
                        int s1 =  s + 1;
                        countDiver.setText("" + s1);
                    }

                    @Override
                    public void onFailure(Call<RegisterNewMarket> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                        Toast.makeText(OrderShowDirectionActivity.this,"انت بالفعل مندوب",Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(name);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
                            Log.d("open","open");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(OrderShowDirectionActivity.this, 1000);
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
        //draw path from 2 points
        gps = new GPSTracker(OrderShowDirectionActivity.this);
        if (gps.canGetLocation()) {
            if (ContextCompat.checkSelfPermission(OrderShowDirectionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                Log.d("your location", String.valueOf(new LatLng(latitude,longitude)));
            }
        }
        LatLng origin = new LatLng(latitude, longitude);
        LatLng dest = new LatLng(lat, lng);
        Log.d("market location", String.valueOf(new LatLng(lat,lng)));
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);

        // click button
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), OrdersActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("idMarket",idMarket);
                startActivity(intent);
            }
        });

        new GetJsonData().execute();

    }


    private class GetJsonData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            final String idMarket = getIntent().getStringExtra("idMarket");
            String getUrl = "http://www.yaqeensa.com/android/ghaith/getCountRegister?id_market=" + idMarket;
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                url = new URL(getUrl);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

                StringBuffer response = new StringBuffer();
                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader inurl = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    String inputLine;
                    while ((inputLine = inurl.readLine()) != null) {
                        response.append(inputLine);

                    }
                    inurl.close();

                } else {

                    Log.i("test", "POST request not worked.");
                }

                finalresult = response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                parseJson(finalresult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void parseJson(String json) throws JSONException {

            JSONObject jObj = new JSONObject(json);
            String count = jObj.getString("count");
            Log.e("count",count);
            TextView countDiver = findViewById(R.id.countDiver);
            countDiver.setText(count);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(20, 20, 20, 20);

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        //Log.v(String.valueOf(MapsActivity.this),connectionResult.toString());
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
                final String name = getIntent().getStringExtra("name");
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                LatLng origin = new LatLng(latitude, longitude);
                LatLng dest = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(origin.latitude, origin.longitude))
                        .title("موقعك")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop))
                );
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(dest.latitude, dest.longitude))
                        .title(name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.place_market))
                );
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(dest);
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                mMap.animateCamera(cu);
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 11));
            }
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

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
    private String downloadUrl(String strUrl) throws IOException {
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
    private class DownloadTask extends AsyncTask<String, Void, String> {

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
            ParserTask parserTask = new ParserTask();
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
            lineOptions.color(getResources().getColor(R.color.yellow));
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




    @Override
    protected void onStart() {
        super.onStart();
        // 2
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 3
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }



}