package com.mahmoudsehsah.ghaithDriver.acitivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

import me.anwarshahriar.calligrapher.Calligrapher;

public class RegisterAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_alert);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font/jf.ttf", true);

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager sessionManagerr = new SessionManager(RegisterAlertActivity.this);
                sessionManagerr.logoutUser();
                startActivity(new Intent(RegisterAlertActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}
