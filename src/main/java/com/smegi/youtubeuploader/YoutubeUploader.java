/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smegi.youtubeuploader;

import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Folder;
import com.smegi.youtubeuploader.uploads.DriveUpload;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergej
 */
public class YoutubeUploader {
    
    public static void main(String args[]) {

        Search s = new Search();
        List<Band> bands = s.getBands(MyPaths.RESOURCES_PATH);
        List<Folder> folders = s.getFolders();

        // Generate videos
        VideoGenerator vg = new VideoGenerator(bands);
        vg.Generate();
        
        // Upload
        upload(bands);
  
       
    }

    private static void upload(List<Band> bands) {
        try {
            DriveUpload du = new DriveUpload();
            du.upload(bands);
            
            System.out.println("--------------------------------");
            System.out.println("---------FINAL CONSOLE----------");
        } catch (Exception ex) {
            Logger.getLogger(YoutubeUploader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
