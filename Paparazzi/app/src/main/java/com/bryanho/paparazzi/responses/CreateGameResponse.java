package com.bryanho.paparazzi.responses;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CreateGameResponse {
    @SerializedName("messagestatus") private String messageStatus;
}
