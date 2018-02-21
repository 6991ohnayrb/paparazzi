package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Message;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SendMessageRequest {
    @SerializedName("GameId") private long gameId;
    @SerializedName("Message") private Message message;

    public SendMessageRequest(long gameId, Message message) {
        this.gameId = gameId;
        this.message = message;
    }
}
