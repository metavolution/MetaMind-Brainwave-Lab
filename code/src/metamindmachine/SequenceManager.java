/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metamindmachine;


import java.io.*;
import java.util.Scanner;

import java.awt.*;

/**
 *
 * @author rain
 */
class seqformat {
    double duration=0;
    int volume=0;
    double basefreq=0;
    double binfreq=0;
    String type="";
    
    public seqformat() {
        
    }

    public seqformat(double p_duration, int p_basefreq, double p_binfreq, double p_value, String p_type) {
        duration = p_duration;
        basefreq = p_basefreq;
        binfreq = p_binfreq;
        type = p_type;
    }
    public seqformat(double p_duration, int p_basefreq, int p_volume, double p_binfreq, double p_value, String p_type) {
        duration = p_duration;
        basefreq = p_basefreq;
        binfreq = p_binfreq;
        type = p_type;
        volume = p_volume;
    }    
}


public class SequenceManager {

    int entrynum=0,length=0;
    seqformat entry[];
    
    

    public boolean LoadSequence(String filename) {
        //System.out.println(filename);
        int d=0,start=0,end=0;
        entrynum = 200; length = 0;
        entry = new seqformat[entrynum];
        String pstring="",valuestr="";
        FileInputStream fstream=null;
        Scanner s=null;
        
        
            try
            {
                fstream = new FileInputStream(filename);
                //DataInputStream in = new DataInputStream(fstream);
                s = new Scanner(fstream);

                d=0;
                while (s.hasNextLine() != false) {
                    pstring = s.nextLine();
                    
                    if(pstring.contains("<entry ")) {
                        //System.out.println(filename);
                        entry[d] = new seqformat();
                        
                        start = pstring.indexOf("type=");
                            valuestr = pstring.substring(start+6);
                            end = valuestr.indexOf('"');
                        if(start != -1 && end != -1) {
                            entry[d].type = valuestr.substring(0, end) ;
                            //System.out.println(d+entry[d].type);
                        }
                        start = pstring.indexOf("binfreq=");
                            valuestr = pstring.substring(start+9);
                            end = valuestr.indexOf('"');
                        if(start != -1 && end != -1) {
                            entry[d].binfreq = Double.parseDouble(valuestr.substring(0, end));
                            //System.out.println(entry[d].binfreq);
                        }   
                        
                        start = pstring.indexOf("duration=");
                            valuestr = pstring.substring(start+10);
                            end = valuestr.indexOf('"');
                        if(start != -1 && end != -1) {
                            entry[d].duration = Double.parseDouble(valuestr.substring(0, end));
                            //System.out.println(entry[d].duration);
                        }                        
                        start = pstring.indexOf("basefreq=");
                            valuestr = pstring.substring(start+10);
                            end = valuestr.indexOf('"');
                        if(start != -1 && end != -1) {
                            entry[d].basefreq = Double.parseDouble(valuestr.substring(0, end));
                            //System.out.println(entry[d].basefreq);
                        }  
                        start = pstring.indexOf("volume=");
                            valuestr = pstring.substring(start+8);
                            end = valuestr.indexOf('"');
                        if(start != -1 && end != -1) {
                            entry[d].volume = Integer.parseInt(valuestr.substring(0, end));
                            //System.out.println(entry[d].volume);
                        }     
                            
                        //if(entry[d].type.length() > 3)
                        if(entry[d].basefreq == 0) {
                            entry[d].basefreq = getbasefreq(entry[d].binfreq);
                        }
                        if(entry[d].volume == 0) {
                            entry[d].volume = getvolume(entry[d].basefreq);
                        }
                           
                        //--calculate length
                        if(entry[d].type.compareTo("binbeat_const") == 0) {
                            length += entry[d].duration;
                        } 
                        if(entry[d].type.compareTo("sleep") == 0) {
                            length += entry[d].duration*60;
                        }                            
                            
                        d++;
                        
                    }
                }

                //s.reset();
                s.close();
                fstream.close();
            } 
            catch (Exception e) {
                System.err.println("Sequence File input error: "+e);
                return false;
            }
  
            entry[d++] = new seqformat(0,0,0,0,"constant");
            entrynum = d;
            System.out.println("Sequence loaded - "+(entrynum-1)+" entries");
            d=0;
            return true;
    }
    
    public double getbasefreq(double p_binfreq) {
        
        if(p_binfreq == 15) return 220;
        if(p_binfreq == 14) return 218;
        if(p_binfreq == 13) return 216;
        if(p_binfreq == 12) return 214;
        if(p_binfreq == 11) return 212;
        if(p_binfreq == 10) return 210;
        if(p_binfreq ==  9) return 200;
        if(p_binfreq ==  8) return 190;
        if(p_binfreq ==  7) return 180;
        if(p_binfreq ==  6) return 170;
        if(p_binfreq ==  5) return 160;
        if(p_binfreq ==  4) return 150;
        if(p_binfreq ==  3) return 140;
        if(p_binfreq ==  2) return 130;
        if(p_binfreq ==  1) return 120;

        return 220;
    }
    
    public int getvolume(double p_basefreq) {
        
        if(p_basefreq >= 210) return 90;
        if(p_basefreq >= 170) return 95;

        return 100;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
 /*    
    SequenceManager() {
        
        int d=0;
        entry = new seqformat[entrynum];
        
        entry[d++] = new seqformat(5,220,1,0,"constant");
        entry[d++] = new seqformat(0,0,70,0,0,"meditation1.mp3");
        entry[d++] = new seqformat(5,220,2,0,"constant");
        entry[d++] = new seqformat(5,220,8,0,"constant");
        entry[d++] = new seqformat(0.1,0,0,0,"sleep");        
        entry[d++] = new seqformat(5,220,9,0,"constant");
        entry[d++] = new seqformat(5,220,15,0,"constant");

        
//short meditation
        entry[d++] = new seqformat(20,220,15,0,"constant");
        entry[d++] = new seqformat(20,219,14,0,"constant");
        entry[d++] = new seqformat(20,218,13,0,"constant");
        entry[d++] = new seqformat(20,217,12,0,"constant");
        entry[d++] = new seqformat(20,216,11,0,"constant");
        entry[d++] = new seqformat(0,0,70,0,0,"meditation1.mp3");        
        entry[d++] = new seqformat(20,210,10,0,"constant");
        entry[d++] = new seqformat(20,200, 9,0,"constant");
        entry[d++] = new seqformat(30,190, 8,0,"constant");
        entry[d++] = new seqformat(30,180, 7,0,"constant");
        entry[d++] = new seqformat(30,170, 6,0,"constant");
        entry[d++] = new seqformat(30,160, 5,0,"constant");
        entry[d++] = new seqformat(40,150, 4,0,"constant");
        
        entry[d++] = new seqformat(5,180, 7,0,"constant");
        entry[d++] = new seqformat(10,170, 6,0,"constant");
        entry[d++] = new seqformat(20,160, 5,0,"constant");
        entry[d++] = new seqformat(40,150, 4,0,"constant");
        entry[d++] = new seqformat(1,0,0,0,"sleep");        

        entry[d++] = new seqformat(5,170, 6,0,"constant");
        entry[d++] = new seqformat(10,160, 5,0,"constant");
        entry[d++] = new seqformat(30,150, 4,0,"constant");
        entry[d++] = new seqformat(1,0,0,0,"sleep");        
        
        entry[d++] = new seqformat(5,160, 5,0,"constant");
        entry[d++] = new seqformat(5,170, 6,0,"constant");
        entry[d++] = new seqformat(5,180, 7,0,"constant");
        entry[d++] = new seqformat(5,190, 8,0,"constant");
        entry[d++] = new seqformat(5,200, 9,0,"constant");
        entry[d++] = new seqformat(5,210,10,0,"constant");
        entry[d++] = new seqformat(5,216,11,0,"constant");
        entry[d++] = new seqformat(5,217,12,0,"constant");
        entry[d++] = new seqformat(5,218,13,0,"constant");
        entry[d++] = new seqformat(5,219,14,0,"constant");
        entry[d++] = new seqformat(5,220,15,0,"constant");        
        
/*
//drop to 4hz - sleep
        entry[d++] = new seqformat(0,0,90,0,0,"relax1.mp3");
        entry[d++] = new seqformat(30,220,15,0,"constant");
        entry[d++] = new seqformat(30,219,14,0,"constant");
        entry[d++] = new seqformat(30,218,13,0,"constant");
        entry[d++] = new seqformat(30,217,12,0,"constant");
        entry[d++] = new seqformat(30,216,11,0,"constant");
        entry[d++] = new seqformat(30,210,10,0,"constant");
        entry[d++] = new seqformat(30,200, 9,0,"constant");
        entry[d++] = new seqformat(40,190, 8,0,"constant");
        entry[d++] = new seqformat(40,180, 7,0,"constant");
        entry[d++] = new seqformat(40,170, 6,0,"constant");
        entry[d++] = new seqformat(60,160, 5,0,"constant");
        entry[d++] = new seqformat(60,150, 100, 4,0,"constant");
        
        entry[d++] = new seqformat(10,190, 8,0,"constant");
        entry[d++] = new seqformat(20,180, 7,0,"constant");
        entry[d++] = new seqformat(30,170, 6,0,"constant");
        entry[d++] = new seqformat(60,160, 5,0,"constant");
        entry[d++] = new seqformat(60,150, 100, 4,0,"constant");

        entry[d++] = new seqformat(10,180, 7,0,"constant");
        entry[d++] = new seqformat(20,170, 6,0,"constant");
        entry[d++] = new seqformat(40,160, 5,0,"constant");
        entry[d++] = new seqformat(60,150, 100, 4,0,"constant");
        entry[d++] = new seqformat(1,0,0,0,"sleep");
        
        entry[d++] = new seqformat(0,0,90,0,0,"relax2.mp3");
        entry[d++] = new seqformat(10,180, 7,0,"constant");
        entry[d++] = new seqformat(20,170, 6,0,"constant");
        entry[d++] = new seqformat(40,160, 5,0,"constant");
        entry[d++] = new seqformat(60,150, 100, 4,0,"constant");
        entry[d++] = new seqformat(3,0,0,0,"sleep");

        entry[d++] = new seqformat(5,180, 7,0,"constant");
        entry[d++] = new seqformat(10,170, 6,0,"constant");
        entry[d++] = new seqformat(20,160, 5,0,"constant");
        entry[d++] = new seqformat(60,150, 100, 4,0,"constant");
        entry[d++] = new seqformat(5,0,0,0,"sleep");

        entry[d++] = new seqformat(5,180, 7,0,"constant");
        entry[d++] = new seqformat(10,170, 6,0,"constant");
        entry[d++] = new seqformat(20,160, 5,0,"constant");
        entry[d++] = new seqformat(60,150, 100, 4,0,"constant");
        
/*        
//wakeup
        entry[d++] = new seqformat(20,210,10,0,"constant");
        entry[d++] = new seqformat(20,200, 9,0,"constant");
        entry[d++] = new seqformat(20,190, 8,0,"constant");
        entry[d++] = new seqformat(20,180, 7,0,"constant");
        entry[d++] = new seqformat(20,170, 6,0,"constant");
        entry[d++] = new seqformat(20,160, 5,0,"constant");
        
        entry[d++] = new seqformat(20,170, 6,0,"constant");
        entry[d++] = new seqformat(20,180, 7,0,"constant");
        entry[d++] = new seqformat(20,190, 8,0,"constant");
        entry[d++] = new seqformat(20,200, 9,0,"constant");
        entry[d++] = new seqformat(20,210,10,0,"constant");
        entry[d++] = new seqformat(20,216,11,0,"constant");
        entry[d++] = new seqformat(20,217,12,0,"constant");
        entry[d++] = new seqformat(20,218,13,0,"constant");
        entry[d++] = new seqformat(20,219,14,0,"constant");
        entry[d++] = new seqformat(40,220,15,0,"constant");
        
        entry[d++] = new seqformat(20,210,10,0,"constant");
        entry[d++] = new seqformat(20,216,11,0,"constant");
        entry[d++] = new seqformat(20,217,12,0,"constant");
        entry[d++] = new seqformat(20,218,13,0,"constant");
        entry[d++] = new seqformat(20,219,14,0,"constant");
        entry[d++] = new seqformat(60,220,15,0,"constant");

        entry[d++] = new seqformat(20,217,12,0,"constant");
        entry[d++] = new seqformat(40,218,13,0,"constant");
        entry[d++] = new seqformat(60,219,14,0,"constant");
        entry[d++] = new seqformat(120,220,15,0,"constant");

        


              
        
        
        entry[d++] = new seqformat(0,0,0,0,"constant"); 
        entrynum = d;
    }
*/    
    

}
