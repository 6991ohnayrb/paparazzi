package com.bryanho.paparazzi.objects;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GameInfo {
    @SerializedName("gameRoomName") private String gameRoomName;
    @SerializedName("playerCount") private int playerCount;
    @SerializedName("gameDuration") private int gameDuration;

    GameInfo(String gameRoomName, int playerCount, int gameDuration) {
        this.gameRoomName = gameRoomName;
        this.playerCount = playerCount;
        this.gameDuration = gameDuration;
    }
}
