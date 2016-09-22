package com.smegi.youtubeuploader.uploads;

import com.google.api.client.http.FileContent;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Folder;
import com.smegi.youtubeuploader.Model.Song;
import com.smegi.youtubeuploader.MyPaths;
import com.smegi.youtubeuploader.Search;
import java.io.FileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriveUpload {

    private int numberOfSongs = Search.numberOfSongs;
    private int songsUploaded = 0;

    // create new folder on Google Drive if its not already created (not in folders.txt)
    private void createFolder(Band band) {
        if (!folderExists(band.getName())) {
            try {
                Drive service = GoogleAuthorize.getDriveService();
                File fileMetadata = new File();
                fileMetadata.setName(band.getName());
                fileMetadata.setMimeType("application/vnd.google-apps.folder");

                File file = service.files().create(fileMetadata)
                        .setFields("id")
                        .execute();
                band.setFolderId(file.getId());

                // Write newly created folder to folders.txt
                String filePath = MyPaths.RESOURCES_PATH + "/folders.txt";
                FileWriter fw = new FileWriter(filePath, true);
                fw.write(band.getName() + "," + band.getFolderId() + "\r\n");
                fw.close();
            } catch (Exception ex) {
                Logger.getLogger(DriveUpload.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // check if folder named as band already exists on Google Drive (in folders.txt)
    private boolean folderExists(String name) {
        Search s = new Search();
        for (Folder f : s.getFolders()) {
            if (name.equals(f.getName())) {
                return true;
            }
        }
        return false;
    }

    public void upload(List<Band> bands) throws IOException, Exception {

        UploadVideo uv = new UploadVideo();

        // Build a new authorized API client service.
        Drive service = GoogleAuthorize.getDriveService();

        // Get list of all folders that already exist
        Search s = new Search();
        List<Folder> folders = s.getFolders();

        // Loop through bands provided
        for (Band band : bands) {

            createFolder(band);

            for (Song song : band.getSongs()) {
                // Creating download link
                String downloadLink;

                // Setting metadate for file
                File metaData = new File();
                metaData.setName(song.getName());
                metaData.setParents(Collections.singletonList(band.getFolderId()));

                // Loading file
                java.io.File filePath = new java.io.File(song.getPath());
                FileContent content = new FileContent("audio/mp3", filePath);

                // Uploading file
                System.out.printf("[%d/%d] Uploading: %s %.2fmb %n", ++songsUploaded, numberOfSongs, song.getName(), filePath.length() / 1000000D);
                File fileTest = service.files()
                        .create(metaData, content)
                        .setFields("id, parents")
                        .execute();

                downloadLink = "https://drive.google.com/file/d/" + fileTest.getId();
                song.setDownloadLink(downloadLink);
                System.out.println("    Drive upload completed");
                try {
                    Thread.sleep(2000);                 //2 seconds
                } catch (InterruptedException ex) {
                    System.out.println("Interrupted exception in DriveUpload class");
                    Thread.currentThread().interrupt();
                }

                // If video is successfully uploaded to youtube delete local MP3, if not delete file on drive
                if (uv.upload(song)) {
                    java.io.File songFile = new java.io.File(song.getPath());
                    if (!songFile.canWrite()) {
                        System.out.println("File was read-only, setting to writable and deleting");
                        songFile.setReadOnly();
                    }
                    songFile.delete();
                } else {
                    try {
                        service.files().delete(fileTest.getId()).execute();
                    } catch (IOException e) {
                        System.out.println("IOexception in DriveUpload class");

                        System.out.println("An error occurred: " + e);
                    }
                }

            }
        }
    }

}
