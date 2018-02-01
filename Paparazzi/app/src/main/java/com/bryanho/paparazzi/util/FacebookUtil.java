package com.bryanho.paparazzi.util;

import com.facebook.Profile;

public class FacebookUtil {
    public static String getProfileId() {
        final Profile profile = Profile.getCurrentProfile();
        return profile != null ? profile.getId() : null;
    }

    public static String getFullName() {
        final String firstName = getFirstName();
        final String lastName = getLastName();
        return firstName != null && lastName != null ? firstName + " " + lastName : null;
    }

    public static String getFirstName() {
        final Profile profile = Profile.getCurrentProfile();
        return profile != null ? profile.getFirstName() : null;
    }

    public static String getLastName() {
        final Profile profile = Profile.getCurrentProfile();
        return profile != null ? profile.getLastName() : null;
    }
}
