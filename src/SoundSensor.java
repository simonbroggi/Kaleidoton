import processing.core.*;

import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

class SoundSensor implements PConstants{
  Minim minim;
  AudioInput audioIn;
  FFT fft;
  int bufferSize = 1024;
  int sampleRate = 44100;
  int bitDepth = 16;
  
  SoundSensor(PApplet pApplet){
    minim = new Minim(pApplet);
    minim.debugOn();
    audioIn = minim.getLineIn(Minim.MONO, bufferSize, sampleRate, bitDepth);
    fft = new FFT(bufferSize, sampleRate);
    // calculate the averages by grouping frequency bands linearly.
    fft.linAverages(10);
  }
  
  public void update(){
    fft.forward(audioIn.mix);
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
  
  public void renderFFTAverages(PApplet pApplet){
    pApplet.rectMode(CORNERS);
    pApplet.noStroke();
    pApplet.fill(pApplet.color(0, 255, 0));
    int w = (int) (pApplet.width/fft.avgSize());
    for(int i = 0; i < fft.avgSize(); i++) {
      /*
      if(i == ((Kaleidoton)papplet).hBand){
        if(i == ((Kaleidoton)papplet).vBand) fill(this.color(255, 255, 255));
        else fill(cBandh);
      }
      else if(i == ((Kaleidoton)papplet).vBand) fill(cBandv);
      else fill(c2);
      */
      // draw a rectangle for each average, multiply the value by 16 so we can see it better
      pApplet.rect(i*w, pApplet.height, i*w + w, pApplet.height - fft.getAvg(i)*16);
    }
    //pApplet.println("avg: "+fft.getAvg(0));
  }
  void renderBuffer(PApplet pApplet){
    pApplet.stroke(pApplet.color(0, 0, 255));
    for(int i = 0; i < audioIn.bufferSize() - 1; i++) {
      int h = PApplet.floor(pApplet.height / 2);
      pApplet.line(i, h + audioIn.mix.get(i)*200, i+1, h + audioIn.mix.get(i+1)*200);
    }
  }
}