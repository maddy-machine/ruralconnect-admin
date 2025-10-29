package com.example.ruralconnect;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class viewreports extends AppCompatActivity {

    // UI Components
    private ImageButton btnBack, btnExport;
    private Button btnToday, btnWeek, btnMonth, btnYear, btnCustom;
    private Button btnExportPDF, btnExportExcel;
    private TextView tvTotalComplaints, tvResolvedComplaints, tvPendingComplaints, tvInProgressComplaints;
    private TextView tvWaterCount, tvRoadsCount, tvElectricityCount, tvHealthcareCount, tvOthersCount;
    private TextView tvAvgResolutionTime, tvResolutionRate;

    // Firebase
    private DatabaseReference complaintsRef;

    // Data
    private String selectedPeriod = "month";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewreports);

        // Initialize Firebase
        complaintsRef = FirebaseDatabase.getInstance().getReference("complaints");

        // Initialize views
        initializeViews();

        // Setup click listeners
        setupClickListeners();

        // Load initial data
        loadReportsData();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnExport = findViewById(R.id.btnExport);

        btnToday = findViewById(R.id.btnToday);
        btnWeek = findViewById(R.id.btnWeek);
        btnMonth = findViewById(R.id.btnMonth);
        btnYear = findViewById(R.id.btnYear);
        btnCustom = findViewById(R.id.btnCustom);

        tvTotalComplaints = findViewById(R.id.tvTotalComplaints);
        tvResolvedComplaints = findViewById(R.id.tvResolvedComplaints);
        tvPendingComplaints = findViewById(R.id.tvPendingComplaints);
        tvInProgressComplaints = findViewById(R.id.tvInProgressComplaints);

        tvWaterCount = findViewById(R.id.tvWaterCount);
        tvRoadsCount = findViewById(R.id.tvRoadsCount);
        tvElectricityCount = findViewById(R.id.tvElectricityCount);
        tvHealthcareCount = findViewById(R.id.tvHealthcareCount);
        tvOthersCount = findViewById(R.id.tvOthersCount);

        tvAvgResolutionTime = findViewById(R.id.tvAvgResolutionTime);
        tvResolutionRate = findViewById(R.id.tvResolutionRate);

        btnExportPDF = findViewById(R.id.btnExportPDF);
        btnExportExcel = findViewById(R.id.btnExportExcel);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnExport.setOnClickListener(v -> showExportOptions());

        // Period selection buttons
        btnToday.setOnClickListener(v -> {
            selectedPeriod = "today";
            updateButtonSelection(btnToday);
            loadReportsData();
        });

        btnWeek.setOnClickListener(v -> {
            selectedPeriod = "week";
            updateButtonSelection(btnWeek);
            loadReportsData();
        });

        btnMonth.setOnClickListener(v -> {
            selectedPeriod = "month";
            updateButtonSelection(btnMonth);
            loadReportsData();
        });

        btnYear.setOnClickListener(v -> {
            selectedPeriod = "year";
            updateButtonSelection(btnYear);
            loadReportsData();
        });

        btnCustom.setOnClickListener(v -> Toast.makeText(this, "Custom date range coming soon", Toast.LENGTH_SHORT).show());

        // Export buttons
        btnExportPDF.setOnClickListener(v -> exportAsPDF());
        btnExportExcel.setOnClickListener(v -> exportAsExcel());
    }

    private void updateButtonSelection(Button selectedButton) {
        // Reset all buttons
        btnToday.setBackgroundTintList(null);
        btnWeek.setBackgroundTintList(null);
        btnMonth.setBackgroundTintList(null);
        btnYear.setBackgroundTintList(null);

        btnToday.setTextColor(Color.parseColor("#9C27B0"));
        btnWeek.setTextColor(Color.parseColor("#9C27B0"));
        btnMonth.setTextColor(Color.parseColor("#9C27B0"));
        btnYear.setTextColor(Color.parseColor("#9C27B0"));

        // Highlight selected button
        selectedButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple_500)));
        selectedButton.setTextColor(Color.WHITE);
    }

    private void loadReportsData() {
        complaintsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total = 0, resolved = 0, pending = 0, inProgress = 0;
                int water = 0, roads = 0, electricity = 0, healthcare = 0, others = 0;
                long totalResolutionTime = 0;
                int resolvedCount = 0;

                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, -getPeriodDays());
                Date startDate = calendar.getTime();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Complaint complaint = snapshot.getValue(Complaint.class);
                    if (complaint != null) {
                        try {
                            Date complaintDate = sdf.parse(complaint.getDate());

                            if (complaintDate != null && complaintDate.after(startDate)) {
                                total++;

                                // Status count
                                String status = complaint.getStatus();
                                if ("Resolved".equals(status)) {
                                    resolved++;
                                } else if ("Pending".equals(status)) {
                                    pending++;
                                } else if ("In Progress".equals(status)) {
                                    inProgress++;
                                }

                                // Category count
                                String category = complaint.getCategory();
                                if (category != null) {
                                    switch (category) {
                                        case "Water Supply":
                                            water++;
                                            break;
                                        case "Roads & Infrastructure":
                                            roads++;
                                            break;
                                        case "Electricity":
                                            electricity++;
                                            break;
                                        case "Healthcare":
                                            healthcare++;
                                            break;
                                        default:
                                            others++;
                                            break;
                                    }
                                }

                                // Calculate resolution time for resolved complaints
                                if ("Resolved".equals(status)) {
                                    long timeDiff = currentDate.getTime() - complaintDate.getTime();
                                    totalResolutionTime += timeDiff;
                                    resolvedCount++;
                                }
                            }
                        } catch (ParseException e) {
                            // Instead of printing the stack trace, show a toast message or log the error
                            Toast.makeText(viewreports.this, "Error parsing date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                // Update UI
                tvTotalComplaints.setText(String.valueOf(total));
                tvResolvedComplaints.setText(String.valueOf(resolved));
                tvPendingComplaints.setText(String.valueOf(pending));
                tvInProgressComplaints.setText(String.valueOf(inProgress));

                tvWaterCount.setText(String.valueOf(water));
                tvRoadsCount.setText(String.valueOf(roads));
                tvElectricityCount.setText(String.valueOf(electricity));
                tvHealthcareCount.setText(String.valueOf(healthcare));
                tvOthersCount.setText(String.valueOf(others));

                // Calculate metrics
                if (resolvedCount > 0) {
                    long avgTime = totalResolutionTime / resolvedCount;
                    long days = avgTime / (1000 * 60 * 60 * 24);
                    tvAvgResolutionTime.setText(getString(R.string.days_format, days));
                } else {
                    tvAvgResolutionTime.setText(R.string.not_applicable);
                }

                if (total > 0) {
                    int resolutionRate = (resolved * 100) / total;
                    tvResolutionRate.setText(getString(R.string.percentage_format, resolutionRate));
                } else {
                    tvResolutionRate.setText(getString(R.string.percentage_format, 0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewreports.this,
                        "Failed to load reports", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getPeriodDays() {
        switch (selectedPeriod) {
            case "today":
                return 0;
            case "week":
                return 7;
            case "year":
                return 365;
            default:
                return 30; // Default to month
        }
    }

    private void showExportOptions() {
        Toast.makeText(this, "Choose export format below", Toast.LENGTH_SHORT).show();
    }

    private void exportAsPDF() {
        Toast.makeText(this, "Exporting report as PDF...", Toast.LENGTH_SHORT).show();
        // Implement PDF export functionality
        // You can use libraries like iText or Android PDF
    }

    private void exportAsExcel() {
        Toast.makeText(this, "Exporting report as Excel...", Toast.LENGTH_SHORT).show();
        // Implement Excel export functionality
        // You can use Apache POI library
    }
}
