package com.example.ruralconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStartedButton = findViewById(R.id.button);
        getStartedButton.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.opening_login_page), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminLoginpage.class);
            startActivity(intent);
        });
    }
}
