package com.bryanho.paparazzi.responses;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PlayerExistsResponse {
    @SerializedName("messagestatus") private String messageStatus;
}
