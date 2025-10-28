package com.example.ruralconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Adapter for displaying a list of recent complaints in a RecyclerView.
public class RecentComplaintAdapter extends RecyclerView.Adapter<RecentComplaintAdapter.ViewHolder> {

    private final Context context;
    private final List<Complaint> complaintList;

    // Constructor
    public RecentComplaintAdapter(Context context, List<Complaint> complaintList) {
        this.context = context;
        this.complaintList = complaintList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for each complaint item
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Binds data to the views in each item
        Complaint complaint = complaintList.get(position);
        holder.title.setText(complaint.getTitle());
        holder.category.setText(complaint.getCategory());
        holder.date.setText(complaint.getDate());
        holder.status.setText(complaint.getStatus());
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    // ViewHolder class to hold the views for each complaint item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView category;
        public final TextView date;
        public final TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvRecentTitle);
            category = itemView.findViewById(R.id.tvRecentCategory);
            date = itemView.findViewById(R.id.tvRecentDate);
            status = itemView.findViewById(R.id.tvRecentStatus);
        }
    }
}
