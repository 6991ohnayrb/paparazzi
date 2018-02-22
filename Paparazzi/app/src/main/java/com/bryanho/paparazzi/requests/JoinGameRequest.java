package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Player;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class JoinGameRequest {
    @SerializedName("player") private Player player;
    @SerializedName("gameRoomName") private String gameRoomName;

    public JoinGameRequest(String gameRoomName) {
        this.player = new Player();
        this.gameRoomName = gameRoomName;
    }
}
