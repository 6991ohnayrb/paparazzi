package com.bryanho.paparazzi.responses;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class JoinGameResponse {
    @SerializedName("messagestatus") private String status;
}
