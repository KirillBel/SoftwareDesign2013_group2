/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.graphview;

import geometry.Rect;
import geometry.Vec2;
import graphview.shapes.ImageShape;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author IvanKhozyainov
 */
public class ImageShapeTest {

    static final int srcWidth = 5;
    static final int srcHeight = 3;
    static final Rectangle srcRect = new Rectangle(0, 0, srcWidth, srcHeight);
    static GraphicsEnvironment ge;
    static GraphicsDevice gd;
    static GraphicsConfiguration gc;
    static BufferedImage src;

    public ImageShapeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gc = gd.getDefaultConfiguration();
        src = gc.createCompatibleImage(srcWidth, srcHeight);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of draw method, of class ImageShape.
     */
    @Test
    public void testDraw() {
        System.out.println("draw");
        assertNotNull(src);
        Graphics2D sg = (Graphics2D) src.createGraphics();
        sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        boolean result = sg.drawImage(src, 0, 0, srcWidth, srcHeight, null);
        sg.dispose();
        boolean expResult = true;
        assertEquals(expResult, result);
    }
}