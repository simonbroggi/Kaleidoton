import processing.core.*;

import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

class SoundSensor implements PConstants{
  private PApplet pApplet;
  private Minim minim;
  private AudioInput audioIn;
  private FFT fft;
  private int bufferSize = 1024;
  private int sampleRate = 44100;
  private int bitDepth = 16;
  private int avgSize = 10;
  private float[] slowAverages = new float[avgSize];
  private float slowAveragesSpeed = 0.8f;
  
  //private BeatDetect beat;
  
  
  public SoundSensor(PApplet theApplet){
    pApplet = theApplet;
    minim = new Minim(pApplet);
    minim.debugOn();
    audioIn = minim.getLineIn(Minim.MONO, bufferSize, sampleRate, bitDepth);
    fft = new FFT(bufferSize, sampleRate);
    // calculate the averages by grouping frequency bands linearly.
    fft.linAverages(avgSize);
    
    //beat = new BeatDetect(bufferSize, sampleRate);

  }
  
  public void setSlowAveragesSpeed(float f){
    slowAveragesSpeed = f;
  }
  
  public void update(){
    fft.forward(audioIn.mix);
    updateSlowAverages();
    //BandPass bpf = new BandPass(55, 12, sampleRate);
    //audioIn.addEffect(bpf);
    /*
    beat.detect(audioIn.mix);
    if(beat.isOnset()){
      System.out.println("BBBBBBBBBBBBBBBBBBBb");
    }
    */
  }
  
  private void updateSlowAverages(){
    for(int i=0; i<avgSize; i++){
      float d = fft.getAvg(i) - slowAverages[i];
      slowAverages[i] += d*slowAveragesSpeed;
    }
  }
  
  public void renderFFT(PApplet pApplet){
    //pApplet.stroke(pApplet.color(0, 0, 255));
    pApplet.noStroke();
    pApplet.fill(pApplet.color(0, 0, 255));
    pApplet.rectMode(CORNERS);
    int w = pApplet.floor(pApplet.width/fft.specSize());
    for (int i = 0; i < fft.specSize(); i++){
      // draw the line for frequency band i, scaling it by 4 so we can see it a bit better
      pApplet.rect(i*w, pApplet.height, i*w + w, pApplet.height - fft.getBand(i) * 16);
    }
  }
  
  public float getAvg0(){
    return fft.getAvg(0);
  }
  public float getSlowAvgDelta0(){
    return fft.getAvg(0)-slowAverages[0];
  }
  public float getAvg2(){
    return fft.getAvg(2);
  }
  public float getSlowAvgDelta2(){
    return fft.getAvg(2)-slowAverages[2];
  }
  
  public void renderFFTAverages(PApplet pApplet){
    pApplet.rectMode(CORNERS);
    pApplet.noStroke();
    pApplet.fill(pApplet.color(0, 255, 0));
    int w = (int) (pApplet.width/avgSize);
    for(int i = 0; i < avgSize; i++) {
      /*
      if(i == ((Kaleidoton)papplet).hBand){
        if(i == ((Kaleidoton)papplet).vBand) fill(this.color(255, 255, 255));
        else fill(cBandh);
      }
      else if(i == ((Kaleidoton)papplet).vBand) fill(cBandv);
      else fill(c2);
      */
      // draw a rectangle for each average, multiply the value by 16 so we can see it better
      pApplet.noStroke();
      pApplet.fill(pApplet.color(0, 255, 0));
      pApplet.rect(i*w, pApplet.height, i*w + w, pApplet.height - fft.getAvg(i)*16);
      //pApplet.stroke(pApplet.color(255, 0, 0));
      //pApplet.line(i*w, pApplet.height - slowAverages[i]*16, i*w + w, pApplet.height - slowAverages[i]*16);
    }
    //pApplet.println("avg: "+fft.getAvg(0));
  }
  public void renderSlowAverages(PApplet pApplet){
    pApplet.fill(pApplet.color(0, 200, 200));
    int w = (int) (pApplet.width/avgSize);
    for(int i = 0; i < avgSize; i++) {
      pApplet.rect(i*w, pApplet.height - slowAverages[i]*16 + 1, i*w + w, pApplet.height - slowAverages[i]*16 - 1);
    }
  }
  void renderBuffer(PApplet pApplet){
    pApplet.stroke(pApplet.color(0, 0, 255));
    for(int i = 0; i < audioIn.bufferSize() - 1; i++) {
      int h = PApplet.floor(pApplet.height / 2);
      pApplet.line(i, h + audioIn.mix.get(i)*200, i+1, h + audioIn.mix.get(i+1)*200);
    }
  }
  public void close(){
    audioIn.close();
    minim.stop();
  }
}