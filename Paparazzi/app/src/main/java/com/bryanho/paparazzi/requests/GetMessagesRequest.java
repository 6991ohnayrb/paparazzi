package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Game;
import com.google.gson.annotations.SerializedName;

public class GetMessagesRequest {
    @SerializedName("gameid") private int gameId;

    public GetMessagesRequest(Game game) {
        gameId = game.getGameId();
    }
}
