package com.smegi.youtubeuploader;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Sergej
 */
public class ImageGenerator {
    public void generateImage() throws IOException {
        BufferedImage image = ImageIO.read (new File("e:\\Projects\\FFMPEG test\\image1.jpg"));
        Graphics graphic = image.getGraphics();
        
        graphic.setFont(new Font("Georgia", Font.PLAIN, 50));
        graphic.drawString("ASDF", 50, 50);
        ImageIO.write(image, "jpg", new File("e:\\Projects\\FFMPEG test\\OutImage.jpg"));
    }
}
