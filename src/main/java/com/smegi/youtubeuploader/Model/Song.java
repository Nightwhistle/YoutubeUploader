package com.smegi.youtubeuploader.Model;

/**
 *
 * @author Sergej
 */
public class Song {
    private String name;
    private String path;
    private String downloadLink;
    
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
}
