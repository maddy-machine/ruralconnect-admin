package com.example.ruralconnect;

public class Complaint {
    private String id;
    private String title;
    private String category;
    private String description;
    private String status;
    private String date;
    private String userId;

    // Empty constructor required for Firebase
    public Complaint() {
    }

    // Constructor with parameters
    public Complaint(String id, String title, String category, String description,
                     String status, String date, String userId) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.status = status;
        this.date = date;
        this.userId = userId;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
