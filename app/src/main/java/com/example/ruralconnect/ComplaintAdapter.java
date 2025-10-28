package com.example.ruralconnect;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {

    private Context context;
    private List<Complaint> complaintList;

    public ComplaintAdapter(Context context, List<Complaint> complaintList) {
        this.context = context;
        this.complaintList = complaintList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Complaint complaint = complaintList.get(position);
        holder.title.setText(complaint.getTitle());
        holder.category.setText(complaint.getCategory());
        holder.date.setText(complaint.getDate());
        holder.status.setText(complaint.getStatus());

        // Set status background
        Drawable statusBackground;
        switch (complaint.getStatus()) {
            case "In Progress":
                statusBackground = ContextCompat.getDrawable(context, R.drawable.status_inprogress_background);
                break;
            case "Resolved":
                statusBackground = ContextCompat.getDrawable(context, R.drawable.status_resolved_background);
                break;
            default:
                statusBackground = ContextCompat.getDrawable(context, R.drawable.status_pending_background);
                break;
        }
        holder.status.setBackground(statusBackground);

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView category;
        public TextView date;
        public TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvComplaintTitle);
            category = itemView.findViewById(R.id.tvComplaintCategory);
            date = itemView.findViewById(R.id.tvComplaintDate);
            status = itemView.findViewById(R.id.tvComplaintStatus);
        }
    }
}
