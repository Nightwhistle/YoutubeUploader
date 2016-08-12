package com.smegi.youtubeuploader;

import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Song;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pakijaner
 */
public class Search {
    private List<Band> bands;
    
    
    // Call with bands list with all bands returned in that list from destined folder.
    // searchFolder("d:\\temp", bands);
    private void searchFolder(String directoryName, List<Band> bands) {
        File directory = new File(directoryName);
        File[] filesList = directory.listFiles();

        for (File file : filesList) {
            if (file.isDirectory()) {
                Band band = new Band();
                band.setName(file.getName());
                File[] songsFiles = file.listFiles();

                for (File songFile : songsFiles) {
                    Song song = new Song();
                    song.setName(songFile.getName());
                    song.setPath(songFile.getAbsolutePath());
                    band.addSong(song);
                }
                bands.add(band);
            }
        }
    }
}