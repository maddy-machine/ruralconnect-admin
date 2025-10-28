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
            Toast.makeText(this, "Opening Login Page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Admin_Login_page.class);
            startActivity(intent);
        });
    }
}
