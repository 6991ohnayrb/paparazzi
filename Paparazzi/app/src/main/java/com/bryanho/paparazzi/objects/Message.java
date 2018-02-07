package com.bryanho.paparazzi.objects;

import android.graphics.Bitmap;

import lombok.Data;

@Data
public class Message {
    private Player sentFrom;
    private String message;
    private Bitmap image;

    public Message(Player sentFrom, String message) {
        this.sentFrom = sentFrom;
        this.message = message;
    }

    public boolean isFromMyself() {
        return (new Player()).equals(sentFrom);
    }
}
