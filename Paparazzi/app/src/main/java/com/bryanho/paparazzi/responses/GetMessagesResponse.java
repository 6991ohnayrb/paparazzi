package com.bryanho.paparazzi.responses;

import com.bryanho.paparazzi.objects.Message;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GetMessagesResponse {
    @SerializedName("status") private String status;
    @SerializedName("messages") private List<Message> messages;
}
