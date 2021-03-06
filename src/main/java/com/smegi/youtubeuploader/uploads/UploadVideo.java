/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.smegi.youtubeuploader.uploads;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.MusicVideo;
import com.smegi.youtubeuploader.Model.Song;
import com.smegi.youtubeuploader.MyPaths;
import com.smegi.youtubeuploader.Search;
import java.io.FileInputStream;
import java.io.FileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Upload a video to the authenticated user's channel. Use OAuth 2.0 to
 * authorize the request. Note that you must add your video files to the project
 * folder to upload them with this application.
 *
 * @author Jeremy Walker
 */
public class UploadVideo {

    private int videosUploaded = 0;
    private int numberOfVideos = Search.numberOfSongs;

    
    private static YouTube youtube;

    
    private static final String VIDEO_FILE_FORMAT = "video/*";
    
    private List<Band> bands;

    public UploadVideo() {
        try {
            youtube = YouTubeAuthorize.getDriveService();
        } catch (Exception ex) {
            Logger.getLogger(UploadVideo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Upload the user-selected video to the user's YouTube channel. The code
     * looks for the video in the application's project folder and uses OAuth
     * 2.0 to authorize the API request.
     *
     * @param bands
     */
    public boolean upload(Song song) throws Exception {
        this.bands = bands;
        // This OAuth 2.0 access scope allows an application to upload files
        // to the authenticated user's YouTube channel, but doesn't allow
        // other types of access.

        // Authorize the request.
        // This object is used to make YouTube Data API requests.
        try {
            MusicVideo musicVideo = song.getMusicVideo();
            System.out.printf("[%d/%d] Uploading: %s%n", ++videosUploaded, numberOfVideos, musicVideo.getName());

            // Add extra information to the video before uploading.
            Video videoObjectDefiningMetadata = new Video();

            // Set the video to be publicly visible. This is the default
            // setting. Other supporting settings are "unlisted" and "private."
            VideoStatus status = new VideoStatus();
            status.setPrivacyStatus("public");
            videoObjectDefiningMetadata.setStatus(status);

            // Most of the video's metadata is set on the VideoSnippet object.
            VideoSnippet snippet = new VideoSnippet();

            snippet.setTitle(musicVideo.getName());
            snippet.setDescription(
                    "Download link: " + song.getShortUrl());

            // Set the keyword tags that you want to associate with the video.
            snippet.setTags(song.getTags());

            // Add the completed snippet object to the video resource.
            videoObjectDefiningMetadata.setSnippet(snippet);
            InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT, new FileInputStream(musicVideo.getPath()));

            // Insert the video. The command sends three arguments. The first
            // specifies which information the API request is setting and which
            // information the API response should return. The second argument
            // is the video resource that contains metadata about the new video.
            // The third argument is the actual video content.
            YouTube.Videos.Insert videoInsert = youtube.videos()
                    .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

            // Set the upload type and add an event listener.
            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

            // Indicate whether direct media upload is enabled. A value of
            // "True" indicates that direct media upload is enabled and that
            // the entire media content will be uploaded in a single request.
            // A value of "False," which is the default, indicates that the
            // request will use the resumable media upload protocol, which
            // supports the ability to resume an upload operation after a
            // network interruption or other transmission failure, saving
            // time and bandwidth in the event of network failures.
            uploader.setDirectUploadEnabled(false);
            uploader.setChunkSize(MediaHttpUploader.MINIMUM_CHUNK_SIZE);
            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            //   System.out.println("Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            //   System.out.println("Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            double progress = (double) uploader.getNumBytesUploaded() / musicVideo.getSize() * 100;
                            System.out.printf("    Upload percentage: %.2f%% \r", progress);
                            break;
                        case MEDIA_COMPLETE:
                            System.out.println("    Youtube upload completed!");
                            Files.delete(Paths.get(musicVideo.getPath()));
                            break;
                        case NOT_STARTED:
                            System.out.println("    Upload Not Started!");
                            break;
                    }
                }
            };
            uploader.setProgressListener(progressListener);

            // Call the API and upload the video.
            Video returnedVideo = videoInsert.execute();

            // Print data about the newly inserted video from the API response.
//                    System.out.println("\n================== Returned Video ==================\n");
//                    System.out.println("  - Id: " + returnedVideo.getId());
//                    System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
//                    System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
//                    System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
//                    System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());
            System.out.println("    Uploaded successfully");
            String filePath = MyPaths.RESOURCES_PATH + "/uploadedSongs.txt";
            FileWriter fw = new FileWriter(filePath, true);
            fw.write(returnedVideo.getSnippet().getTitle() + "," + returnedVideo.getId() + "\r\n");
            fw.close();
            return true;
        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {

            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Throwable t) {

            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
            return false;
        }
    }

}
