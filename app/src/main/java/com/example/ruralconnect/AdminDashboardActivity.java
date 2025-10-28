package com.example.ruralconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruralconnect.RecentComplaintAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    // UI Components
    private TextView tvWelcome, tvTotalComplaints, tvPendingComplaints;
    private TextView tvInProgressComplaints, tvResolvedComplaints;
    private Button btnViewAllComplaints, btnManageCategories, btnViewReports, btnLogout;
    private ImageButton btnSettings;
    private RecyclerView rvRecentComplaints;

    // Firebase
    private DatabaseReference complaintsRef;
    private FirebaseAuth mAuth;

    // Data
    private List<Complaint> recentComplaintsList;
    private RecentComplaintAdapter recentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        complaintsRef = FirebaseDatabase.getInstance().getReference("complaints");

        // Initialize UI views
        initializeViews();

        // Welcome Message
        setupWelcomeMessage();

        // Set up RecyclerView
        setupRecyclerView();

        // Load data from Firebase
        loadStatistics();
        loadRecentComplaints();

        // Setup button listeners
        setupClickListeners();
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalComplaints = findViewById(R.id.tvTotalComplaints);
        tvPendingComplaints = findViewById(R.id.tvPendingComplaints);
        tvInProgressComplaints = findViewById(R.id.tvInProgressComplaints);
        tvResolvedComplaints = findViewById(R.id.tvResolvedComplaints);
        btnViewAllComplaints = findViewById(R.id.btnViewAllComplaints);
        btnManageCategories = findViewById(R.id.btnManageCategories);
        btnViewReports = findViewById(R.id.btnViewReports);
        btnLogout = findViewById(R.id.btnLogout);
        btnSettings = findViewById(R.id.btnProfile); // Corrected ID
        rvRecentComplaints = findViewById(R.id.rvRecentComplaints);
    }

    private void setupWelcomeMessage() {
        if (mAuth.getCurrentUser() != null) {
            String email = mAuth.getCurrentUser().getEmail();
            if (email != null && !email.isEmpty()) {
                String name = email.split("@")[0];
                tvWelcome.setText(getString(R.string.welcome_admin, name));
            } else {
                tvWelcome.setText(getString(R.string.welcome_admin, "Admin"));
            }
        }
    }

    private void setupRecyclerView() {
        recentComplaintsList = new ArrayList<>();
        recentAdapter = new RecentComplaintAdapter(this, recentComplaintsList);
        rvRecentComplaints.setLayoutManager(new LinearLayoutManager(this));
        rvRecentComplaints.setAdapter(recentAdapter);
    }

    private void loadRecentComplaints() {
        Query recentQuery = complaintsRef.orderByKey().limitToLast(5);
        recentQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentComplaintsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Complaint complaint = snapshot.getValue(Complaint.class);
                    if (complaint != null) {
                        complaint.setId(snapshot.getKey());
                        recentComplaintsList.add(0, complaint); // Add to the top of the list
                    }
                }
                recentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to load recent complaints", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnViewAllComplaints.setOnClickListener(v -> 
            startActivity(new Intent(this, ViewComplaintsActivity.class)));

        btnManageCategories.setOnClickListener(v -> 
            Toast.makeText(this, getString(R.string.manage_categories_soon), Toast.LENGTH_SHORT).show());

        btnViewReports.setOnClickListener(v -> 
            Toast.makeText(this, getString(R.string.reports_feature_soon), Toast.LENGTH_SHORT).show());

        btnSettings.setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class)));

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, getString(R.string.logged_out_successfully), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminDashboardActivity.this, AdminLoginpage.class));
            finish();
        });
    }

    private void loadStatistics() {
        complaintsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long total = dataSnapshot.getChildrenCount();
                long pending = 0, inProgress = 0, resolved = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String status = snapshot.child("status").getValue(String.class);
                    if (status != null) {
                        switch (status) {
                            case "Pending": pending++; break;
                            case "In Progress": inProgress++; break;
                            case "Resolved": resolved++; break;
                        }
                    }
                }
                tvTotalComplaints.setText(String.valueOf(total));
                tvPendingComplaints.setText(String.valueOf(pending));
                tvInProgressComplaints.setText(String.valueOf(inProgress));
                tvResolvedComplaints.setText(String.valueOf(resolved));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to load statistics", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh statistics when the activity is resumed
        loadStatistics();
    }
}
