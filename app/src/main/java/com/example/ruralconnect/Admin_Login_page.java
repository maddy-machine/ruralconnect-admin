package com.example.ruralconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Admin_Login_page extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Login button click
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Test credentials (for now)
            if (email.equals("admin@ruralconnect.com") && password.equals("admin123")) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Admin_Login_page.this, AdminDashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
