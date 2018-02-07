package com.bryanho.paparazzi.responses;

import com.bryanho.paparazzi.objects.Game;
import com.bryanho.paparazzi.objects.Player;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponse {
    private String loginStatus;
    private Player player;
    private List<Game> games;
}
