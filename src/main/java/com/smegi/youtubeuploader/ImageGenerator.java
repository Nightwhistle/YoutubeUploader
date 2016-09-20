package com.smegi.youtubeuploader;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static javafx.scene.text.Font.font;
import javax.imageio.ImageIO;

/**
 *
 * @author Sergej
 */
public class ImageGenerator {

    private final String backgroundPath;
    private final String bandName;
    private final String songName;

    public ImageGenerator(String backgroundPath, String bandName, String songName) {
        this.backgroundPath = backgroundPath;
        this.bandName = bandName;
        this.songName = songName;
    }

    public void generateImage() throws IOException, FontFormatException {
        BufferedImage image = ImageIO.read(new File(backgroundPath));
        Graphics2D graphic = (Graphics2D) image.getGraphics();
        graphic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Windows\\Fonts\\footlight-mt-light.ttf")));

        // Draw top rectangle
        Color rectangleColor = new Color(0, 0, 0, 160);
        graphic.setColor(rectangleColor);
        graphic.fillRect(0, 100, 1920, 200);

        // Draw bottom rectangle
        graphic.setColor(rectangleColor);
        graphic.fillRect(0, 1080 - 170, 1920, 170);

        // Write Band Name
        Color c = new Color(176, 202, 213);
        graphic.setColor(c);
        Font footlightFont = new Font("Footlight MT Light", Font.PLAIN, 90);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        
        attributes.put(TextAttribute.TRACKING, -0.05);
        Font footlightFontTracking = footlightFont.deriveFont(attributes);
        graphic.setFont(footlightFontTracking);
        
        FontMetrics fontMetrics = graphic.getFontMetrics();
        graphic.drawString(bandName, 1400 - fontMetrics.stringWidth(bandName), 200);
        System.out.println(bandName + " - " + fontMetrics.stringWidth(bandName));

        // Write Song Name
        fontMetrics = graphic.getFontMetrics();
        graphic.setFont(footlightFontTracking);
        graphic.drawString(songName, 1400 - fontMetrics.stringWidth(songName), 300);
        System.out.println(songName + " - " + fontMetrics.stringWidth(songName));

        // Write download link
        ImageIO.write(image, "jpg", new File("d:\\OutImage.jpg"));
    }
}
