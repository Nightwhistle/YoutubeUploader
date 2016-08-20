package com.smegi.youtubeuploader.Model;

import java.util.List;

/**
 *
 * @author Sergej
 */
public class Song {
    private String name;
    private String path;
    private String downloadLink;
    private List<String> tags;
    private MusicVideo musicVideo;
    
    public String getAdflyLink() {
        return "http://adf.ly/1640822/" + downloadLink;
    }
    
    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public MusicVideo getMusicVideo() {
        return musicVideo;
    }

    public void setMusicVideo(MusicVideo musicVideo) {
        this.musicVideo = musicVideo;
    }
}
