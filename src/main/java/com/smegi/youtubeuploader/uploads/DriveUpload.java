package com.smegi.youtubeuploader.uploads;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Folder;
import com.smegi.youtubeuploader.Model.Song;
import com.smegi.youtubeuploader.MyPaths;
import com.smegi.youtubeuploader.Search;
import java.io.FileInputStream;
import java.io.FileWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriveUpload {

    private int numberOfSongs = Search.numberOfSongs;
    private int songsUploaded = 0;

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME
            = "Drive API Java Quickstart";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/drive-java-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES
            = Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException, Exception {

        // Load client secrets.
        Reader clientSecretReader = new InputStreamReader(new FileInputStream(new java.io.File(MyPaths.CLIENT_SECRETS_PATH)));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     *
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException, Exception {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // create new folder on Google Drive if its not already created (not in folders.txt)
    private void createFolder(Band band) {
        if (!folderExists(band.getName())) {
            try {
                Drive service = getDriveService();
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
        // Build a new authorized API client service.
        Drive service = getDriveService();

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
                System.out.printf("Uploading %s %.2fmb [%d/%d]%n", song.getName(), filePath.length() / 1000000D, ++songsUploaded, numberOfSongs);
                File fileTest = service.files()
                        .create(metaData, content)
                        .setFields("id, parents")
                        .execute();

                downloadLink = "https://drive.google.com/file/d/" + fileTest.getId();
                song.setDownloadLink(downloadLink);
                System.out.println("Upload finished");
            }
        }
    }

}
