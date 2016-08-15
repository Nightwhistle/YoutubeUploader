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
    private List<MusicVideo> videos = new ArrayList<>();
    private String background;
    private String path;

    public List<MusicVideo> getMusicVideos() {
        return videos;
    }

    public void setMusicVideos(List<MusicVideo> videos) {
        this.videos = videos;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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
    
    public void addMusicVideo(MusicVideo video) {
        videos.add(video);
    }
    
    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    

}
