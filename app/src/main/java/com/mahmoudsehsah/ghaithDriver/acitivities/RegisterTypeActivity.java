package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.mahmoudsehsah.ghaithDriver.R;

import me.anwarshahriar.calligrapher.Calligrapher;

public class RegisterTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_type);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);

        LinearLayout driver = findViewById(R.id.driver);
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterTypeActivity.this, RegisterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("type","driver");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        LinearLayout delivery = findViewById(R.id.delivery);
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterTypeActivity.this, RegisterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("type","delivery");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

}
