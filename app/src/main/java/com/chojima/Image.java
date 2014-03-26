package com.chojima;

public class Image {
    private final String link;
    private final int height;
    private final int width;
    private final boolean is_album;

    public Image(String link, int height, int width, boolean is_album) {
        this.link = link;
        this.height = height;
        this.width = width;
        this.is_album = is_album;
    }

    public String getLink() {
        return link;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isAlbum() { return is_album; }

    @Override public String toString() {
        return "[link=" + link + " height=" + height + " width=" + width + " isAlbum=" + is_album + "]";
    }
}
