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
    ornamentControlWindow = controlP5.addControlWindow("controlP5window",0,0,512,512);
    //controlP5.setColorBackground(color(0, 0, 80));
    //controlP5.setColorForeground(color(255, 0, 0));
    //controlP5.setColorLabel(color(0, 0, 255));
    
    controlP5.controlWindow = ornamentControlWindow;
    //controlWindow.hideCoordinates();
    ornamentControlWindow.setBackground(128);
    
    int knobDiameter = 40;
    
    addP5Slider(patternInput, "setRadius", 0.01f, 0.3f, 0.05f);
    addP5Slider(patternInput, "setV", 0.0f, 1.0f, 0.5f);
    addP5Slider(pattern, "setTileHeight", 16, 512, 80);
    addP5Slider(patternInput, "setAngle", 0.0f, TWO_PI, 0.0f);
    
    occ = new OrnamentControlCanvas(patternInput, soundSensor);
    occ.pre();
    ornamentControlWindow.addCanvas(occ);
    
    
  }
  
  private int cP5X = 20, cP5Y = 20, cP5W = 200, cP5H = 20, cP5Lx=70, cP5Ly = 5;
  private void addP5Slider(Object plugTo, String theName, float theMin, float theMax, float theDefaultValue){
    //cP5Y += cP5H;
    Slider slider = controlP5.addSlider(theName, theMin, theMax, theDefaultValue, cP5X, cP5Y, cP5W, cP5H);
    slider.moveTo(ornamentControlWindow);
    slider.plugTo(plugTo);
    Textlabel t = controlP5.addTextlabel("l_"+theName, /*plugTo.getClass().getName()+"."+*/theName, cP5X+cP5Lx, cP5Y+cP5Ly);
    t.moveTo(ornamentControlWindow);
    cP5Y += cP5H*2;
  }
  private void addP5Slider(Object plugTo, String theName, int theMin, int theMax, int theDefaultValue){
    Slider slider = controlP5.addSlider(theName, theMin, theMax, theDefaultValue, cP5X, cP5Y, cP5W, cP5H);
    slider.moveTo(ornamentControlWindow);
    slider.plugTo(plugTo);
    Textlabel t = controlP5.addTextlabel("l_"+theName, /*plugTo.getClass().getName()+"."+*/theName, cP5X+cP5Lx, cP5Y+cP5Ly);
    t.moveTo(ornamentControlWindow);
    cP5Y += cP5H*2;
    //cP5Y += cP5H;
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
    patternInput.setU(avg0/10);
    
    patternInput.updateUV();
    
    //pattern.setTileHeight(50+floor(soundSensor.fft.getAvg(1)*100));
    
    pattern.render(patternInput);
    addition.render(pattern);
  }
  
  public void exit(){
    soundSensor.close();
    super.exit();
  }

}