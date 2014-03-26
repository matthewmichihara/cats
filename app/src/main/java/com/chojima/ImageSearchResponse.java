package com.chojima;

import java.util.ArrayList;
import java.util.List;

public class ImageSearchResponse {
    private final List<Image> data;

    public ImageSearchResponse(List<Image> data) {
        this.data = data;

        List<Image> albumsToRemove = new ArrayList<Image>();
        for (Image image : data) {
            if (image.isAlbum()) {
                albumsToRemove.add(image);
            }
        }

        data.removeAll(albumsToRemove);
    }

    public List<Image> getImages() {
        List<Image> albumsToRemove = new ArrayList<Image>();
        for (Image image : data) {
            if (image.isAlbum()) {
                albumsToRemove.add(image);
            }
        }

        data.removeAll(albumsToRemove);

        return data;
    }
}
