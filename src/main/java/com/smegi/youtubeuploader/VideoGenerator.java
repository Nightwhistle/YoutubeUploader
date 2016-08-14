package com.smegi.youtubeuploader;

import com.smegi.youtubeuploader.Model.Band;
import com.smegi.youtubeuploader.Model.Song;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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

                    String[] cmd = {"ffmpeg", "-y", "-loop", "1", "-i", imagePath, "-i", audioPath, "-c:v", "libx264", "-tune", "stillimage", "-c:a", "aac", "-strict", "experimental", "-b:a", "192k", "-pix_fmt", "yuv420p", "-shortest", outPath};

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
                    System.out.println(Arrays.toString(cmd));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}