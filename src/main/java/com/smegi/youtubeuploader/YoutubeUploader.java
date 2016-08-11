/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smegi.youtubeuploader;

import com.smegi.youtubeuploader.Model.*;
import java.util.List;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Sergej
 */
public class YoutubeUploader {

    public static void main(String args[]) {

        List<Band> bands = new ArrayList<>();
        searchFolder("e:\\projects\\youtubeuploader", bands);

    }

    public static void searchFolder(String directoryName, List<Band> bands) {
        File directory = new File(directoryName);
        File[] filesList = directory.listFiles();

        Band band = new Band();

        for (File file : filesList) {
            band.setName(file.getName());
            if (file.isFile()) {
                Song song = new Song();
                song.setName(file.getName());
                song.setPath(file.getAbsolutePath());
                band.addSong(song);
            } else if (file.isDirectory()) {
                bands.add(band);
                searchFolder(file.getAbsolutePath(), bands);
            }
        }

    }
}
