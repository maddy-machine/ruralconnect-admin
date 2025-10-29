package com.example.ruralconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private LinearLayout layoutGettingStarted, layoutVideoTutorials, layoutUserGuide;
    private LinearLayout layoutEmailSupport, layoutPhoneSupport, layoutLiveChat;
    private LinearLayout faqItem1, faqItem2, faqItem3, faqItem4;
    private TextView faqAnswer1, faqAnswer2, faqAnswer3, faqAnswer4;
    private ImageView iconExpand1, iconExpand2, iconExpand3, iconExpand4;
    private Button btnSendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);

        layoutGettingStarted = findViewById(R.id.layoutGettingStarted);
        layoutVideoTutorials = findViewById(R.id.layoutVideoTutorials);
        layoutUserGuide = findViewById(R.id.layoutUserGuide);

        layoutEmailSupport = findViewById(R.id.layoutEmailSupport);
        layoutPhoneSupport = findViewById(R.id.layoutPhoneSupport);
        layoutLiveChat = findViewById(R.id.layoutLiveChat);

        faqItem1 = findViewById(R.id.faqItem1);
        faqItem2 = findViewById(R.id.faqItem2);
        faqItem3 = findViewById(R.id.faqItem3);
        faqItem4 = findViewById(R.id.faqItem4);

        faqAnswer1 = findViewById(R.id.faqAnswer1);
        faqAnswer2 = findViewById(R.id.faqAnswer2);
        faqAnswer3 = findViewById(R.id.faqAnswer3);
        faqAnswer4 = findViewById(R.id.faqAnswer4);

        iconExpand1 = findViewById(R.id.iconExpand1);
        iconExpand2 = findViewById(R.id.iconExpand2);
        iconExpand3 = findViewById(R.id.iconExpand3);
        iconExpand4 = findViewById(R.id.iconExpand4);

        btnSendFeedback = findViewById(R.id.btnSendFeedback);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Quick Help Options
        layoutGettingStarted.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Getting Started Guide", Toast.LENGTH_SHORT).show();
        });

        layoutVideoTutorials.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Video Tutorials", Toast.LENGTH_SHORT).show();
        });

        layoutUserGuide.setOnClickListener(v -> {
            Toast.makeText(this, "Opening User Guide", Toast.LENGTH_SHORT).show();
        });

        // FAQ Items
        faqItem1.setOnClickListener(v -> toggleFAQ(faqAnswer1, iconExpand1));
        faqItem2.setOnClickListener(v -> toggleFAQ(faqAnswer2, iconExpand2));
        faqItem3.setOnClickListener(v -> toggleFAQ(faqAnswer3, iconExpand3));
        faqItem4.setOnClickListener(v -> toggleFAQ(faqAnswer4, iconExpand4));

        // Contact Support
        layoutEmailSupport.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:support@ruralconnect.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
            startActivity(Intent.createChooser(emailIntent, "Send email"));
        });

        layoutPhoneSupport.setOnClickListener(v -> {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
            phoneIntent.setData(Uri.parse("tel:+918001234567"));
            startActivity(phoneIntent);
        });

        layoutLiveChat.setOnClickListener(v -> {
            Toast.makeText(this, "Live chat feature coming soon", Toast.LENGTH_SHORT).show();
        });

        // Send Feedback
        btnSendFeedback.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:feedback@ruralconnect.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback");
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));
        });
    }

    private void toggleFAQ(TextView answer, ImageView icon) {
        if (answer.getVisibility() == View.GONE) {
            answer.setVisibility(View.VISIBLE);
            icon.setRotation(180);
        } else {
            answer.setVisibility(View.GONE);
            icon.setRotation(0);
        }
    }
}