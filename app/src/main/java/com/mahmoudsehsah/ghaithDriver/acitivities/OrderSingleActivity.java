package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.mahmoudsehsah.ghaithDriver.models.AddNewOffer;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

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
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSingleActivity extends AppCompatActivity  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private String finalresult;
    static double Mylatitude ,Mylongitude,workLatitude, workLongitude;
    GPSTracker gps;
    private GoogleApiClient mGoogleApiClient;
    //maps main
    private GoogleMap mMap;
    private Location mLastLocation;
     LocationManager mLocationManager;
     LocationListener mLocationListener;
     Double currentLatitude;
     Double currentLongitude;
    static double lat, lng;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_single);
        new GetJsonData().execute();

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Button accepted = findViewById(R.id.accepted);
        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptedOrder();
            }
        });


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
                                status.startResolutionForResult(OrderSingleActivity.this, 1000);
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

        gps = new GPSTracker(OrderSingleActivity.this);
        if (gps.canGetLocation()) {
            if (ContextCompat.checkSelfPermission(OrderSingleActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Mylatitude = gps.getLatitude();
                Mylongitude = gps.getLongitude();
                Log.d("my location", String.valueOf(new LatLng(Mylatitude,Mylongitude)));
            }
        }
        Log.d("Single my location", String.valueOf(new LatLng(Mylatitude,Mylongitude)));

        String id_item = getIntent().getStringExtra("id_item");
        Log.d("id_item",id_item);

        String lat_user1 = getIntent().getStringExtra("lat_user");
        double lat_user = Double.parseDouble(lat_user1);
        String lng_user1 = getIntent().getStringExtra("lng_user");
        double lng_user = Double.parseDouble(lng_user1);
        String lat_market1 = getIntent().getStringExtra("lat_market");
        double lat_market = Double.parseDouble(lat_market1);
        String lng_market1 = getIntent().getStringExtra("lng_market");
        double lng_market = Double.parseDouble(lng_market1);
        Log.d("mapt info",lat_user+"-"+lng_user+"-"+lat_market+"-"+lng_market);

        String description_val = getIntent().getStringExtra("description_val");
        Log.d("description_val",description_val);


        LatLng me = new LatLng(Mylatitude, Mylongitude);
        LatLng client = new LatLng(lat_user, lng_user);
        LatLng market = new LatLng(lat_market, lng_market);
        String url = getDirectionsUrl(me, market);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);

        String url2 = getDirectionsUrl(market, client);
        DownloadTask downloadTask2 = new DownloadTask();
        downloadTask2.execute(url2);
    }

    private void AcceptedOrder() {
        EditText price_driver =  findViewById(R.id.price_driver);
        TextView time =  findViewById(R.id.time);
        String  price_driver_value = price_driver.getText().toString();
        SessionManager sessionManager = new SessionManager(OrderSingleActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String uid = user.get(SessionManager.USER_ID);
        String photo_driver = user.get(SessionManager.AVATAR);
        String name_driver = user.get(SessionManager.KEY_NAME);
        String id_order =  getIntent().getStringExtra("id_item");
        String text =  name_driver+" عرض جديد علي طلبك من السائق ";
        Log.d("text",text);
        Log.d("photo_driver",photo_driver);
        Log.d("name_driver",name_driver);
        TextView id_user =  findViewById(R.id.id_user);
        String id_user_val = id_user.getText().toString();
        String time_val = time.getText().toString();
        String description_val = getIntent().getStringExtra("description_val");
        Log.d("description_val",description_val);
        Log.d("time_val",time_val);
        if (price_driver_value.matches("")){
            Toast.makeText(OrderSingleActivity.this,"يجب ادخال السعر اولا",Toast.LENGTH_SHORT).show();
        }else{
            final AlertDialog dialog = new SpotsDialog(OrderSingleActivity.this,"من فضلك انتظر");
            dialog.show();
            APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
            Call<AddNewOffer> call = APIRequests.AddNewOffer(id_user_val,uid,photo_driver,name_driver,text, price_driver_value,time_val,description_val, id_order);
            call.enqueue(new Callback<AddNewOffer>() {
                @Override
                public void onResponse(Call<AddNewOffer> call, Response<AddNewOffer> response) {
                    Toast.makeText(OrderSingleActivity.this,"تم ارسال العرض بنجاح",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Intent intent = new Intent(getBaseContext(), OrdersActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<AddNewOffer> call, Throwable t) {
                    dialog.dismiss();
                    Log.d("Error",t.getMessage());
                }

            });
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String lat_user1 = getIntent().getStringExtra("lat_user");
        double lat_user = Double.parseDouble(lat_user1);
        String lng_user1 = getIntent().getStringExtra("lng_user");
        double lng_user = Double.parseDouble(lng_user1);
        String lat_market1 = getIntent().getStringExtra("lat_market");
        double lat_market = Double.parseDouble(lat_market1);
        String lng_market1 = getIntent().getStringExtra("lng_market");
        double lng_market = Double.parseDouble(lng_market1);

        LatLng me = new LatLng(Mylatitude, Mylongitude);
        LatLng client = new LatLng(lat_user, lng_user);
        LatLng market = new LatLng(lat_market, lng_market);

        mMap.addMarker(new MarkerOptions().position(me).title("موقعك").icon(BitmapDescriptorFactory.fromResource(R.drawable.pic)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(client.latitude, client.longitude)).title("مكان العميل").icon(BitmapDescriptorFactory.fromResource(R.drawable.drop)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(market.latitude, market.longitude)).title("المتجر").icon(BitmapDescriptorFactory.fromResource(R.drawable.place_market)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
    //    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sy, 15));
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(me);
        builder.include(market);
        //builder.include(client);
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
//        mMap.animateCamera(cu);
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
                LatLng me = new LatLng(Mylatitude, Mylongitude);
                LatLng dest = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(me.latitude, me.longitude))
                        .title("موقعك")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop))
                );
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(dest.latitude, dest.longitude))
                        .title(name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.place_market))
                );
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(me);
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

    private class GetJsonData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            final String id = getIntent().getStringExtra("id_item");
            String getUrl = "http://yaqeensa.com/android/ghaith/orderSingle?id="+id;
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

            JSONArray jArr = new JSONArray(json);

            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);

                String places= obj.getString("places");
                TextView places_Text = findViewById(R.id.places);
                places_Text.setText(places);

                String name= obj.getString("name");
                TextView name_Text = findViewById(R.id.name);
                name_Text.setText(name);

                String time= obj.getString("time");
                TextView time_Text = findViewById(R.id.time);
                time_Text.setText(time);

                String km= obj.getString("km");
                TextView km_Text = findViewById(R.id.km);
                km_Text.setText(km);

                String id_user = obj.getString("id_user");
                TextView id_user_Text = findViewById(R.id.id_user);
                id_user_Text.setText(id_user);

                String lat_market1 = getIntent().getStringExtra("lat_market");
                double lat_market = Double.parseDouble(lat_market1);
                String lng_market1 = getIntent().getStringExtra("lng_market");
                double lng_market = Double.parseDouble(lng_market1);
                double distanceToMarket = distance(Mylatitude,Mylongitude,lat_market,lng_market);
                String distanceToMarketNew = String.format(Locale.ENGLISH, "%.3f", distanceToMarket/1000);
                Log.d("distanceToMarket1", String.valueOf(distanceToMarketNew));
                TextView kmToClient_text = findViewById(R.id.kmToClient);
                kmToClient_text.setText(distanceToMarketNew);

            }
        }


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
        super.onBackPressed();
        startActivity(new Intent(OrderSingleActivity.this, OrdersActivity.class));
        finish();

    }

}
