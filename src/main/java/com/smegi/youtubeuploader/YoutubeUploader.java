/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smegi.youtubeuploader;

import com.smegi.youtubeuploader.Model.Band;
import java.util.List;

/**
 *
 * @author Sergej
 */
public class YoutubeUploader {

    public static void main(String args[]) {
        
        
        
        Search s = new Search();
        List<Band> bands = s.getBands("e:\\projects\\youtubeuploader");
        VideoGenerator vg = new VideoGenerator(bands);
        vg.Generate();
        
    }
}
