package com.bryanho.paparazzi.requests;

import com.bryanho.paparazzi.objects.Player;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RateImageRequest {
    @SerializedName("gameRoomName") private String gameRoomName;
    @SerializedName("imageId") private String imageId;
    @SerializedName("rating") private int rating;
    @SerializedName("player") private Player player;

    public RateImageRequest(String gameRoomName, String imageId, int rating, Player player) {
        this.gameRoomName = gameRoomName;
        this.imageId = imageId;
        this.rating = rating;
        this.player = player;
    }
}
