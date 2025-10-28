package com.example.ruralconnect;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    // UI Components
    private ImageButton btnBack;
    private CardView cardProfile;
    private TextView tvUserName, tvUserEmail;
    private Button btnEditProfile;
    private LinearLayout layoutChangePassword, layoutChangeEmail, layoutDeleteAccount;
    private LinearLayout layoutAboutApp, layoutPrivacyPolicy, layoutTermsConditions;
    private SwitchCompat switchPushNotifications, switchEmailNotifications, switchSmsNotifications;
    private SwitchCompat switchDarkMode;
    private Button btnLogout;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference usersRef;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize SharedPreferences
        preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Initialize views
        initializeViews();

        // Load user data
        loadUserData();

        // Load settings
        loadNotificationSettings();
        loadDarkModeSettings();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        cardProfile = findViewById(R.id.cardProfile);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        layoutChangePassword = findViewById(R.id.layoutChangePassword);
        layoutChangeEmail = findViewById(R.id.layoutChangeEmail);
        layoutDeleteAccount = findViewById(R.id.layoutDeleteAccount);

        switchPushNotifications = findViewById(R.id.switchPushNotifications);
        switchEmailNotifications = findViewById(R.id.switchEmailNotifications);
        switchSmsNotifications = findViewById(R.id.switchSmsNotifications);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        layoutAboutApp = findViewById(R.id.layoutAboutApp);
        layoutPrivacyPolicy = findViewById(R.id.layoutPrivacyPolicy);
        layoutTermsConditions = findViewById(R.id.layoutTermsConditions);

        btnLogout = findViewById(R.id.btnLogout);
    }

    private void loadUserData() {
        if (currentUser != null) {
            final String userId = currentUser.getUid();
            String email = currentUser.getEmail();

            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User data exists, load it
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String emailFromDb = dataSnapshot.child("email").getValue(String.class);

                        if (name != null && !name.isEmpty()) {
                            tvUserName.setText(name);
                        } else {
                            tvUserName.setText(email != null ? email.split("@")[0] : "User");
                        }

                        tvUserEmail.setText(emailFromDb != null ? emailFromDb : email);
                    } else {
                        // User data doesn't exist, create it
                        String defaultName = email != null ? email.split("@")[0] : "User";

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("userId", userId);
                        userData.put("name", defaultName);
                        userData.put("email", email);
                        userData.put("phone", "Not provided");
                        userData.put("role", "user");
                        userData.put("registrationDate", System.currentTimeMillis());

                        usersRef.child(userId).setValue(userData)
                                .addOnSuccessListener(aVoid -> {
                                    tvUserName.setText(defaultName);
                                    tvUserEmail.setText(email);
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                    Toast.makeText(SettingsActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadNotificationSettings() {
        switchPushNotifications.setChecked(preferences.getBoolean("push_notifications", true));
        switchEmailNotifications.setChecked(preferences.getBoolean("email_notifications", true));
        switchSmsNotifications.setChecked(preferences.getBoolean("sms_notifications", false));
    }

    private void loadDarkModeSettings() {
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkMode);
    }

    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Profile Edit
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profile feature coming soon", Toast.LENGTH_SHORT).show();
        });

        cardProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profile feature coming soon", Toast.LENGTH_SHORT).show();
        });

        // Dark Mode Switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("dark_mode", isChecked).apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Change Password
        layoutChangePassword.setOnClickListener(v -> showChangePasswordDialog());

        // Change Email
        layoutChangeEmail.setOnClickListener(v -> showChangeEmailDialog());

        // Delete Account
        layoutDeleteAccount.setOnClickListener(v -> showDeleteAccountConfirmation());

        // Notification Switches
        switchPushNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("push_notifications", isChecked).apply();
            Toast.makeText(this, "Push notifications " + (isChecked ? "enabled" : "disabled"),
                    Toast.LENGTH_SHORT).show();
        });

        switchEmailNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("email_notifications", isChecked).apply();
            Toast.makeText(this, "Email notifications " + (isChecked ? "enabled" : "disabled"),
                    Toast.LENGTH_SHORT).show();
        });

        switchSmsNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("sms_notifications", isChecked).apply();
            Toast.makeText(this, "SMS notifications " + (isChecked ? "enabled" : "disabled"),
                    Toast.LENGTH_SHORT).show();
        });

        // About App
        layoutAboutApp.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });

        // Privacy Policy
        layoutPrivacyPolicy.setOnClickListener(v -> {
            startActivity(new Intent(this, PrivacyPolicyActivity.class));
        });

        // Terms & Conditions
        layoutTermsConditions.setOnClickListener(v -> {
            startActivity(new Intent(this, TermsConditionsActivity.class));
        });

        // Logout
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void showChangePasswordDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);
        TextInputEditText etCurrentPassword = dialogView.findViewById(R.id.etCurrentPassword);
        TextInputEditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);
        TextInputEditText etConfirmPassword = dialogView.findViewById(R.id.etConfirmPassword);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialogView.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String currentPassword = etCurrentPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validation
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            changePassword(currentPassword, newPassword, dialog);
        });

        dialog.show();
    }

    private void changePassword(String currentPassword, String newPassword, AlertDialog dialog) {
        if (currentUser == null || currentUser.getEmail() == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Re-authenticate user
        AuthCredential credential = EmailAuthProvider.getCredential(
                currentUser.getEmail(), currentPassword);

        currentUser.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    // Update password
                    currentUser.updatePassword(newPassword)
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(this, "Password changed successfully",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to change password: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Current password is incorrect",
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void showChangeEmailDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_email, null);
        TextInputEditText etNewEmail = dialogView.findViewById(R.id.etNewEmail);
        TextInputEditText etPassword = dialogView.findViewById(R.id.etPassword);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialogView.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String newEmail = etNewEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (newEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            changeEmail(newEmail, password, dialog);
        });

        dialog.show();
    }

    private void changeEmail(String newEmail, String password, AlertDialog dialog) {
        if (currentUser == null || currentUser.getEmail() == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), password);

        currentUser.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    currentUser.updateEmail(newEmail)
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(this, "Email updated successfully. Please login again.",
                                        Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                mAuth.signOut();
                                Intent intent = new Intent(this, AdminLoginpage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to update email: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                });
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    mAuth.signOut();
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AdminLoginpage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteAccountConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action is irreversible.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // You would typically re-authenticate the user before this critical action
                    if(currentUser != null) {
                        currentUser.delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this, "Account deleted successfully.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SettingsActivity.this, AdminLoginpage.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SettingsActivity.this, "Failed to delete account: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload user data in case it changed
        currentUser = mAuth.getCurrentUser();
        loadUserData();
    }
}
