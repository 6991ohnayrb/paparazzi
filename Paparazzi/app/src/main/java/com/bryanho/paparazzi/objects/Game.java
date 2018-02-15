package com.bryanho.paparazzi.objects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Game {
    @SerializedName("gameId") private int gameId;
    @SerializedName("gameInfo") private GameInfo gameInfo;
    @SerializedName("player") private List<Player> players = new ArrayList<>();
    @SerializedName("messages") private List<Message> messages = new ArrayList<>();

    public Game(String gameRoomName, int maximumPlayers, int gameDuration) {
        gameId = Math.abs(Long.toString(System.currentTimeMillis()).hashCode());
        gameInfo = new GameInfo(gameRoomName, maximumPlayers, gameDuration);
        players.add(new Player());
    }
}
