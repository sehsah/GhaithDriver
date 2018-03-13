package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.Server.Server;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.custom.CustomTypefaceSpan;
import com.mahmoudsehsah.ghaithDriver.models.Login;
import com.mahmoudsehsah.ghaithDriver.custom.CheckConnection;
import com.mahmoudsehsah.ghaithDriver.models.User;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends ActivityManagePermission {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private static final String TAG = "login";
    RelativeLayout relative_register;
    EditText driver_telephone, driver_password;
    Button login;
    SessionManager sessionManager;
    TextView as, txt_createaccount, forgot_password;
    private String token;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);


        final SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
        bindView();
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                        login();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.network), Toast.LENGTH_LONG).show();
                    }

                } else {
                    // do nothing
                }
            }
        });


    }

    public void bindView() {
        context = LoginActivity.this;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        driver_telephone = (EditText) findViewById(R.id.phone);
        driver_password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        sessionManager = new SessionManager(getApplicationContext());
        forgot_password = (TextView) findViewById(R.id.txt_forgotpassword);
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                  //  changepassword_dialog();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.network), Toast.LENGTH_LONG).show();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    public Boolean validate() {
        Boolean value = true;
        if (driver_telephone.getText().toString().trim().equalsIgnoreCase("")) {
            value = false;
            driver_telephone.setError(getString(R.string.fiels_is_required));
        } else {
            driver_telephone.setError(null);
        }

        if (driver_password.getText().toString().trim().equalsIgnoreCase("")) {
            value = false;
            driver_password.setError(getString(R.string.fiels_is_required));
        } else {
            driver_password.setError(null);
        }
        return value;
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void login() {

        //final ProgressDialog loading = ProgressDialog.show(this,"جاري تحميل البيانات","من فضلك انتظر",false,false);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.create();
        SpannableString ss=  new SpannableString("جاري  تسجيل الدخول");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/jf.ttf");
        ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", typeFace), 0, ss.length(), 0);
        dialog.setMessage(ss);
        dialog.show();
        //////////////////

        final String driver_telephone_val = driver_telephone.getText().toString().trim();
        final String driver_password_val = driver_password.getText().toString().trim();

        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<Login> call = APIRequests.login(driver_telephone_val, driver_password_val);
        call.enqueue(new Callback<Login>() {

            @Override
            public void onResponse(Call<Login> call, retrofit2.Response<Login> response) {
                dialog.dismiss();
                Log.d("getSuccess", response.body().getSuccess());
                if(response.body().getSuccess().equalsIgnoreCase("1")){

                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                    User userDetails = response.body().getData().get(0);

                    String name = userDetails.getDriverUsername();
                    String email = "admin@ghaith.com";
                    String user_id = userDetails.getDriverId();
                    String avatar = userDetails.getDriverPhoto();
                    String mobile = userDetails.getDriverTelephone();
                    String type = userDetails.getType();
                    Log.d("type",type);
                    Log.d("user_id",user_id);
                    sessionManager.createLoginSession(name, email, user_id, avatar, mobile);
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if(type == "driver"){
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }else{
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                    }
                }else if(response.body().getSuccess().equalsIgnoreCase("0")){
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("getMessage", response.body().getMessage());
                }else{
                    Toast.makeText(LoginActivity.this, "خطا الاتصال بالسيرفر", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                dialog.dismiss();
                Log.d("Error",t.getMessage());
            }

        });

    }



//    public void changepassword_dialog() {
//        final Dialog dialog = new Dialog(LoginActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.password_reset);
//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.gravity = Gravity.CENTER_HORIZONTAL;
//        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//        final EditText email = (EditText) dialog.findViewById(R.id.input_email);
//        TextView title = (TextView) dialog.findViewById(R.id.title);
//        TextView message = (TextView) dialog.findViewById(R.id.message);
//
//        AppCompatButton btn_change = (AppCompatButton) dialog.findViewById(R.id.btn_reset);
//        AppCompatButton btn_cancel = (AppCompatButton) dialog.findViewById(R.id.btn_cancel);
//
//        Typeface font = Typeface.createFromAsset(getAssets(), "font/AvenirLTStd_Medium.otf");
//        Typeface font1 = Typeface.createFromAsset(getAssets(), "font/AvenirLTStd_Book.otf");
//        btn_change.setTypeface(font1);
//        btn_cancel.setTypeface(font1);
//        email.setTypeface(font);
//        title.setTypeface(font);
//        message.setTypeface(font);
//
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
//
//        btn_change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View view = LoginActivity.this.getCurrentFocus();
//                if (view != null) {
//                    CheckConnection.hideKeyboard(LoginActivity.this, view);
//                }
//                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
//                    //dialog.cancel();
//                    resetPassword(email.getText().toString().trim(), dialog);
//
//                } else {
//                    email.setError(getString(R.string.email_invalid));
//                    // Toast.makeText(LoginActivity.this, "email is invalid", Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        });
//        dialog.show();
//
//    }
}
