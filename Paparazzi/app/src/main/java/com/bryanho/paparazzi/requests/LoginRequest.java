package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Player;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class LoginRequest {
    @SerializedName("Player") private Player player;

    public LoginRequest(Player player) {
        this.player = player;
    }
}
