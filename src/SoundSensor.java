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
  
  private SoundAverage[] theAverages;
  public SoundAverage[] getTheAverages(){
    return theAverages;
  }
  //private BeatDetect beat;
  
  
  public class SoundAverage{
    private float low, high, slowAverageSpeed, threshhold;
    private float average, slowAverage;
    private float ausschlag;//
    private float factor;
    private String name;
    public SoundAverage(String name, float low, float high){
      this(name, low, high, 0.8f, 0.0f, 1.0f);
    }
    public SoundAverage(String name, float low, float high, float slowAveragesSpeed, float threshhold, float factor){
      this.name = name;
      this.low = low;
      this.high = high;
      this.slowAverageSpeed = slowAveragesSpeed;
      this.threshhold = threshhold;
      this.factor = factor;
    }
    public String getName(){
      return name;
    }
    public float getFactor(){
      return factor;
    }
    public void setFactor(float factor){
      this.factor = factor;
    }
    public float getAverage(){
      return average*factor;
    }
    public float getSlowAverage(){
      return slowAverage*factor;
    }
    public void update(FFT fft){
      average = fft.calcAvg(low, high);
      float d = average - slowAverage;
      slowAverage += d*slowAverageSpeed;
    }
    public void setLow(float low){
      if(low < high){
        this.low = low;
        //System.out.println("low set to "+this.low);
      }
    }
    public void setHigh(float high){
      if(high > low){
        this.high = high;
        //System.out.println("high set to "+this.high);
      }
    }
    public float getLow(){
      return low;
    }
    public float getHigh(){
      return high;
    }
    //between 0 and 1
    public void setSlowAverageSpeed(float slowAverageSpeed){
      this.slowAverageSpeed = slowAverageSpeed;
    }
    public float getSlowAverageSpeed(){
      return slowAverageSpeed;
    }
    public void setThreshhold(float threshhold){
      this.threshhold = threshhold;
    }
    public float getThreshhold(){
      return threshhold;
    }
    public void draw(PApplet pApplet, int leftX, int rightX, int baseY, int factor){
      factor*=this.factor;
      pApplet.noStroke();
      pApplet.fill(pApplet.color(180, 0, 0, 128));
      pApplet.rect(leftX, baseY, rightX, baseY - average*factor);
      pApplet.fill(pApplet.color(255, 0, 0, 128));
      pApplet.rect(leftX, baseY - slowAverage*factor - 1 -(threshhold*factor/2), rightX, baseY - slowAverage*factor + 1 +(threshhold*factor/2));
    }
  }
  
  
  public SoundSensor(PApplet theApplet){
    pApplet = theApplet;
    minim = new Minim(pApplet);
    minim.debugOn();
    audioIn = minim.getLineIn(Minim.MONO, bufferSize, sampleRate, bitDepth);
    fft = new FFT(bufferSize, sampleRate);
    // calculate the averages by grouping frequency bands linearly.
    fft.linAverages(avgSize);
    
    theAverages = new SoundAverage[4];
    theAverages[0] = new SoundAverage("horizontal movement - low base", 32, 240);
    theAverages[0].setSlowAverageSpeed(0.5f);
    theAverages[1] = new SoundAverage("size - high base", 240, 500);
    theAverages[1].setFactor(8.0f);
    theAverages[2] = new SoundAverage("rotation - speach", 500, 2048);
    theAverages[2].setFactor(20.0f);
    theAverages[3] = new SoundAverage("vertical movement - letter s", 6000, 15000);
    theAverages[3].setFactor(25.0f);
    
    //beat = new BeatDetect(bufferSize, sampleRate);

  }
  
  public void setSlowAveragesSpeed(float f){
    slowAveragesSpeed = f;
  }
  
  public void update(){
    fft.forward(audioIn.mix);
    updateSlowAverages();
    for(int i=0; i<theAverages.length; i++){
      theAverages[i].update(fft);
    }
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
  public void renderTheAverages(PApplet pApplet){
    int w = pApplet.floor(pApplet.width/theAverages.length);
    for(int i=0; i<theAverages.length; i++){
      theAverages[i].draw(pApplet, i*w, i*w+w, pApplet.height, 6);
    }
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