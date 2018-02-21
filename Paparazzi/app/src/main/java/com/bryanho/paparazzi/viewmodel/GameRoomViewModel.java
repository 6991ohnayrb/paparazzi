package com.bryanho.paparazzi.viewmodel;

import com.bryanho.paparazzi.objects.Message;

import java.util.List;

import lombok.Data;

@Data
public class GameRoomViewModel {
    private List<Message> messages;
}
