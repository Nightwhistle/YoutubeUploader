/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smegi.youtubeuploader2;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Sergej
 */
public class YoutubeUploader {

    public static void main(String args[]) {

        ArrayList<File> files = new ArrayList<File>();
        searchFolder("src", files);
        
        for (File f : files) {
            System.out.println(f.getAbsoluteFile());
        }
    }
    
    public static void searchFolder(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);
        
        File[] filesList = directory.listFiles();
        
        for (File file : filesList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                searchFolder(file.getAbsolutePath(), files);
            }
        }
    }
}
