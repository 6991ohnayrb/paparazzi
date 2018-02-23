package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Player;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PlayerExistsRequest {
    @SerializedName("player") private Player player;

    public PlayerExistsRequest(Player player) {
        this.player = player;
    }
}
