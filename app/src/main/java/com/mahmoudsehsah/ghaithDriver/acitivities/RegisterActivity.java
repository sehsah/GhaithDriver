package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.custom.CustomTypefaceSpan;
import com.mahmoudsehsah.ghaithDriver.models.Register;
import com.mahmoudsehsah.ghaithDriver.custom.CheckConnection;
import com.mahmoudsehsah.ghaithDriver.models.User;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 7/3/17.
 */

public class RegisterActivity extends ActivityManagePermission implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    String permissionAsk[] = {PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE, PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_ACCESS_FINE_LOCATION, PermissionUtils.Manifest_ACCESS_COARSE_LOCATION};
    RelativeLayout relative_signin;
    EditText driver_password, input_confirmPassword, driver_telephone, driver_username;
    AppCompatButton sign_up;
    SwipeRefreshLayout swipeRefreshLayout;
    SessionManager sessionManager;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Double currentLatitude;
    private Double currentLongitude;
    private View rootView;
    Typeface tfavv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        final SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
        }

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);

        BindView();

        AskPermission();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                        register();
                    } else {
                        Toast.makeText(RegisterActivity.this, "network is not available", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // do nothing
                }
            }
        });
        final String type = getIntent().getStringExtra("type");
        Log.e("type",type);
    }

    public Boolean validate() {
        Boolean value = true;

        if (driver_username.getText().toString().trim().equals("")) {
            driver_username.setError("هذا الحقل مطلوب");
            value = false;
        } else {
            driver_username.setError(null);
        }
        if (driver_telephone.getText().toString().trim().equals("")) {
            driver_telephone.setError(getString(R.string.mobile_invalid));
            value = false;
        } else {
            driver_telephone.setError(null);
        }
        if (!(driver_password.length() >= 6)) {
            value = false;
            driver_password.setError("كلمه المرور لا تقل عن 6 احرف او ارقام");
        } else {
            driver_password.setError(null);
        }
        if (!driver_password.getText().toString().trim().equals("") && (!input_confirmPassword.getText().toString().trim().equals(driver_password.getText().toString().trim()))) {
            value = false;
            input_confirmPassword.setError("كلمه المرور غير متطابقين");
        } else {
            input_confirmPassword.setError(null);
        }
        return value;
    }

    @SuppressLint("WrongViewCast")
    public void BindView() {
        driver_password =  findViewById(R.id.password);
        input_confirmPassword =  findViewById(R.id.password2);
        driver_username = (EditText)findViewById(R.id.username);
        driver_telephone =  findViewById(R.id.phone);
        sign_up =  findViewById(R.id.sign_up);
        sessionManager = new SessionManager(getApplicationContext());
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void register() {

        //final ProgressDialog loading = ProgressDialog.show(this,"جاري تحميل البيانات","من فضلك انتظر",false,false);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.create();
        SpannableString ss=  new SpannableString("جاري  التسجيل ");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/jf.ttf");
        ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", typeFace), 0, ss.length(), 0);
        dialog.setMessage(ss);
        dialog.show();
        //////////////////

        final String driver_telephone_val = driver_telephone.getText().toString().trim();
        final String driver_password_val = driver_password.getText().toString().trim();
        final String driver_username_val = driver_username.getText().toString().trim();

        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        final String type = getIntent().getStringExtra("type");
        Log.e("type",type);

        Call<Register> call = APIRequests.register(type,driver_telephone_val, driver_password_val,driver_username_val);
        call.enqueue(new Callback<Register>() {

            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                dialog.dismiss();
                if(response.body().getSuccess().equalsIgnoreCase("1")){

                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("getMessage", response.body().getMessage());
                    User userDetails = response.body().getData().get(0);
                    String user_id = userDetails.getDriverId();
                    String type  = userDetails.getType();
                    Intent intent = new Intent(RegisterActivity.this, RegisterFinishActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user_id",user_id);
                    bundle.putSerializable("type",type);
                    intent.putExtras(bundle);
                    startActivity(intent);



                }else if(response.body().getSuccess().equalsIgnoreCase("0")){
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("getMessage", response.body().getMessage());

                }else{
                    Toast.makeText(RegisterActivity.this, "خطا الاتصال بالسيرفر", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                dialog.dismiss();
                Log.d("Error",t.getMessage());
            }

        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        try {
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
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
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
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                //Toast.makeText(getActivity(), currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

    public void getCurrentlOcation() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
    }

    public void tunonGps() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(30 * 1000);
            mLocationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            getCurrentlOcation();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and setting the result in onActivityResult().
                                status.startResolutionForResult(RegisterActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }



    public void AskPermission() {
        askCompactPermissions(permissionAsk, new PermissionResult() {
            @Override
            public void permissionGranted() {
                if (!GPSEnable()) {
                    tunonGps();
                } else {
                    getCurrentlOcation();
                }

            }

            @Override
            public void permissionDenied() {

            }

            @Override
            public void permissionForeverDenied() {
                //  openSettingsApp(getApplicationContext());
            }
        });
    }

    public Boolean GPSEnable() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;

        }
        return false;
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();

    }
}
