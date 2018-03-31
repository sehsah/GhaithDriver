package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.custom.CustomTypefaceSpan;
import com.mahmoudsehsah.ghaithDriver.models.FinishRegister;
import com.mahmoudsehsah.ghaithDriver.models.Register;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

import java.io.File;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterFinishActivity extends AppCompatActivity {
    String imagePath,imagePath2,imagePath3,imagePath4;
    ImageView img ,img2,img3,img4;
    Button upload,upload2,upload3,upload4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_finish);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);

        img = findViewById(R.id.img);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);


        RelativeLayout up4 = findViewById(R.id.up4);
        RelativeLayout up1 = findViewById(R.id.up1);
        RelativeLayout up2 = findViewById(R.id.up2);
        RelativeLayout up3 = findViewById(R.id.up3);

        upload =  findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        upload2 =  findViewById(R.id.upload2);
        upload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage2();
            }
        });
        upload3 =  findViewById(R.id.upload3);
        upload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage3();
            }
        });
        upload4 =  findViewById(R.id.upload4);
        upload4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage4();
            }
        });

        final String type = getIntent().getStringExtra("type");
        Log.e("type",type);
        if(type.equals("delivery")){
            up1.setVisibility(View.GONE);
            up2.setVisibility(View.GONE);
            up3.setVisibility(View.GONE);

//            RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
//                    RelativeLayout.LayoutParams.FILL_PARENT);
//            up4.setLayoutParams(layout_description);

            

        }

        Button sign_up = findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinishRegister();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void FinishRegister() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.create();
        SpannableString ss=  new SpannableString("جاري  ارسال الملفات ");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/jf.ttf");
        ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", typeFace), 0, ss.length(), 0);
        dialog.setMessage(ss);
        dialog.show();
        //////////////////

        MultipartBody.Part  images = null;
        MultipartBody.Part  images2 = null;
        MultipartBody.Part  images3 = null;
        MultipartBody.Part  images4 = null;

        if(imagePath != null) {
            File file = new File(imagePath);
            Log.d("file", String.valueOf(file));
            RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
            images = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        }
        if(imagePath2 != null) {
            File file2 = new File(imagePath2);
            Log.d("file2", String.valueOf(file2));
            RequestBody requestFile2 = RequestBody.create(MediaType.parse("*/*"), file2);
            images2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestFile2);
        }
        if(imagePath3 != null) {
            File file3 = new File(imagePath3);
            RequestBody requestFile3 = RequestBody.create(MediaType.parse("*/*"), file3);
            images3 = MultipartBody.Part.createFormData("file3", file3.getName(), requestFile3);
        }
        if(imagePath4 != null) {
            File file4 = new File(imagePath4);
            RequestBody requestFile4 = RequestBody.create(MediaType.parse("*/*"), file4);
            images4 = MultipartBody.Part.createFormData("file4", file4.getName(), requestFile4);
        }

       final String driver_id = getIntent().getStringExtra("user_id");
        Log.e("driver_id",driver_id);

        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<FinishRegister> call = APIRequests.FinishRegister(driver_id, images,images2,images3,images4);
        call.enqueue(new Callback<FinishRegister>() {
            @Override
            public void onResponse(Call<FinishRegister> call, retrofit2.Response<FinishRegister> response) {
                dialog.dismiss();
                Log.d("getMessage", String.valueOf(response.body()));
                startActivity(new Intent(RegisterFinishActivity.this,RegisterAlertActivity.class));
            }

            @Override
            public void onFailure(Call<FinishRegister> call, Throwable t) {
                dialog.dismiss();
                Log.d("Error", t.getMessage());
                startActivity(new Intent(RegisterFinishActivity.this,RegisterAlertActivity.class));

            }

        });
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
            imagePath = getRealPathFromURI(imageUri);
            upload.setVisibility(View.GONE);
            img.setImageURI(imageUri);

        }

        if (resultCode == RESULT_OK && requestCode == 102) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"Unable to pick image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri imageUri = data.getData();
            imagePath2 = getRealPathFromURI(imageUri);
            upload2.setVisibility(View.GONE);
            img2.setImageURI(imageUri);
        }

        if (resultCode == RESULT_OK && requestCode == 103) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"Unable to pick image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri imageUri = data.getData();
            imagePath3 = getRealPathFromURI(imageUri);
            upload3.setVisibility(View.GONE);
            img3.setImageURI(imageUri);
        }

        if (resultCode == RESULT_OK && requestCode == 104) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"Unable to pick image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri imageUri = data.getData();
            imagePath4 = getRealPathFromURI(imageUri);
            upload4.setVisibility(View.GONE);
            img4.setImageURI(imageUri);
        }

    }


    private void chooseImage() {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose image");
        startActivityForResult(chooserIntent, 100);

    }

    private void chooseImage2() {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose image");
        startActivityForResult(chooserIntent, 102);

    }


    private void chooseImage3() {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose image");
        startActivityForResult(chooserIntent, 103);

    }


    private void chooseImage4() {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose image");
        startActivityForResult(chooserIntent, 104);

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
}
