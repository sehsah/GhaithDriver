package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.custom.CustomTypefaceSpan;
import com.mahmoudsehsah.ghaithDriver.models.UpdateUnformation;
import com.mahmoudsehsah.ghaithDriver.models.User;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class MySettingsActivity extends AppCompatActivity {

    EditText username,phone ,password ,repassword;
    Button update;
    String imagePath;
    //ImageView img;
    FloatingActionButton user_photo_edit_fab;
    CircleImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);


        // change font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);
        //prevent keyboard open auto when activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //toolbar
        Toolbar mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SessionManager sessionManager = new SessionManager(MySettingsActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String uid = user.get(SessionManager.USER_ID);
        String uname = user.get(SessionManager.KEY_NAME);
        String uphone = user.get(SessionManager.KEY_MOBILE);
        String photo = user.get(SessionManager.AVATAR);

        username =  findViewById(R.id.username);
        username.setText(uname);

        phone = findViewById(R.id.phone);
        phone.setText(uphone);

        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);

        img = findViewById(R.id.user_photo);
        Picasso.with(MySettingsActivity.this).load(photo).placeholder(R.drawable.loading).into(img);
        update =  findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Update();
                } else {
                    // do nothing
                }
            }
        });

        img = findViewById(R.id.user_photo);
        user_photo_edit_fab = findViewById(R.id.user_photo_edit_fab);
        user_photo_edit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_PICK);
                final Intent chooserIntent = Intent.createChooser(galleryIntent, "من فضلك اختار الصورة ");
                startActivityForResult(chooserIntent, 100);
            }
        });

    }


    public Boolean validate() {
        Boolean value = true;

        if (!repassword.getText().toString().trim().equals("") && (!repassword.getText().toString().trim().equals(password.getText().toString().trim()))) {
            value = false;
            repassword.setError("كلمه المرور غير متطابقين");
        } else {
            repassword.setError(null);
        }
        return value;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"Unable to pick image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri imageUri = data.getData();
            img.setImageURI(imageUri);
            imagePath = getRealPathFromURI(imageUri);
        }

    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result  = cursor.getString(column_index);
        cursor.close();
        return result ;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void Update() {
        final String customers_username =  username.getText().toString().trim();
        final String customers_telephone =  phone.getText().toString().trim();
        String customers_password =  password.getText().toString().trim();
        if(customers_password.isEmpty()){
            customers_password = "";
            Log.e("password","null");
        }else {
            customers_password =  password.getText().toString().trim();
            Log.e("password","customers_password");
        }

        //final ProgressDialog loading = ProgressDialog.show(this,"جاري تحميل البيانات","من فضلك انتظر",false,false);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.create();
        SpannableString ss=  new SpannableString("جاري  ارسال الطلب");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/jf.ttf");
        ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", typeFace), 0, ss.length(), 0);
        dialog.setMessage(ss);
        dialog.show();
        //////////////////

        SessionManager sessionManager = new SessionManager(MySettingsActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        final int customers_id = Integer.parseInt(user.get(SessionManager.USER_ID));

        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        MultipartBody.Part  images = null;
        if(imagePath != null) {
            File file = new File(imagePath);
            Log.d("file", String.valueOf(file));
            RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
            images = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            Log.d("images", String.valueOf(images));
        }
                     Log.d("customers_password", String.valueOf(customers_password));

        Log.d("DATA",customers_username+ customers_telephone+ customers_password+customers_id+ images);
        Call<UpdateUnformation> call = APIRequests.updateInformation(customers_username, customers_telephone, customers_password,customers_id,images);
        call.enqueue(new Callback<UpdateUnformation>() {
            @Override
            public void onResponse(Call<UpdateUnformation> call, retrofit2.Response<UpdateUnformation> response) {
                dialog.dismiss();
                SessionManager sessionManager = new SessionManager(MySettingsActivity.this);
                String name = customers_username;
                String email = "admin@ghaith.com";
                int user_id = customers_id;
                String mobile = customers_telephone;
                username.setText(name);
                phone.setText(mobile);
                HashMap<String, String> user = sessionManager.getUserDetails();
                String type = user.get(SessionManager.TYPE);
                String avatar = user.get(SessionManager.AVATAR);

                sessionManager.createLoginSession(name, email, String.valueOf(user_id), avatar, mobile, type);
                ///////////////////////////////////////////////////
                Toast.makeText(MySettingsActivity.this,"تم تعديل البيانات بنجاح",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), ShowMarketActivity.class);
                startActivity(intent);
                imagePath = null;
            }

            @Override
            public void onFailure(Call<UpdateUnformation> call, Throwable t) {
                dialog.dismiss();
                Log.d("Error", t.getMessage());
            }

        });
    }



    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(MySettingsActivity.this, ShowMarketActivity.class);
        startActivity(intent);
    }



}
