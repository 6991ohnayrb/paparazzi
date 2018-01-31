package com.bryanho.paparazzi.objects;

import com.facebook.Profile;

import lombok.Data;

@Data
class Player {
    private String facebookUserId;
    private String firstName;
    private String lastName;

    // Creates default player with current profile information
    Player() {
        final Profile currentProfile = Profile.getCurrentProfile();
        facebookUserId = currentProfile.getId();
        firstName = currentProfile.getFirstName();
        lastName = currentProfile.getLastName();
    }
}
