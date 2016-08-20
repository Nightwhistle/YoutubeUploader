package com.smegi.youtubeuploader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pakijaner
 */
public class TagsGenerator {

    /**
     *
     * @param songName
     * @return
     */
    public List<String> generate(String songName) {
        List<String> tags = new ArrayList<>();

        // add song name to tags list
        tags.add(songName);
        
        if (songName.toLowerCase().contains("live")) {
            tags.add("live");
        }

        // add predefined tags list from file
        try {
            
            for (String tag : Files.readAllLines(Paths.get(MyPaths.DEFAULT_TAGS_PATH))) {
                tags.add(tag);
            }
        } catch (IOException ex) {
            Logger.getLogger(TagsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tags;
    }

}