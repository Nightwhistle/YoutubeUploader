package com.smegi.youtubeuploader;

import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Song;
import com.smegi.youtubeuploader.Model.MusicVideo;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Sergej
 */
public class VideoGenerator {

    private List<Band> bands;

    public VideoGenerator(List<Band> bands) {
        this.bands = bands;
    }

    public void Generate() {
        // Loop through all songs and create videos from background + mp3
        try {
            for (Band band : bands) {
                for (Song song : band.getSongs()) {

                    String imagePath = band.getBackground();
                    String audioPath = song.getPath();
                    String outPath = band.getPath() + "\\" +song.getName().substring(0, song.getName().length()-4) + ".mp4";
                    // Skipping duplicates, 1FPS, libx264 video codec and copy audio in same folder where sources are
                    String[] cmd = {"ffmpeg", "-hide_banner", "-n", "-loop", "1", "-framerate", "1", "-i", imagePath, "-i", audioPath, "-c:v", "libx264", "-tune", "stillimage", "-c:a", "copy", "-strict", "experimental", "-b:a", "192k", "-pix_fmt", "yuv420p", "-shortest", outPath};

                    Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
                    final InputStream pOut = p.getInputStream();
                    Thread outputDrainer = new Thread() {
                        @Override
                        public void run() {
                            try {
                                int c;
                                do {
                                    c = pOut.read();
                                    if (c >= 0) {
                                        System.out.print((char) c);
                                    }
                                } while (c >= 0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    outputDrainer.start();
                    p.waitFor();
                    
                    // Create music video with (HD SOUND) at end of name
                    // Put it in bands list for later use
                    MusicVideo video = new MusicVideo();
                    String videoName = song.getName().substring(0, song.getName().length()-4) + " (HD SOUND)";
                    video.setName(videoName);
                    video.setPath(outPath);
                    
                    // getting video size
                    java.io.File f = new java.io.File(outPath);
                    video.setSize(f.length());
                    
                    // setting video in Music object
                    song.setMusicVideo(video);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
