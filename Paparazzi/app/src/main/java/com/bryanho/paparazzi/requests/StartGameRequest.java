package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Game;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class StartGameRequest {
    @SerializedName("game") private Game game;

    public StartGameRequest(Game game) {
        this.game = game;
    }
}
