/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metamindmachine;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import java.lang.Object.*;
import javax.sound.sampled.*;

/**
 *
 * @author rain
 */
public class RainLib {
    

    public static int CenterString(String outstr, int midx, int y, Graphics g) {
        FontMetrics fontMetrics = g.getFontMetrics();
        Rectangle2D area = fontMetrics.getStringBounds(outstr, g);
        g.drawString(outstr, (int)(midx-area.getWidth()/2), y);          
        return (int)(area.getWidth());
    }
    
    public static void Sleep(long millisecs) {
        try {
        Thread.sleep(millisecs);
        } catch (Throwable e) {}
    }
    public static void Sleep(double millisecs) {
        Sleep((int)millisecs);
    }

    
    public static char getRandomSymbol() {
        Random rand = new Random();
        
        return (char)(33+rand.nextInt(93));
    }
}
