package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Game;
import com.google.gson.annotations.SerializedName;

public class CreateGameRequest {
    @SerializedName("game") private Game game;

    public CreateGameRequest(Game game) {
        this.game = game;
    }
}
