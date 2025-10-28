package com.example.ruralconnect;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewComplaintsActivity extends AppCompatActivity {

    private RecyclerView rvComplaints;
    private ComplaintAdapter adapter;
    private List<Complaint> complaintList;
    private DatabaseReference complaintsRef;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaints);

        // Initialize Firebase
        complaintsRef = FirebaseDatabase.getInstance().getReference("complaints");

        // Initialize views
        rvComplaints = findViewById(R.id.rvComplaints);
        btnBack = findViewById(R.id.btnBack);

        // Set up RecyclerView
        complaintList = new ArrayList<>();
        adapter = new ComplaintAdapter(this, complaintList);
        rvComplaints.setLayoutManager(new LinearLayoutManager(this));
        rvComplaints.setAdapter(adapter);

        // Load complaints
        loadComplaints();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadComplaints() {
        complaintsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                complaintList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Complaint complaint = snapshot.getValue(Complaint.class);
                    if (complaint != null) {
                        complaint.setId(snapshot.getKey());
                        complaintList.add(complaint);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ViewComplaintsActivity.this,
                        "Failed to load complaints", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
