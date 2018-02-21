package com.bryanho.paparazzi.responses;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SendMessageResponse {
    @SerializedName("messagestatus") private String messageStatus;
    @SerializedName("timestamp") private long timestamp;
}
