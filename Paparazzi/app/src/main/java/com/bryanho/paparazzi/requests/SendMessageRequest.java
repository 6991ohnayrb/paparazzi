package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Message;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SendMessageRequest {
    @SerializedName("GameId") private int gameId;
    @SerializedName("Message") private Message message;

    public SendMessageRequest(int gameId, Message message) {
        this.gameId = gameId;
        this.message = message;
    }
}
