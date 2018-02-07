package com.bryanho.paparazzi.objects;

import com.bryanho.paparazzi.util.FacebookUtil;

import lombok.Data;

@Data
public class Player {
    private String facebookUserId;
    private String firstName;
    private String lastName;

    // Creates default player with current profile information
    public Player() {
        facebookUserId = FacebookUtil.getProfileId();
        firstName = FacebookUtil.getFirstName();
        lastName = FacebookUtil.getLastName();
    }

    // Creates a player with specified facebookUserId
    public Player(String facebookUserId) {
        this.facebookUserId = facebookUserId;
        firstName = "John";
        lastName = "Doe";
    }
}
