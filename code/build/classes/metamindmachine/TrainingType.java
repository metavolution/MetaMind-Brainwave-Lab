/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metamindmachine;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.util.Date;
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
public class TrainingType {
    Date start;
    Date end;
    int rounds;
    int complexity;
    Date[] reactiontime;
    boolean[] problem;
    boolean[] response;
    Frame frame;
    BufferStrategy buf;
    Random rand;
    String title;
    String info;
    int screenWidth;
    int screenHeight;
    int midx;
    int midy;    
    Font font1 = new Font("Serif", Font.PLAIN, 30);
    Font font2 = new Font("Serif", Font.PLAIN, 15);
    Font font3 = new Font("Serif", Font.PLAIN, 50);
    
    public TrainingType() {
    }

    public TrainingType(Frame frame1, BufferStrategy buf1) {
        init(frame1,buf1);
    }    
    
    public void init(Frame frame1, BufferStrategy buf1) {
        frame = frame1;
        buf = buf1;
        screenWidth = frame.getWidth();
        screenHeight = frame.getHeight();
        midx = screenWidth/2;
        midy = screenHeight/2;    
        
        Date date = new Date();
        rand = new Random(date.getTime());        

        Graphics g = buf.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, screenWidth, screenHeight);         
        g.dispose();
        buf.show();        
    } 
    
    public void paintInfo() {
        Graphics g = buf.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, screenWidth, screenHeight);        
        
        g.setFont(font1);        
        g.setColor(Color.white);
        RainLib.CenterString(title,midx,50,g);
        
        g.setFont(font2);        
        RainLib.CenterString(info,midx,70,g);

    }

    public void paintStats() {
        
    }

    
    public TrainingType(int p_rounds) {
        rounds = p_rounds;
        problem = new boolean[rounds];
        
        start = new Date();
                
        //fill problem array
        Random rand3 = new Random(start.getTime());
        System.out.println("rounds: "+rounds);
        for (int i=0;i<rounds;i++) {
            problem[i] = rand3.nextBoolean(); rand3.nextInt(5);
            System.out.println(problem[i]);
        }
 
       
           
    }
    
}