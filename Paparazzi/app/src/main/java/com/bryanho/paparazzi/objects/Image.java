package com.bryanho.paparazzi.objects;

import java.util.List;

import lombok.Data;

@Data
public class Image {
    private String imageId;
    private String imageContent;
    private Player targetPlayer;
    private List<Integer> ratings;

    public Image(String imageContent) {
        this.imageContent = imageContent;
    }
}
