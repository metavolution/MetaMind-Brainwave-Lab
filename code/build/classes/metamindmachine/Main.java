/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metamindmachine;

/**
 *
 * @author rain
 */
public class Main {
    public static String datadir = "data/";
    public static String sequencedir = "sequences/";
    static boolean useusb = false;
    static boolean screengraphics = true;
    static boolean autostart = false;
    
    static int status = 1;
    static SessionManager sessionmgr = null;
    static String sequence = "";
    
    public static void main(String[] args) {

        for(int i=0;i < args.length;i++) {
            if(args[i].equals("-useusb")) {
                useusb = true;
                screengraphics = false;
            }
            if(args[i].equals("-sg")) {
                screengraphics = true;
            }
            if(args[i].contains(".mmms")) {
                sequence = args[i];
                System.out.println("autostart using sequence: "+sequence);
                autostart = true;
            }
            
        }
        System.out.println("useusb: "+useusb+" | "+"screengraphics: "+screengraphics);
        
        sessionmgr = new SessionManager();
        
        if(autostart) {
            sessionmgr.sequence.LoadSequence(sequence);
            status = 2;
        } else {
            StartFrame startframe = new StartFrame();
        }
        

        

        

        while(status != 0) {
            if(status == 2) {
                sessionmgr.StartSession();
            }
            
            
            RainLib.Sleep(100);
        }

        RainLib.Sleep(100);
        if(useusb) LEDCtrl.LEDoff();
        System.out.println("main program exit");
        System.exit(0);
        
    }

}
