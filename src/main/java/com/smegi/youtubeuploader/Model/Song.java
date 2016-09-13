package com.smegi.youtubeuploader.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    // Getting url shriked from gr.de using HTTP requests
    public String getTinyUrl() throws IOException {
        String urlPath = "http://qr.de/api/short?longurl=" + getAdflyLink();
        System.out.println("Api path: " + urlPath);
        StringBuilder jsonResponse = new StringBuilder();
        String response = "";
        try {
            URL url = new URL(urlPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonResponse.append(line);
                System.out.println("Response: " + line);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse.toString());
            response = root.path("url").toString().replaceAll("\"", "");

        } catch (MalformedURLException ex) {
            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Returning: " + response);
        return response;
    }
}
