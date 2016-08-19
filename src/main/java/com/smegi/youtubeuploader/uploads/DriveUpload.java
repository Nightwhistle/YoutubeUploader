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
import com.google.api.services.drive.model.FileList;
import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Song;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DriveUpload {

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
        Reader clientSecretReader = new InputStreamReader(new FileInputStream(new java.io.File("D:\\NetBeansProjects\\YoutubeUploader\\src\\main\\resources\\client_secrets.json")));
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

    public void upload(List<Band> bands) throws IOException, Exception {
        // Build a new authorized API client service.
        Drive service = getDriveService();

        // Folder TEST on smegibrn@gmail drive
        String folderId = "0B6zT2QOu7WjjTW56aEp4YzRxa2M";

        // Loop through bands provided
        for (Band band : bands) {
            for (Song song : band.getSongs()) {
                // Creating download link
                String downloadLink;
                
                // Setting metadate for file
                File metaData = new File();
                metaData.setName(song.getName());
                metaData.setParents(Collections.singletonList(folderId));

                // Loading file
                java.io.File filePath = new java.io.File(song.getPath());
                FileContent content = new FileContent("audio/mp3", filePath);

                // Creating upload file
                File fileTest = service.files()
                        .create(metaData, content)
                        .setFields("id, parents")
                        .execute();
                
                downloadLink = "https://drive.google.com/file/d/" + fileTest.getId();
                song.setDownloadLink(downloadLink);
                
            }
        }
    }

}
