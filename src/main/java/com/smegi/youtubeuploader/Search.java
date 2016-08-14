package com.smegi.youtubeuploader;

import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Song;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergej
 */
public class Search {
    
    // Returns ArrayList of all bands in folder
    public List<Band> getBands(String directoryName) {
        File directory = new File(directoryName);
        File[] filesList = directory.listFiles();
        List<Band> bands = new ArrayList<>();

        for (File file : filesList) {
            if (file.isDirectory()) {
                Band band = new Band();
                band.setName(file.getName());
                band.setPath(file.getAbsolutePath());
                File[] songsFiles = file.listFiles();

                for (File songFile : songsFiles) {
                    if (isImage(songFile)) {
                        band.setBackground(songFile.getAbsolutePath());
                        continue;
                    }
                    Song song = new Song();
                    song.setName(songFile.getName());
                    song.setPath(songFile.getAbsolutePath());
                    band.addSong(song);
                }
                bands.add(band);
            }
        }
        return bands;
    }
    
    
    // Returns true of file is image (jpg only so far, open for addition in return line)
    private boolean isImage(File file) {
        String extension = file.getName().substring(file.getName().length() - 3);
        return extension.equalsIgnoreCase("jpg") ||
               extension.equalsIgnoreCase("png");
    }
}