/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metamindmachine;

/**
 *
 * @author rain
 */



public class SessionManager {

    SequenceManager sequence;
    String seqfile=null;
    
    int SAMPLE_RATE = StdAudio.SAMPLE_RATE;
    double[] a = new double[SAMPLE_RATE*2+1]; 
    
    
    
    public SessionManager() {
        sequence = new SequenceManager();
    }
    
    public void StartSession() {
        
    try {        
        seqformat entry;
  
        LEDFlasher ledthread = null;
        ScreenGraphicsThread sgthread = null;
        
        //init threads
        if(Main.useusb) {
            LEDCtrl.init();
            ledthread = new LEDFlasher("LEDFlasher1");
            //ledthread.setPriority(1);
        }
        if(Main.screengraphics) {
            sgthread = new ScreenGraphicsThread("ScreenGraphicsThread1");
        }
        
        //--loop entries
        for(int j=0;j < sequence.entrynum;j++) { 
            entry = sequence.entry[j];            
            
            System.out.println("entry:"+entry.type+entry.duration+entry.binfreq);
            //--special handling for entries
            if(entry.type.compareToIgnoreCase("sleep") == 0) {
                if(Main.screengraphics) sgthread.status = 1;
                if(Main.useusb) ledthread.status = 1;
                RainLib.Sleep(1000*60*entry.duration);
                continue;
            }
            if(entry.type.contains(".mp3")) {
                if(Main.screengraphics) sgthread.status = 1;
                if(Main.useusb)ledthread.status = 1;
                mp3playThread mp3thread = new mp3playThread(entry.type,entry.volume);
                mp3thread.start();
                continue;
            }
            if(entry.duration == 0) {
                System.out.println("end of sequence reached");
                break;
            }
            
            //--give entry parameters to flasher threads
            if(Main.useusb) {
                if(j==0) ledthread.binfreq = entry.binfreq;
                ledthread.setActive(entry.binfreq,entry.duration);
            }
            if(Main.screengraphics) {
                if(j==0) sgthread.binfreq = entry.binfreq;
                sgthread.setActive(entry.binfreq,entry.duration);
            }

            
            //--play sound sample
            for(int sample=0;sample < entry.duration;sample++) {
                //fill array with 1 sec of stereo 16-bit tonedata
                for (int i = 0; i < SAMPLE_RATE*2; i++) {
                   a[i] = Math.sin(2*Math.PI * i  / SAMPLE_RATE * entry.basefreq/2); 
                   a[i+1] = Math.sin(2*Math.PI * i / SAMPLE_RATE * (entry.basefreq-entry.binfreq)/2);
                   i++;
                }

                //System.out.println("playing sample: "+sample);
                //StdAudio.toneplayled(0,a,SAMPLE_RATE*2,entry[j].binfreq);
                //StdAudio.play(a);
                
                StdAudio.toneplay(0,entry.volume,a,SAMPLE_RATE*2);
                if(Main.status == 0) break;
            }  
            if(Main.status == 0) break;
        }

        if(Main.useusb) {
            ledthread.status = 0;
            LEDCtrl.LEDoff();
        }    
        if(Main.screengraphics) {
            sgthread.status = 0;
        }    
        Main.status = 0;
        

    } catch (Throwable e) {
        System.out.println("Exception: "+e.toString());
        }
    }

}
