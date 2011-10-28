/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metamindmachine;

/**
 *
 * @author rain
 */
public class LEDFlasher extends Thread{
    //int duration;
    double binfreq;
    int status = 1;
    int sleeptime;
    double nextfreq=0;
    double duration=1;
    boolean interrupted=false;
    
    public LEDFlasher(String str) {
        super(str);
        this.start();
    }
    public void setActive(double p_binfreq,double p_duration) {
        System.out.println("SetActive"+p_binfreq+binfreq+nextfreq);
        nextfreq = p_binfreq;
        binfreq = p_binfreq;
        status = 2;
        
    }
    
    public void run() {
      System.out.println("Thread start");  
      try {        
          
        while(status != 0) {
            if(status > 1) {
                  //System.out.println("active...");
                for(int j=0;j<binfreq*duration;j++) {

                    //System.out.println("ON");  
                    LEDCtrl.LEDon();
                    if(nextfreq > 0) {
                        sleeptime = (int)(1000/binfreq/2);
                        interrupted = true;
                    }
                    Thread.sleep(sleeptime);

                    //System.out.println("OFF");  
                    LEDCtrl.LEDoff();        
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
        LEDCtrl.LEDoff();      
        
      } catch (Throwable e) {}        
      System.out.println("LED Thread exit");
    }
  
}
