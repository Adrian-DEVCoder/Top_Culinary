package com.example.top_culinary.model;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.Size;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class Mensaje implements Serializable {
    private static final long serialVersionUID = -1234567890123456789L;

    private String content;

    private String senderId;
    private Long timestamp;

    public Mensaje() {
    }

    public Mensaje(String content, String senderId, Long timestamp) {
        this.content = content;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTimeStamp() {
        if (timestamp == null) {
            return "";
        }
        Date date = new Date(timestamp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(date);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            return sdf.format(date);
        }
    }
}
