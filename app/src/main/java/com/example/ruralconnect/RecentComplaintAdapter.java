package com.example.ruralconnect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecentComplaintsAdapter extends RecyclerView.Adapter<RecentComplaintsAdapter.ViewHolder> {

    private Context context;
    private List<Complaint> complaintList;

    public RecentComplaintsAdapter(Context context, List<Complaint> complaintList) {
        this.context = context;
        this.complaintList = complaintList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Complaint complaint = complaintList.get(position);

        holder.tvRecentTitle.setText(complaint.getTitle());
        holder.tvRecentCategory.setText("Category: " + complaint.getCategory());
        holder.tvRecentDate.setText(complaint.getDate());
        holder.tvRecentStatus.setText(complaint.getStatus());

        // Set status background color
        switch (complaint.getStatus()) {
            case "Pending":
                holder.tvRecentStatus.setBackgroundColor(Color.parseColor("#FF9800"));
                break;
            case "In Progress":
                holder.tvRecentStatus.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            case "Resolved":
                holder.tvRecentStatus.setBackgroundColor(Color.parseColor("#8BC34A"));
                break;
        }

        // Click to view details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ComplaintDetailsActivity.class);
            intent.putExtra("complaint_id", complaint.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecentTitle, tvRecentCategory, tvRecentDate, tvRecentStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecentTitle = itemView.findViewById(R.id.tvRecentTitle);
            tvRecentCategory = itemView.findViewById(R.id.tvRecentCategory);
            tvRecentDate = itemView.findViewById(R.id.tvRecentDate);
            tvRecentStatus = itemView.findViewById(R.id.tvRecentStatus);
        }
    }
}
