package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.Toast;

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
import com.mahmoudsehsah.ghaithDriver.custom.CustomTypefaceSpan;
import com.mahmoudsehsah.ghaithDriver.models.ChatList;
import com.mahmoudsehsah.ghaithDriver.models.FinishOrder;
import com.mahmoudsehsah.ghaithDriver.models.SendMessage;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MessageActivity extends AppCompatActivity {

    LinearLayout layout;
    RelativeLayout layout_2;
    Button sendButton;
    EditText messageArea;
    ScrollView scrollView;
    private Toolbar mTopToolbar;
    TextView driver_name_text,drivername,pricc;
    ImageView driver_photo;
    private ArrayList<ChatList> data;
    private DataAdapterGetMessage adapter;
    private RecyclerView recyclerView;
    String imagePath;
    ImageView img;
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
          drivername = findViewById(R.id.drivername);
          pricc = findViewById(R.id.price);
        drivername.setText(driver_name);


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


        String id_order = getIntent().getStringExtra("id_order");
        Log.d("id_order",id_order);
        String url2 = "http://yaqeensa.com/android/ghaith/ShowOrderInfo?id="+id_order;
        RequestQueue requestQueue2= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest2=new StringRequest(Request.Method.GET, url2, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                         int price = Integer.parseInt(jsonObject1.getString("price"));
                         int price2 = Integer.parseInt(jsonObject1.getString("price2"));
                         Log.e("price", String.valueOf(price+price2));
                         pricc.setText("التكلفه :"+ String.valueOf(price2+price));
                        int  status = Integer.parseInt(jsonObject1.getString("status"));
                        if(status == 2){
                            LinearLayout box_information = findViewById(R.id.message_area);
                            box_information.setVisibility(View.GONE);
                            TextView text_finish = findViewById(R.id.text_finish);
                            text_finish.setVisibility(View.VISIBLE);
                        }
                        final String name_delivery =jsonObject1.getString("name_delivery");
                        Log.e("price", "Done");



                    }
                }catch (JSONException e){e.printStackTrace();}
            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout2 = 30000;
        RetryPolicy policy2 = new DefaultRetryPolicy(socketTimeout2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest2.setRetryPolicy(policy2);
        requestQueue2.add(stringRequest2);

        initViews();

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

        ImageView bottomSheetButton = findViewById(R.id.img_attachment);
        bottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });

        img = findViewById(R.id.img);

        Button getDirection = findViewById(R.id.getDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id_order = getIntent().getStringExtra("id_order");
                Intent intent = new Intent(MessageActivity.this, DirectionActivity.class);
                final Bundle bundle = new Bundle();
                bundle.putSerializable("id_order",id_order);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        TextView viewOrder = findViewById(R.id.viewOrder);
        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    public void openBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.botton_sheet, null);
        Typeface customTypeOne = Typeface.createFromAsset(getAssets(), "font/jf.ttf");

        final Dialog mBottomSheetDialog = new Dialog(MessageActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        Button finishorder = view.findViewById(R.id.finishorder);
        finishorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinishOrder();
                mBottomSheetDialog.dismiss();
            }
        });

        Button sendImg = view.findViewById(R.id.sendImg);
        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                Log.e("chooserIntent","اختار الصوره");
                mBottomSheetDialog.dismiss();

            }
        });

        Button sendButtonImage = findViewById(R.id.sendButtonImage);
        sendButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendNewMessagPhoto();

            }
        });

        Button editepay = view.findViewById(R.id.editepay);
        editepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_order = getIntent().getStringExtra("id_order");
                String driver_name = getIntent().getStringExtra("client_name");

                TextView price = findViewById(R.id.price);
                String price_val = price.getText().toString();
                Intent intent = new Intent(MessageActivity.this, BillActivity.class);
                final Bundle bundle = new Bundle();
                bundle.putSerializable("id_order",id_order);
                bundle.putSerializable("price_val",price_val);
                bundle.putSerializable("client_name",driver_name);
                intent.putExtras(bundle);
                startActivity(intent);
                mBottomSheetDialog.dismiss();

            }
        });

        sendImg.setTypeface(customTypeOne);
        editepay.setTypeface(customTypeOne);
        finishorder.setTypeface(customTypeOne);
    }


    private void FinishOrder() {
        final ProgressDialog dialog = new ProgressDialog(this);
        SpannableString ss=  new SpannableString("جاري انهاء الطلب");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/jf.ttf");
        ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", typeFace), 0, ss.length(), 0);
        dialog.setMessage(ss);
        dialog.show();
        //////////////////

        String id_order = getIntent().getStringExtra("id_order");
        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<FinishOrder> call = APIRequests.FinishOrder(id_order);
        call.enqueue(new Callback<FinishOrder>() {
            @Override
            public void onResponse(Call<FinishOrder> call, retrofit2.Response<FinishOrder> response) {
                finish();
                startActivity(getIntent());
                Toast.makeText(MessageActivity.this,"تم انهاء الطلب",Toast.LENGTH_SHORT).show();
                dialog.dismiss();


//                LayoutInflater inflater = LayoutInflater.from(MessageActivity.this);
//                View subView = inflater.inflate(R.layout.dialog_finish_order, null);
//                final TextView text_dialog = (TextView)subView.findViewById(R.id.text_dialog);
//                final TextView btn_dialog = (TextView)subView.findViewById(R.id.btn_dialog);
//                final AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
//                Typeface face=Typeface.createFromAsset(getAssets(),"font/jf.ttf");
//                text_dialog.setTypeface(face);
//                btn_dialog.setTypeface(face);
//                builder.setView(subView);
//                final AlertDialog alertDialog = builder.create();
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                alertDialog.show();
//                alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
//                    @Override
//                    public void onShow(DialogInterface arg0) {
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                    }
//                });
//                btn_dialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//                    }
//                });
            }

            @Override
            public void onFailure(Call<FinishOrder> call, Throwable t) {
                Log.d("Error", t.getMessage());
                finish();
                startActivity(getIntent());
                Toast.makeText(MessageActivity.this,"تم انهاء الطلب",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }


    private void SendNewMessagPhoto() {
        SessionManager sessionManager = new SessionManager(MessageActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String driver_id  = user.get(SessionManager.USER_ID);
        String client_id = getIntent().getStringExtra("client_id");
        String id_order = getIntent().getStringExtra("id_order");
        String type = "2";

        MultipartBody.Part  images = null;
        if(imagePath != null) {
            File file = new File(imagePath);
            Log.d("file", String.valueOf(file));
            RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
            images = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            Log.d("images", String.valueOf(images));
        }

        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<SendMessage> call = APIRequests.SendMessagePhoto(images, client_id,driver_id,driver_id,id_order,type);
        call.enqueue(new Callback<SendMessage>() {
            @Override
            public void onResponse(Call<SendMessage> call, retrofit2.Response<SendMessage> response) {
                finish();
                startActivity(getIntent());
                recyclerView.scrollToPosition(data.size() - 1);

            }

            @Override
            public void onFailure(Call<SendMessage> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }

        });

    }

    private void SendNewMessagr() {

        SessionManager sessionManager = new SessionManager(MessageActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String driver_id  = user.get(SessionManager.USER_ID);
        String client_id = getIntent().getStringExtra("client_id");
        String message = messageArea.getText().toString();
        String id_order = getIntent().getStringExtra("id_order");
        String type = "1";
        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<SendMessage> call = APIRequests.SendMessagee(message, client_id,driver_id,driver_id,id_order,type);
        call.enqueue(new Callback<SendMessage>() {
            @Override
            public void onResponse(Call<SendMessage> call, retrofit2.Response<SendMessage> response) {
                finish();
                startActivity(getIntent());
                recyclerView.scrollToPosition(data.size() - 1);

            }

            @Override
            public void onFailure(Call<SendMessage> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"Unable to pick image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri imageUri = data.getData();
            img.setImageURI(imageUri);
            imagePath = getRealPathFromURI(imageUri);
            LinearLayout message_area = findViewById(R.id.message_area);
            message_area.setVisibility(View.GONE);
            LinearLayout box_send_image = findViewById(R.id.box_send_image);
            box_send_image.setVisibility(View.VISIBLE);
            //mBottomSheetDialog.show();

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

    private void initViews(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }

    private void loadJSON() {

        final ProgressDialog dialog = new ProgressDialog(this);
        SpannableString ss=  new SpannableString("");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/jf.ttf");
        ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", typeFace), 0, ss.length(), 0);
        dialog.setMessage(ss);
        dialog.show();
        ///////////////////////////
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
                dialog.dismiss();
                JSONResponseGetMasseges JSONResponseGetMasseges = response.body();
                data = new ArrayList<>(Arrays.asList(JSONResponseGetMasseges.getAndroid()));
                adapter = new DataAdapterGetMessage(getApplicationContext(),data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponseGetMasseges> call, Throwable t) {
                dialog.dismiss();
                Log.d("Error",t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MessageActivity.this, MyOrdersActivity.class));
        finish();

    }

}
