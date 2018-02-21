package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Game;
import com.google.gson.annotations.SerializedName;

public class GetMessagesRequest {
    @SerializedName("gameid") private long gameId;

    public GetMessagesRequest(Game game) {
        gameId = game.getGameId();
    }
}
