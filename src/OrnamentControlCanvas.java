import processing.core.PApplet;
import controlP5.ControlWindowCanvas;

public class OrnamentControlCanvas extends ControlWindowCanvas {

  private PatternInput patternInput;
  private SoundSensor soundSensor;
  
  public OrnamentControlCanvas(PatternInput thePatternInput, SoundSensor theSoundSensor){
    patternInput = thePatternInput;
    soundSensor = theSoundSensor;
  }
  
  @Override
  public void draw(PApplet theApplet) {
    
    patternInput.render(theApplet);
    soundSensor.renderFFT(theApplet);
    soundSensor.renderFFTAverages(theApplet);
    soundSensor.renderBuffer(theApplet);
    
  }

}
