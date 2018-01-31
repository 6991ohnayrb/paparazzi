package com.bryanho.paparazzi.objects;

import lombok.Data;

@Data
class GameInfo {
    private String gameRoomName;
    private int playerCount;
    private int gameDuration;

    GameInfo(String gameRoomName, int playerCount, int gameDuration) {
        this.gameRoomName = gameRoomName;
        this.playerCount = playerCount;
        this.gameDuration = gameDuration;
    }
}
