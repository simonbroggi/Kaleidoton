import processing.core.*;
import processing.opengl.*;
import controlP5.*;

public class JPOrnament extends PApplet{
  
  private ControlP5 controlP5;
  private ControlWindow ornamentControlWindow;
  private OrnamentControlCanvas occ;
  
  public static void main(String args[]){
    //PApplet.main(new String[] {"--present", "JPOrnament"});
    PApplet.main(new String[] {"JPOrnament"});
  }
  static float TAN30 = PApplet.tan(PApplet.radians(30));
  Pattern pattern;
  PatternInput patternInput;
  SoundSensor soundSensor;
  Kreisspiegelung addition;
  
  
  public void setup(){
    size(1024,768, OPENGL);
    frameRate(60);
    
    
    
    pattern = new P6M(this);
    addition = new Kreisspiegelung(this, 200, 300, 180);
    patternInput = new PatternInput(this, "image.jpg");
    
    soundSensor = new SoundSensor(this);
    
    addOrnamentControll();
  }
  
  
  private void addOrnamentControll(){
    controlP5 = new ControlP5(this);
    ornamentControlWindow = controlP5.addControlWindow("controlP5window",100,100,512,512);
    controlP5.controlWindow = ornamentControlWindow;
    //controlWindow.hideCoordinates();
    ornamentControlWindow.setBackground(color(128));
    Controller mySlider = controlP5.addSlider("sliderValue",0,255,40,40,100,10);
    Textfield field = controlP5.addTextfield("myWindowTextfield",70,130,100,20);
    
    occ = new OrnamentControlCanvas(patternInput, soundSensor);
    occ.pre();
    ornamentControlWindow.addCanvas(occ);
    
    
  }
  
  public void keyPressed() {
    if (key == CODED) {
      if (keyCode == UP) {
        addition.addToR(3);
      } else if (keyCode == DOWN) {
        addition.addToR(-3);
      } 
    }
  }
  public void draw(){
    //println("draw ornament");
    addition.setXY(mouseX, mouseY);
    soundSensor.update();
    
    float avg0 = soundSensor.fft.getAvg(0);
    patternInput.u = avg0/10;
    
    patternInput.updateUV();
    
    //pattern.setTileHeight(50+floor(soundSensor.fft.getAvg(1)*100));
    
    pattern.render(patternInput);
    addition.render(pattern);
  }
  
  public void exit(){
    
    super.exit();
  }

}