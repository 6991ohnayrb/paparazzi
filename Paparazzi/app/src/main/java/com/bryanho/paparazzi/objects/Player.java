package com.bryanho.paparazzi.objects;

import com.bryanho.paparazzi.util.FacebookUtil;

import lombok.Data;

@Data
class Player {
    private String facebookUserId;
    private String firstName;
    private String lastName;

    // Creates default player with current profile information
    Player() {
        facebookUserId = FacebookUtil.getProfileId();
        firstName = FacebookUtil.getFirstName();
        lastName = FacebookUtil.getLastName();
    }
}
