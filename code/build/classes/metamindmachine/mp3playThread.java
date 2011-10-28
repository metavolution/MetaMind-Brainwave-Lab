/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metamindmachine;

import java.io.*;
import javax.sound.sampled.*;


/**
 *
 * @author rain
 */
public class mp3playThread extends Thread{
    String filename;
    int volume=100;
    
    public mp3playThread(String p_filename,int p_volume){
        super(p_filename);
        filename = p_filename;
        volume = p_volume;
    }
    
    public void run() {
        mp3Play(filename);
        
        
    }
    
    public void mp3Play(String filename)
    {
      try {
        File file = new File(Main.datadir+filename);
        AudioInputStream in= AudioSystem.getAudioInputStream(file);
        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat();//StdAudio.getAudioFormat();
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                                                                      baseFormat.getSampleRate(),
                                                                                      16,
                                                                                      baseFormat.getChannels(),
                                                                                      baseFormat.getChannels() * 2,
                                                                                      baseFormat.getSampleRate(),
                                                                                      false);
        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        // Play now.
        rawplay(decodedFormat, din);
        in.close();
      } catch (Exception e)
        {
            //Handle exception.
            System.out.println("exception: "+e.toString());
        }
    }

    private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException,                                                                                                LineUnavailableException
    {
      byte[] data = new byte[4096];
      System.out.println("playing mp3 sample: "+filename);
      SourceDataLine line = getLine(targetFormat);
      if (line != null)
      {
        FloatControl balctrl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        balctrl.setValue(-35+(int)(0.35*volume));

          
       // Start
        line.start();
        line.flush();
        int nBytesRead = 0, nBytesWritten = 0;
        while (nBytesRead != -1)
        {
            nBytesRead = din.read(data, 0, data.length);
            if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
        }
        // Stop
        line.drain();
        line.stop();
        line.close();
        din.close();
          }
        }

        private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
        {
          SourceDataLine res = null;
          DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
          res = (SourceDataLine) StdAudio.mainmix.getLine(info);
          res.open(audioFormat);
          return res;
        } 
  
}
