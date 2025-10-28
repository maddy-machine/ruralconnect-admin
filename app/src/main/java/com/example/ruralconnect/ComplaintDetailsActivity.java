package com.example.ruralconnect;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ComplaintDetailsActivity extends AppCompatActivity {

    private TextView tvDetailTitle, tvDetailCategory, tvDetailDescription;
    private TextView tvDetailStatus, tvDetailDate, tvDetailUserId;
    private Button btnUpdateStatus, btnDelete;
    private ImageButton btnBack;

    private DatabaseReference complaintRef;
    private String complaintId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_setails);

        // Get complaint ID from intent
        complaintId = getIntent().getStringExtra("complaint_id");

        if (complaintId == null) {
            Toast.makeText(this, "Invalid complaint ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase reference
        complaintRef = FirebaseDatabase.getInstance().getReference("complaints").child(complaintId);

        // Initialize views
        initializeViews();

        // Load complaint details
        loadComplaintDetails();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailUserId = findViewById(R.id.tvDetailUserId);

        btnUpdateStatus = findViewById(R.id.btnUpdateStatus);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadComplaintDetails() {
        complaintRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Complaint complaint = dataSnapshot.getValue(Complaint.class);

                if (complaint != null) {
                    tvDetailTitle.setText(complaint.getTitle());
                    tvDetailCategory.setText(complaint.getCategory());
                    tvDetailDescription.setText(complaint.getDescription());
                    tvDetailStatus.setText(complaint.getStatus());
                    tvDetailDate.setText(complaint.getDate());
                    tvDetailUserId.setText("User ID: " + complaint.getUserId());

                    // Set status color
                    updateStatusColor(complaint.getStatus());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ComplaintDetailsActivity.this,
                        "Failed to load details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatusColor(String status) {
        switch (status) {
            case "Pending":
                tvDetailStatus.setBackgroundColor(0xFFFF9800);
                break;
            case "In Progress":
                tvDetailStatus.setBackgroundColor(0xFF2196F3);
                break;
            case "Resolved":
                tvDetailStatus.setBackgroundColor(0xFF8BC34A);
                break;
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnUpdateStatus.setOnClickListener(v -> showStatusUpdateDialog());

        btnDelete.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void showStatusUpdateDialog() {
        String[] statuses = {"Pending", "In Progress", "Resolved"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Status");
        builder.setItems(statuses, (dialog, which) -> {
            String newStatus = statuses[which];
            updateStatus(newStatus);
        });
        builder.show();
    }

    private void updateStatus(String newStatus) {
        complaintRef.child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                    tvDetailStatus.setText(newStatus);
                    updateStatusColor(newStatus);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Complaint");
        builder.setMessage("Are you sure you want to delete this complaint?");
        builder.setPositiveButton("Delete", (dialog, which) -> deleteComplaint());
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteComplaint() {
        complaintRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Complaint deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete complaint", Toast.LENGTH_SHORT).show();
                });
    }
}
