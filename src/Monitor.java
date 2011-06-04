import processing.core.*;
class Monitor extends ExtraPWindow{
  PatternInput patternInput;
  SoundSensor soundSensor;
  Monitor(PApplet theApplet, PatternInput thePatternInput, SoundSensor theSoundSensor){
    super(theApplet, "Pattern Monitor", 600, 0, 800, 600, P3D, 30);
    println("konstructing Monitor");
    //frame.setResizable(true);
    patternInput = thePatternInput;
    soundSensor = theSoundSensor;
  }
  
  public void setup(){
    println("setting up monitor");
  }
  
  public void draw(){
    background(color(125, 125, 200));
    if(patternInput != null){ //why
      //println("drawing monitor");
      patternInput.render(this);
    }
    if(soundSensor != null){
      soundSensor.renderBuffer(this);
      soundSensor.renderFFT(this);
    }
  }
}