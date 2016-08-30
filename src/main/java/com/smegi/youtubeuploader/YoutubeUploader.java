/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smegi.youtubeuploader;

import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Folder;
import com.smegi.youtubeuploader.Model.Song;
import com.smegi.youtubeuploader.uploads.DriveUpload;
import java.util.List;

/**
 *
 * @author Sergej
 */
public class YoutubeUploader {
    
    public static void main(String args[]) throws Exception {

        Search s = new Search();
        List<Band> bands = s.getBands(MyPaths.RESOURCES_PATH);
        List<Folder> folders = s.getFolders();

        VideoGenerator vg = new VideoGenerator(bands);
        vg.Generate();

        DriveUpload du = new DriveUpload();
        du.upload(bands);

//        UploadVideo uv = new UploadVideo();
//        uv.upload(bands);

        System.out.println("--------------------------------");
        System.out.println("---------FINAL CONSOLE----------");

        for (Band band : bands) {
            System.out.println("Folder: " + band.getName() + " - " + band.getFolderId());
            for (Song song : band.getSongs()) {
                System.out.printf("%s - %s [%s]%n", band.getName(), song.getName(), song.getPath());
            }
        }
        
        System.out.println("--------------------------------");
        
        for (Folder folder : folders) {
            System.out.printf("%s - %s %n", folder.getName(), folder.getId());
        }
    }
}
