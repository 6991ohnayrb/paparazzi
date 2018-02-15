package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Player;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GetGamesRequest {
    @SerializedName("Player") private Player player;

    public GetGamesRequest(Player player) {
        this.player = player;
    }
}
