package metamindmachine;


import java.io.*;
import javax.sound.sampled.*;
import java.net.URL;
//import java.net.*;
//import java.applet.*;

/**
 *  <i>Standard audio</i>. This class provides a basic capability for
 *  creating, reading, and saving audio. 
 *  <p>
 *  The audio format uses a sampling rate of 44,100 (CD quality audio), 16-bit, monaural.
 *
 *  <p>
 *  For additional documentation, see <a href="http://www.cs.princeton.edu/introcs/15inout">Section 1.5</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 */
public final class StdAudio {

    /**
     *  The sample rate - 44,100 Hz for CD quality audio.
     */
    public static int channels = 2;
    
    public static final int SAMPLE_RATE = 22050;

    private static final int BYTES_PER_SAMPLE = 4;                // 16-bit audio
    private static final int BITS_PER_SAMPLE = 16;                // 16-bit audio
    private static final double MAX_16_BIT = Short.MAX_VALUE;     // 32,767
    private static final int SAMPLE_BUFFER_SIZE = 4096;


    static SourceDataLine[] line = new SourceDataLine[channels];    // to play the sound
    private static byte[][] buffer = new byte[2][];         // our internal buffer
    private static int i = 0;             // number of samples currently in internal buffer
    private static int i2 = 0;
    
    public static Mixer mainmix;
   
    // static initializer
    static { init(); }

    // open up an audio stram
    static void init() {
        try {
            Mixer.Info[] mixerinfo = AudioSystem.getMixerInfo();
            //for (int i=0;i < mixerinfo.length;i++) System.out.println("mixers:"+mixerinfo[i].getName() + mixerinfo[i].getVendor() + mixerinfo[i].getDescription());

            mainmix = AudioSystem.getMixer(mixerinfo[0]);
            
            Line.Info lineinf[] = mainmix.getSourceLineInfo();
            //for(int i=0;i < lineinf.length;i++) System.out.println("lines:"+lineinf[i].toString());
            
            // 44,100 samples per second, 16-bit audio, mono, signed PCM, little Endian
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 2, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);


            line[0] = (SourceDataLine) mainmix.getLine(info);
            //line[1] = (SourceDataLine) mainmix.getLine(info);
            
            Control[] linectrl = line[0].getControls();
            //for(int j=0;j<linectrl.length;j++) System.out.println(linectrl[j].toString());
            
                    
            //line = (SourceDataLine) AudioSystem.getLine(info);
            line[0].open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);
            //line[1].open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);
            
/*
            linectrl = line[0].getControls();
            for(int j=0;j<linectrl.length;j++)
                System.out.println(linectrl[j].toString());
 */
/*            
            //set line balance to left and right
            FloatControl balctrl = (FloatControl) line[0].getControl(FloatControl.Type.BALANCE);
            balctrl.setValue(-1);
            FloatControl balctrl2 = (FloatControl) line[1].getControl(FloatControl.Type.BALANCE);
            balctrl2.setValue(1);
*/
            
            // the internal buffer is a fraction of the actual buffer size, this choice is arbitrary
            // it gets diveded because we can't expect the buffered data to line up exactly with when
            // the sound card decides to push out its samples.
            buffer[0] = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE];
            //buffer[1] = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE];
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        
        // no sound gets made before this call
        line[0].flush();
        line[0].start();
        //line[1].start();
    }
/*
    public static void toneplayled(int channel, double volume, double[] in,int len,double binfreq) {
        boolean ledon = false;
        double i=0;
        //LEDCtrl.LEDon();
        for (int l = 0; l < len; l++) {
            if (in[l] < -1.0) in[l] = -1.0;
            if (in[l] > +1.0) in[l] = +1.0;

            // convert to bytes
            short s = (short) (MAX_16_BIT * (in[l]/100*volume));
            buffer[channel][0] = (byte) s;
            buffer[channel][1] = (byte) (s >> 8);   // little Endian
            s = (short) (MAX_16_BIT * (in[l+1]/100*volume));
            buffer[channel][2] = (byte) s;
            buffer[channel][3] = (byte) (s >> 8);   // little Endian

            
              if((i % (44100/binfreq)) < 1) {
                  if(ledon) {
                      LEDCtrl.LEDoff();
                      ledon = false;
                  } else {
                      LEDCtrl.LEDon();
                      ledon = true;
                  }
              }

            //if(l % (len/binfreq) >= (len/binfreq)-1) LEDCtrl.LEDoff();

                
            
            // send to sound card
            line[channel].write(buffer[channel], 0, 4);
            l++;
            i++;

        }
    }    
*/    
    public static void toneplay(int channel, double volume, double[] in,int len) {
        for (int l = 0; l < len; l++) {
            if (in[l] < -1.0) in[l] = -1.0;
            if (in[l] > +1.0) in[l] = +1.0;

            // convert to bytes
            short s = (short) (MAX_16_BIT * (in[l]/100*volume));
            buffer[channel][0] = (byte) s;
            buffer[channel][1] = (byte) (s >> 8);   // little Endian
            s = (short) (MAX_16_BIT * (in[l+1]/100*volume));
            buffer[channel][2] = (byte) s;
            buffer[channel][3] = (byte) (s >> 8);   // little Endian

            
            // send to sound card
            line[channel].write(buffer[channel], 0, 4);
            l++;

        }
    }
    
    public static AudioFormat getAudioFormat() {
        AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 2, true, false);        
        return format;
    }
    
    public static final void toneplay(int channel,double in) {

        // clip if outside [-1, +1]
        if (in < -1.0) in = -1.0;
        if (in > +1.0) in = +1.0;


        // convert to bytes
        short s = (short) (MAX_16_BIT * in);
        buffer[channel][i++] = (byte) s;
        buffer[channel][i++] = (byte) (s >> 8);   // little Endian
        buffer[channel][i++] = (byte) s;
        buffer[channel][i++] = (byte) (s >> 8);   // little Endian

        
        
        // send to sound card
        line[channel].write(buffer[channel], 0, 4);
        i=0;

    }    

    
    // not-instantiable
    private StdAudio() { }


    /**
     * Close standard audio.
     */
    public static final void close() {
        line[0].drain();
        line[0].stop();
        
//        line[1].drain();
//        line[1].stop();
        
    }
   
    public static final void play(double in) {

        // clip if outside [-1, +1]
        if (in < -1.0) in = -1.0;
        if (in > +1.0) in = +1.0;

        // convert to bytes
        short s = (short) (MAX_16_BIT * in);
        buffer[0][i++] = (byte) s;
        buffer[0][i++] = (byte) (s >> 8);   // little Endian
        buffer[0][i++] = (byte) s;
        buffer[0][i++] = (byte) (s >> 8);   // little Endian

        
        // send to sound card if buffer is full        
        if (i >= buffer.length) {
            line[0].write(buffer[0], 0, 4);
            //line[1].write(buffer, 0, buffer.length);
            i = 0;
        }
    }
/*
    public static final void simuplay(double in1, double in2,int len) {

        // clip if outside [-1, +1]
        if (in1 < -1.0) in1 = -1.0;
        if (in1 > +1.0) in1 = +1.0;
        if (in2 < -1.0) in1 = -1.0;
        if (in2 > +1.0) in1 = +1.0;


        // convert to bytes
        short s = (short) (MAX_16_BIT * in1);
        buffer[i++] = (byte) s;
        buffer[i++] = (byte) (s >> 8);   // little Endian
        buffer[i++] = (byte) s;
        buffer[i++] = (byte) (s >> 8);   // little Endian

        
        s = (short) (MAX_16_BIT * in2);
        buffer[1][i2++] = (byte) s;
        buffer[1][i2++] = (byte) (s >> 8);   // little Endian        
        buffer[1][i2++] = (byte) s;
        buffer[1][i2++] = (byte) (s >> 8);   // little Endian        
        
        // send to sound card if buffer is full        
        if (i >= len) {
            line[0].write(buffer, 0, len);
            //line[1].write(buffer[1], 0, len);
            i = 0;
            i2= 0;
        }
    }
*/


    
    public static void play(double[] input) {
        for (int i = 0; i < input.length; i++) {
            play(input[i]);
        }
    }

    public static double[] read(String filename) {
        byte[] data = {0}; // = readByte(filename);
        int N = data.length;
        double[] d = new double[N/2];
        for (int i = 0; i < N/2; i++) {
            d[i] = ((short) (((data[2*i+1] & 0xFF) << 8) + (data[2*i] & 0xFF))) / ((double) MAX_16_BIT);
        }
        return d;
    }





     // Play a sound file (in .wav or .au format) in a background thread.
/*
    public static void play(String filename) {
        URL url = null;
        try {
            File fil = new File(filename);
            if (fil.canRead()) url = fil.toURI().toURL();
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        // URL url = StdAudio.class.getResource(filename);
        if (url == null) throw new RuntimeException("audio " + filename + " not found");
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }
*/

    // return data as a byte array
    private static byte[] readByte(String filename) {
        byte[] data = null;
        AudioInputStream ais = null;
        try {
            URL url = StdAudio.class.getResource(filename);
            ais = AudioSystem.getAudioInputStream(url);
            data = new byte[ais.available()];
            ais.read(data);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not read " + filename);
        }

        return data;
    }


    /**
     * Save the double array as a sound file (using .wav or .au format).
     */
    public static void save(String filename, double[] input) {

        // assumes 44,100 samples per second
        // use 16-bit audio, mono, signed PCM, little Endian
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        byte[] data = new byte[2 * input.length];
        for (int i = 0; i < input.length; i++) {
            int temp = (short) (input[i] * MAX_16_BIT);
            data[2*i + 0] = (byte) temp;
            data[2*i + 1] = (byte) (temp >> 8);
        }

        // now save the file
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais, format, input.length);
            if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
            }
            else if (filename.endsWith(".au") || filename.endsWith(".AU")) {
                AudioSystem.write(ais, AudioFileFormat.Type.AU, new File(filename));
            }
            else {
                throw new RuntimeException("File format not supported: " + filename);
            }
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }




   /***********************************************************************
    * sample test client
    ***********************************************************************/

    // create a note (sine wave) of the given frequency (Hz), for the given
    // duration (seconds) scaled to the given volume (amplitude)
    private static double[] note(double hz, double duration, double amplitude) {
        int N = (int) Math.round(SAMPLE_RATE * duration);
        double[] tone = new double[N+1];
        for (int i = 0; i <= N; i++)
            tone[i] = amplitude * Math.sin(2 * Math.PI * i * hz / SAMPLE_RATE);
        return tone;
    }

    /**
     * Test client - play an A major scale to standard audio.
     */
    public static void main(String[] args) {
        
        double hz = 440.0;
        StdAudio.play(note(hz, 1.0, 0.5));
        
        
        // 440 Hz for 1 sec
        double freq = 440.0;
        for (int i = 0; i < 44100; i++) {
            StdAudio.play(0.5 * Math.sin(2*Math.PI * freq * i / SAMPLE_RATE));
        }
        
        // scale increments
        int[] steps = { 0, 2, 4, 5, 7, 9, 11, 12 };
        for (int i = 0; i < steps.length; i++) {
            double hz2 = 440.0 * Math.pow(2, steps[i] / 12.0);
            StdAudio.play(note(hz2, 1.0, 0.5));
        }


        // need to call this in non-interactive stuff so the program doesn't terminate
        // until all the sound leaves the speaker.
        StdAudio.close(); 

        // need to terminate a Java program with sound
        System.exit(0);
    }
}


