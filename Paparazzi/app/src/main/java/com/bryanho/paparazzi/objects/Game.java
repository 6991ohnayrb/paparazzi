package com.bryanho.paparazzi.objects;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Game {
    private int gameId;
    private GameInfo gameInfo;
    private List<Player> players = new ArrayList<>();

    public Game(String gameRoomName, int maximumPlayers, int gameDuration) {
        gameId = Math.abs(Long.toString(System.currentTimeMillis()).hashCode());
        gameInfo = new GameInfo(gameRoomName, maximumPlayers, gameDuration);
        players.add(new Player());
    }
}
