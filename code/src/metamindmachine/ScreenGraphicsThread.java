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
import java.io.*;
import java.net.*;

/**
 *
 * @author rain
 */
public class ScreenGraphicsThread extends TrainingType implements Runnable,KeyListener
{
   public GraphicsDevice gd;
   Graphics g = null;
   
   public Thread thread;
   
   int x = 0;
   double binfreq;
   int status = 1;
   int sleeptime;
   double nextfreq=0;
   double duration=1;
   boolean interrupted=false;
   
   URL imageurl = this.getClass().getResource("pentagram2.gif");
   Image image = new ImageIcon(imageurl).getImage();
   int width = image.getWidth(null);
   int height = image.getHeight(null);
   ImageObserver obs = null;

   
   ScreenGraphicsThread(String str)
   {
      thread = new Thread(this,str);
      gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
      frame = new Frame(gd.getDefaultConfiguration());
      frame.setUndecorated(true);
      frame.setIgnoreRepaint(true);
      frame.setResizable(false);
      frame.addKeyListener(this);
      frame.setFocusable(true);
      gd.setFullScreenWindow(frame);
      frame.setBackground(Color.BLACK);
      frame.createBufferStrategy(2);
      buf = frame.getBufferStrategy();
      
      super.init(frame,buf);
      frame.requestFocus();
            
      thread.start();
   }
   
   public void run()
   {
      System.out.println("ScreenGraphicsThread start");  
      
      //--initial Graphics
      int i=0,w=30,h=30;
      int range=20;
      
      String wordlist[] = {"relaxing","breathing","focusing","widening","brain","Gehirn","energy","power"};
      title = "MetaMindMachine";
      info = "relax body and mind and close eyes or focus on center";
      g = buf.getDrawGraphics();


      
      try {        
          
        while(status != 0) {
            if(status > 1) {
                  //System.out.println("active...");
                for(int j=0;j<binfreq*duration;j++) {

                    //System.out.println("ON");  
                    ScreenFlashColor(Color.RED);
                    if(nextfreq > 0) {
                        sleeptime = (int)(1000/binfreq/2);
                        interrupted = true;
                    }
                    Thread.sleep(sleeptime);

                    //System.out.println("OFF");  
                    ScreenFlashColor(Color.BLACK);
                    if(nextfreq > 0) {
                        sleeptime = (int)(1000/binfreq/2);
                        interrupted = true;
                    }
                    Thread.sleep(sleeptime);

                    if(interrupted) {
                        nextfreq = 0;
                        j = (int)binfreq+1;
                        interrupted = false;
                    }
                }
            }
            else {
              //System.out.println("Sleeping...");
              Thread.sleep(10);
            }
          
        }
              
        
      } catch (Throwable e) {}        
      System.out.println("Thread exit");

      //RainLib.Sleep(2000);


   }
   public void ScreenFlashColor(Color color) {
        //frame.setBackground(color);
        g.setColor(color);
        //g.fillRect(0, 0, 800, 600);
        g.fillRect(0, 0, screenWidth, screenHeight);         

        g.setColor(Color.black);
        g.fillRect(midx-150, midy-100, 300, 200);         

        g.drawImage(image, midx-width/2, midy-height/2, obs);
        
        paintInfo();
        buf.show();   
   }
   
    public void paintInfo() {
        g.setFont(font1);        
        g.setColor(Color.white);
        RainLib.CenterString(title,midx,25,g);
        
        g.setFont(font2);        
        RainLib.CenterString(info,midx,40,g);

        RainLib.CenterString("Brainwave Hz: "+binfreq,midx,55,g);
    }

   public void setActive(double p_binfreq,double p_duration) {
        System.out.println("SetActive"+p_binfreq+binfreq+nextfreq);
        nextfreq = p_binfreq;
        binfreq = p_binfreq;
        status = 2;
        
   }    
    
   public void keyPressed(KeyEvent e)
   {
      if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
      {
         System.out.println("program exit requested");
         Main.status = 0;
         frame.setVisible(false);
         frame.dispose();
         //thread.stop();
         
      }
   }
   
   public void keyReleased(KeyEvent e)
   {
   }
   
   public void keyTyped(KeyEvent e)
   {
   }
}