package com.smegi.youtubeuploader.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
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

    public String getShortUrl() {
        String key = "37590ad3bd54f2643183c9d45a061a08";
        String uid = "1640822";
        String advert_type = "int";
        String domain = "q.gs";
        String url = downloadLink;

        String response = null;

        try {
            URL req = new URL("http://api.adf.ly/api.php?key=" + key + "&uid=" + uid + "&advert_type=" + advert_type + "&domain=" + domain + "&url=" + url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.openStream()));
            response = reader.readLine();
            reader.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getClass().getSimpleName() + ": " + ex.getMessage();
        }
    }
}
