package com.smegi.youtubeuploader;

import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Folder;
import com.smegi.youtubeuploader.Model.Song;
import java.io.BufferedReader;
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
public class Search {
    
    public static int numberOfSongs = 0;
    
    // returns list of all folder names with their Drive IDs
    public List<Folder> getFolders() {
        List<Folder> folders = new ArrayList<>();
        
        try {
            BufferedReader br = Files.newBufferedReader(Paths.get(MyPaths.RESOURCES_PATH + "/folders.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                Folder folder = new Folder();
                String[] parts = line.split(",");
                folder.setName(parts[0]);
                folder.setId(parts[1]);
                folders.add(folder);
            }
        } catch (IOException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        }
        return folders;
    }

    // Returns ArrayList of all bands in folder
    public List<Band> getBands(String directoryName) {
        File directory = new File(directoryName);
        File[] filesList = directory.listFiles();
        List<Band> bands = new ArrayList<>();
        List<Folder> folders = getFolders();

        for (File file : filesList) {
            if (file.isDirectory()) {
                Band band = new Band();
                band.setName(file.getName());
                band.setPath(file.getAbsolutePath());
                File[] songsFiles = file.listFiles();
                
                // check if folder already exists on Drive and if it is assign folder ID to band object
                for (Folder f : folders) {
                    if (f.getName().equals(band.getName())) {
                        band.setFolderId(f.getId());
                    }
                }

                for (File songFile : songsFiles) {
                    if (isImage(songFile)) {
                        band.setBackground(songFile.getAbsolutePath());
                        continue;
                    }
                    
                    if (isText(songFile)) {
                        band.setTags(songFile);
                        continue;
                    }

                    if (isMusicFile(songFile)) {
                        TagsGenerator tg = new TagsGenerator();
                        Song song = new Song();
                        song.setName(songFile.getName());
                        song.setPath(songFile.getAbsolutePath());
                        song.setTags(tg.generate(songFile.getName()));
                        
                        band.addSong(song);
                        numberOfSongs++;
                    }
                }
                bands.add(band);
            }
        }
        return bands;
    }

    // Returns true of file is image (jpg only so far, open for addition in return line)
    private boolean isImage(File file) {
        String extension = file.getName().substring(file.getName().length() - 3);
        return extension.equalsIgnoreCase("jpg")
            || extension.equalsIgnoreCase("png");
    }

    private boolean isText(File file) {
        String extension = file.getName().substring(file.getName().length() - 3);
        return extension.equalsIgnoreCase("txt");
    }

    private boolean isMusicFile(File file) {
        String extension = file.getName().substring(file.getName().length() - 3);
        return extension.equalsIgnoreCase("mp3");
    }
}
