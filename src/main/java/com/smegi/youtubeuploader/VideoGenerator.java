package com.smegi.youtubeuploader;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Sergej
 */
public class VideoGenerator {
    
    public void Generate() throws IOException, InterruptedException {
        for (int i = 1; i <= 2; i++) {
            String imagePath = String.format("e:\\Projects\\FFMPEG test\\image%s.jpg", i);
            String audioPath = String.format("e:\\Projects\\FFMPEG test\\audio%s.mp3", i);
            String outPath = String.format("e:\\Projects\\FFMPEG test\\out%s.mp4", i);

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
                    }
                }
            };
            outputDrainer.start();
            p.waitFor();
        }
    }
}
