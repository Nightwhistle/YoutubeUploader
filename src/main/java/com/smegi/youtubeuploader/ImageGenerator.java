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
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this.songName = songName.substring(bandName.length() + 3, songName.length() - 4);
    }

    // Returning path to generated image (It's D:/ hardcoded);
    public String generateImage() throws IOException {
        BufferedImage image = ImageIO.read(new File(backgroundPath));
        Graphics2D graphic = (Graphics2D) image.getGraphics();
        graphic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Windows\\Fonts\\footlight-mt-light.ttf")));
        } catch (FontFormatException ex) {
            Logger.getLogger(ImageGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Draw top rectangle
        Color rectangleColor = new Color(0, 0, 0, 160);
        graphic.setColor(rectangleColor);
        graphic.fillRect(0, 100, 1920, 220);

        // Draw bottom rectangle
        graphic.setColor(rectangleColor);
        graphic.fillRect(0, 900, 1920, 100);

        // Write Band Name
        Color c = new Color(226, 240, 246);
        graphic.setColor(c);
        Font footlightFont = new Font("Footlight MT Light", Font.BOLD, 160);
        Map<TextAttribute, Object> attributes = new HashMap<>();

        attributes.put(TextAttribute.TRACKING, -0.05);
        Font footlightFontTracking = footlightFont.deriveFont(attributes);
        graphic.setFont(footlightFontTracking);

        FontMetrics fontMetrics = graphic.getFontMetrics();
        graphic.drawString(bandName, 1800 - fontMetrics.stringWidth(bandName), 220);
        System.out.println(bandName + " - " + fontMetrics.stringWidth(bandName));

        // Write Song Name
        double tracking = -0.05;
        Color cs = new Color(176, 202, 213);
        graphic.setColor(cs);
        Font footlightFontSongName = new Font("Footlight MT Light", Font.PLAIN, 90);
        Map<TextAttribute, Object> attributesSongName = new HashMap<>();
        attributesSongName.put(TextAttribute.TRACKING, tracking);
        Font footlightFontTrackingSongName = footlightFontSongName.deriveFont(attributesSongName);
        graphic.setFont(footlightFontTrackingSongName);

        boolean fixingFont = false;
        Font fixedSongNameFont;

        while (true) {
            System.out.println("Still looping");
            attributesSongName.clear();
            tracking = tracking - 0.001;
            attributesSongName.put(TextAttribute.TRACKING, tracking);
            fixedSongNameFont = footlightFontSongName.deriveFont(attributesSongName);
            graphic.setFont(fixedSongNameFont);
            fontMetrics = graphic.getFontMetrics();
            if (fontMetrics.stringWidth(songName) < 1800) {
                break;
            }
        }

        System.out.println(songName + " - " + fontMetrics.stringWidth(songName));
        graphic.drawString(songName, 1800 - fontMetrics.stringWidth(songName), 300);

        // Write download link
        Font footlightFontDownloadLink = new Font("Footlight MT Light", Font.PLAIN, 70);
        Map<TextAttribute, Object> attributesDownloadLink = new HashMap<>();
        attributesDownloadLink.put(TextAttribute.TRACKING, -0.05);
        Font footlightFontTrackingDownloadLink = footlightFontDownloadLink.deriveFont(attributesDownloadLink);
        graphic.setFont(footlightFontTrackingDownloadLink);

        fontMetrics = graphic.getFontMetrics();
        graphic.drawString("Download link in description", 200, 970);
        System.out.println(backgroundPath);
        // Write image
        ImageIO.write(image, "jpg", new File("d:\\OutImage.jpg"));
        return "d:\\OutImage.jpg";
    }
}
