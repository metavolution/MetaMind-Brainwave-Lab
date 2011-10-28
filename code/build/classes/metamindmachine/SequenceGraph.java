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

/**
 *
 * @author rain
 */
public class SequenceGraph extends JPanel  {
    
    
    public void paint (Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        Dimension size = getSize();
        int width = size.width;
        int height = size.height;
        int[] sy= new int[1000];
        int[] gy= new int[1000];
        int d=0,e=0;
        SequenceManager sequence = Main.sessionmgr.sequence;
        seqformat[] entry = sequence.entry;
        int maxy = 20;
        double uysize = height/maxy;
        double uxsize=0;
        
        //sequence.entrynum = 20;
        int xvalnum=0;
        for(int i=0;i < sequence.entrynum;i++) {
            xvalnum += entry[i].duration;
        }
        
        
        uxsize = width/xvalnum;
        //--iterate sequencearray and get x,y values
        for(int i=0;i < sequence.entrynum;i++) {
            //System.out.println();
            double duration = entry[i].duration;
            
            if(uxsize < 1) {
                //System.out.println("scale down");
                duration = duration*uxsize;
            }
            if(entry[i].type.equals("binbeat_const")) {
                for(int j=0;j <= duration;j++) {
                    sy[d++] = (int)entry[i].binfreq;
                    
                }
            }
        }
        uxsize = width/d;
        
        //--scale graph to canvas size
        //--enlarge graph
        if(uxsize >= 1) {
            for(int i=0;i < d;i++) {
                for(int j=0;j < uxsize;j++) {
                    gy[e++] = (int)(height-4-sy[i]*uysize);
                }
            }
        }
        
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, width, height);
        
        

        //--draw legend info
        g.setColor(new Color(111,111,111));
        int hz=maxy;
        for(int i=0;i < height;i+=uysize) {
            g.drawString(Integer.toString(hz)+"Hz", 1, i);
            g.drawLine(0,i,width,i);
            hz--;
        }
        
        //--draw graph
        g.setColor(Color.blue);
        for(int x=0;x < e;x++) {
            //System.out.println("drawing");
            g.drawLine(30+x,gy[x],30+x,gy[x]);
            g.drawLine(30+x,gy[x]-1,30+x,gy[x]-1);
            g.drawLine(30+x,gy[x]+1,30+x,gy[x]+1);
        }
        

        //g2d.drawPolyline(sy, gy, d);
        
    }

}
