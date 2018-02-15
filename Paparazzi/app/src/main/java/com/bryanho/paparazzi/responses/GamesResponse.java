package com.bryanho.paparazzi.responses;

import com.bryanho.paparazzi.objects.Game;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class GamesResponse {
    @SerializedName("games") List<Game> games;
}
