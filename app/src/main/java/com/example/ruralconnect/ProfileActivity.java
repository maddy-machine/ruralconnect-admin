package com.example.ruralconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton btnBack, btnEditProfile;
    private CardView btnChangePicture;
    private TextView tvUserName, tvUserEmail, tvUserRole;
    private TextView tvFullName, tvEmail, tvPhone, tvJoinDate;
    private TextView tvTotalComplaints, tvResolvedComplaints, tvPendingComplaints;
    private LinearLayout layoutMyComplaints, layoutSettings;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference usersRef, complaintsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        complaintsRef = FirebaseDatabase.getInstance().getReference("complaints");

        // Initialize views
        initializeViews();

        // Load user data
        loadUserProfile();
        loadComplaintStats();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePicture = findViewById(R.id.btnChangePicture);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserRole = findViewById(R.id.tvUserRole);

        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvJoinDate = findViewById(R.id.tvJoinDate);

        tvTotalComplaints = findViewById(R.id.tvTotalComplaints);
        tvResolvedComplaints = findViewById(R.id.tvResolvedComplaints);
        tvPendingComplaints = findViewById(R.id.tvPendingComplaints);

        layoutMyComplaints = findViewById(R.id.layoutMyComplaints);
        layoutSettings = findViewById(R.id.layoutSettings);
    }

    private void loadUserProfile() {
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String userId = currentUser.getUid();

            // Set email
            tvUserEmail.setText(email);
            tvEmail.setText(email);

            // Load user data from Firebase
            usersRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);
                        String role = dataSnapshot.child("role").getValue(String.class);
                        Long registrationTimestamp = dataSnapshot.child("registrationDate").getValue(Long.class);

                        // Set name
                        if (name != null && !name.isEmpty()) {
                            tvUserName.setText(name);
                            tvFullName.setText(name);
                        } else {
                            String defaultName = email != null ? email.split("@")[0] : "User";
                            tvUserName.setText(defaultName);
                            tvFullName.setText(defaultName);
                        }

                        // Set phone
                        if (phone != null && !phone.isEmpty()) {
                            tvPhone.setText(phone);
                        } else {
                            tvPhone.setText("Not provided");
                        }

                        // Set role
                        if (role != null) {
                            String roleText = role.equals("admin") ? "Administrator" : "User";
                            tvUserRole.setText(roleText);
                        } else {
                            tvUserRole.setText("User");
                        }

                        // Set join date
                        if (registrationTimestamp != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                            String joinDate = sdf.format(new Date(registrationTimestamp));
                            tvJoinDate.setText(joinDate);
                        } else {
                            tvJoinDate.setText("Recently");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this,
                            "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadComplaintStats() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            complaintsRef.orderByChild("userId").equalTo(userId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int total = 0, resolved = 0, pending = 0;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                total++;
                                String status = snapshot.child("status").getValue(String.class);
                                if ("Resolved".equals(status)) {
                                    resolved++;
                                } else if ("Pending".equals(status)) {
                                    pending++;
                                }
                            }

                            tvTotalComplaints.setText(String.valueOf(total));
                            tvResolvedComplaints.setText(String.valueOf(resolved));
                            tvPendingComplaints.setText(String.valueOf(pending));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        btnChangePicture.setOnClickListener(v -> {
            Toast.makeText(this, "Change picture feature coming soon", Toast.LENGTH_SHORT).show();
            // Implement image picker
        });

        layoutMyComplaints.setOnClickListener(v -> {
            // Navigate to My Complaints page
            Toast.makeText(this, "Opening my complaints...", Toast.LENGTH_SHORT).show();
        });

        layoutSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
        loadComplaintStats();
    }
}
