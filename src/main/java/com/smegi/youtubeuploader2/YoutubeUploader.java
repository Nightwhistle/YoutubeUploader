/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smegi.youtubeuploader2;

import java.io.IOException;

/**
 *
 * @author Sergej
 */
public class YoutubeUploader {
    public static void main(String args[]) throws IOException, InterruptedException {
        
        String[] cmd = {"ffmpeg", "-loop", "1", "-i", "e:\\Projects\\FFMPEG test\\image1.jpg", "-i", "e:\\Projects\\FFMPEG test\\audio1.mp3", "-c:v", "libx264", "-tune", "stillimage", "-c:a", "aac", "-strict", "experimental", "-b:a", "192k", "-pix_fmt", "yuv420p", "-shortest", "out1.mp4"};
        String[] cmd2 = {"ffmpeg", "-loop", "1", "-i", "e:\\Projects\\FFMPEG test\\image2.jpg", "-i", "e:\\Projects\\FFMPEG test\\audio2.mp3", "-c:v", "libx264", "-tune", "stillimage", "-c:a", "aac", "-strict", "experimental", "-b:a", "192k", "-pix_fmt", "yuv420p", "-shortest", "out2.mp4"};

Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
Process p2 = new ProcessBuilder(cmd2).redirectErrorStream(true).start();

    }
}
