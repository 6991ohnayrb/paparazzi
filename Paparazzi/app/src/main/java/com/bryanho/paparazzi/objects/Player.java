package com.bryanho.paparazzi.objects;

import com.bryanho.paparazzi.util.FacebookUtil;
import com.facebook.Profile;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Player {
    @SerializedName("facebookUserId") private String facebookUserId;
    @SerializedName("firstName") private String firstName;
    @SerializedName("lastName") private String lastName;

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

    // Creates a player with Profile
    public Player(Profile profile) {
        facebookUserId = profile.getId();
        firstName = profile.getFirstName();
        lastName = profile.getLastName();
    }
}
