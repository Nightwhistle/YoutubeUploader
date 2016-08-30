package com.smegi.youtubeuploader.Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergej
 */
public class Band {

    private String name;
    private List<Song> songs = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String background;
    private String path;
    private String folderId;

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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(File tagFile) {
        try {
            for (String tag : Files.readAllLines(Paths.get(tagFile.getAbsolutePath()))) {
                tags.add(tag);
            }
        } catch (IOException ex) {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
        tags.add(name);
    }

    public void setFolderId(String id) {
        folderId = id;
    }

    public String getFolderId() {
        return folderId;
    }

}
