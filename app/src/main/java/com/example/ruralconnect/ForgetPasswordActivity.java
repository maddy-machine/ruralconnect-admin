package com.example.ruralconnect;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextInputEditText etEmail;
    private Button btnSendResetLink;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        btnSendResetLink = findViewById(R.id.btnSendResetLink);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Back to login text
        tvBackToLogin.setOnClickListener(v -> finish());

        // Send reset link button
        btnSendResetLink.setOnClickListener(v -> sendPasswordResetEmail());
    }

    private void sendPasswordResetEmail() {
        String email = etEmail.getText().toString().trim();

        // Validation
        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.email_is_required));
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.please_enter_a_valid_email));
            etEmail.requestFocus();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        btnSendResetLink.setEnabled(false);
        btnSendResetLink.setText(getString(R.string.sending));

        // Send password reset email
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    btnSendResetLink.setEnabled(true);
                    btnSendResetLink.setText(getString(R.string.send_reset_link));

                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                getString(R.string.password_reset_link_sent),
                                Toast.LENGTH_LONG).show();
                        finish(); // Go back to login page
                    } else {
                        String errorMessage = task.getException() != null ?
                                task.getException().getMessage() : getString(R.string.failed_to_send_reset_email);
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
