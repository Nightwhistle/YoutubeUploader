package com.smegi.youtubeuploader.Model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergej
 */
public class Band {
    private String name;
    private List<Song> songs = new ArrayList<>();
    private Background background;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
    
    public void addSong(Song song) {
        songs.add(song);
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    
}
