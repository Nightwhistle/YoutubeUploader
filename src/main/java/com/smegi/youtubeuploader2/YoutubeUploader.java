/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smegi.youtubeuploader2;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Sergej
 */
public class YoutubeUploader {

    public static void main(String args[]) throws IOException, InterruptedException {

        ImageGenerator ig = new ImageGenerator();
        ig.generateImage();
        VideoGenerator vg = new VideoGenerator();
        vg.Generate();
        
    }
}
