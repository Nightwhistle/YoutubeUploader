package com.smegi.youtubeuploader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergej
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
        songName = songName.replace(".mp3", "");
//        songName = songName.replace("- ", "");
//        songName = songName.replaceAll("'", "");
//        songName = songName.replaceAll("\"", "").replaceAll("[()]", "");
        songName = songName.replaceAll("[^A-Za-z0-9 ]", "");

        String[] songNameArray = songName.split(" ");
        for (String tag : songNameArray) {
            if (tag.length() < 2) continue;
            tags.add(tag);
        }

        // add predefined tags list from file
        try {

            for (String tag : Files.readAllLines(Paths.get(MyPaths.DEFAULT_TAGS_PATH))) {
                tag = tag.replaceAll("[^A-Za-z0-9 ]", "");
                if (tag.length() < 2) continue;
                tags.add(tag);
            }
        } catch (IOException ex) {
            Logger.getLogger(TagsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.print("Tags: ");
        for (String tag : tags) {
            System.out.print(tag + " ");
        }
        System.out.println("");
        return tags;
    }

}
