package com.bryanho.paparazzi.objects;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Message {
    @SerializedName("sentFrom") private Player sentFrom;
    @SerializedName("message") private String message;
    @SerializedName("image") private String image;

    public Message(Player sentFrom, String message, String image) {
        this.sentFrom = sentFrom;
        this.message = message;
        this.image = image;
    }

    public boolean isFromMyself() {
        return (new Player()).equals(sentFrom);
    }
}
