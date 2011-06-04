import processing.core.*;

public class JPOrnament extends PApplet{
  
  public static void main(String args[]){
    PApplet.main(new String[] {"JPOrnament", "--present"});
  }
  static float TAN30 = PApplet.tan(PApplet.radians(30));
  Pattern pattern;
  PatternInput patternInput;
  SoundSensor soundSensor;
  Monitor monitor;
  Kreisspiegelung addition;
  
  public void setup(){
    size(800,600, P3D);
    frameRate(60);
    
    pattern = new P6M(this);
    addition = new Kreisspiegelung(this, 200, 300, 180);
    patternInput = new PatternInput(this, "image.jpg");
    
    soundSensor = new SoundSensor(this);
    
    monitor = new Monitor(this, patternInput, soundSensor);
  }
  
  public void draw(){
    //println("draw ornament");
    addition.setXY(mouseX, mouseY);
    soundSensor.update();
    pattern.render(this, patternInput);
    addition.render(this, pattern);
  }

}