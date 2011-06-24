import processing.core.*;
import processing.opengl.*;
import controlP5.*;

public class JPOrnament extends PApplet{
  
  private ControlP5 controlP5;
  private ControlWindow ornamentControlWindow;
  private OrnamentControlCanvas occ;
  
  public static void main(String args[]){
    PApplet.main(new String[] {"--present", "JPOrnament"});
    //PApplet.main(new String[] {"JPOrnament"});
  }
  static float TAN30 = PApplet.tan(PApplet.radians(30));
  Pattern pattern;
  PatternInput patternInput;
  SoundSensor soundSensor;
  Kreisspiegelung addition;
  
  
  public void setup(){
    //size(1920,1080, OPENGL);
    size(1910,1070, OPENGL);
    //size(1024,768, OPENGL);
    
    
    
    //frame.setResizable(true);
    //frame.setUndecorated(true);
    
    frameRate(60);
    
    
    
    
    pattern = new P6M(this);
    addition = new Kreisspiegelung(this, 200, 300, 180);
    patternInput = new PatternInput(this, "image.jpg");
    
    soundSensor = new SoundSensor(this);
    
    addOrnamentControll();
  }
  
  public void setFullscreen(){
    System.out.println("fullscreen is stil a problem "+screenWidth);
    
    
    //ornamentControlWindow.papplet().frame.setResizable(true);
    //ornamentControlWindow.papplet().resize(screenWidth, screenHeight);
    ornamentControlWindow.setUndecorated(!ornamentControlWindow.isUndecorated());
    
  }
  
  
  private void addOrnamentControll(){
    
    controlP5 = new ControlP5(this);
    
    ornamentControlWindow = controlP5.addControlWindow("controlP5window",0,0,800,600, 30);
    //ornamentControlWindow.
    //controlP5.setColorBackground(color(0, 0, 80));
    //controlP5.setColorForeground(color(255, 0, 0));
    //controlP5.setColorLabel(color(0, 0, 255));
    
    //controlP5.controlWindow = ornamentControlWindow;
    //controlWindow.hideCoordinates();
    ornamentControlWindow.setBackground(128);
    
    occ = new OrnamentControlCanvas(patternInput, soundSensor);
    occ.pre();
    //JPOrnamentControl jpOC = new JPOrnamentControl(ornamentControlWindow.papplet());
    //ornamentControlWindow.papplet().registerDraw(jpOC);
    ornamentControlWindow.papplet().registerDispose(this);
    ornamentControlWindow.addCanvas(occ);
    
    /*
    int knobDiameter = 40;
    
    addP5Slider(patternInput, "setRadius", 0.01f, 0.3f, 0.05f);
    addP5Slider(patternInput, "setV", 0.0f, 1.0f, 0.5f);
    addP5Slider(pattern, "setTileHeight", 16, 512, 80);
    addP5Slider(patternInput, "setAngle", 0.0f, TWO_PI, 0.0f);
    
    Knob knob = controlP5.addKnob("setSlowAveragesSpeed", 0.0f, 1.0f, 0.7f, 200, 400, 40);
    knob.moveTo(ornamentControlWindow);
    knob.setMoveable(false);
    knob.plugTo(soundSensor);
    */
    Button full = controlP5.addButton("setFullscreen", 0.0f, 300, 400, 60, 40);
    full.moveTo(ornamentControlWindow);
    
    
    SoundSensor.SoundAverage[] avgs = soundSensor.getTheAverages();
    for(int i=0; i<avgs.length; i++){
    //for(int i=0; i<1; i++){
      ControlGroup c = addSoundAverageControl(avgs[i]);
      c.setPosition(10, 30+200*i);
      c.moveTo(ornamentControlWindow);
      
    }
    
  }
  private int theAvgNumber = 1;
  private ControlGroup addSoundAverageControl(SoundSensor.SoundAverage average){
    ControlGroup group = controlP5.addGroup("soundAverage "+theAvgNumber, 10, 10, 420);
    group.setBackgroundHeight(150);
    group.setBackgroundColor(color(0,100));
    
    Slider slider = controlP5.addSlider("setLow", 1, 2000, average.getLow(), 10, 20, 300, 20);
    slider.moveTo(group);
    slider.plugTo(average);
    
    slider = controlP5.addSlider("setHigh", 1, 2000, average.getHigh(), 10, 50, 300, 20);
    slider.moveTo(group);
    slider.plugTo(average);
    
    slider = controlP5.addSlider("setSlowAverageSpeed", 0.0f, 1.0f, average.getSlowAverageSpeed(), 10, 80, 300, 20);
    slider.moveTo(group);
    slider.plugTo(average);
    
    slider = controlP5.addSlider("setThreshhold", 0.0f, 10.0f, average.getThreshhold(), 10, 110, 300, 20);
    slider.moveTo(group);
    slider.plugTo(average);
    
    /*
    Knob knob = controlP5.addKnob("setSlowAverageSpeed", 0.0f, 1.0f, 0.8f, 20, 90, 30);
    knob.moveTo(group);
    knob.plugTo(average);
    */
    theAvgNumber++;
    return group;
  }
  
  private int cP5X = 20, cP5Y = 20, cP5W = 200, cP5H = 20, cP5Lx=70, cP5Ly = 5;
  private void addP5Slider(Object plugTo, String theName, float theMin, float theMax, float theDefaultValue){
    //cP5Y += cP5H;
    Slider slider = controlP5.addSlider(theName, theMin, theMax, theDefaultValue, cP5X, cP5Y, cP5W, cP5H);
    slider.moveTo(ornamentControlWindow);
    slider.setMoveable(false);
    slider.plugTo(plugTo);
    /*Textlabel t = controlP5.addTextlabel("l_"+theName, plugTo.getClass().getName()+"."+theName, cP5X+cP5Lx, cP5Y+cP5Ly);
    t.moveTo(ornamentControlWindow);
    t.setMoveable(false);*/
    cP5Y += cP5H*2;
  }
  private void addP5Slider(Object plugTo, String theName, int theMin, int theMax, int theDefaultValue){
    Slider slider = controlP5.addSlider(theName, theMin, theMax, theDefaultValue, cP5X, cP5Y, cP5W, cP5H);
    slider.moveTo(ornamentControlWindow);
    slider.plugTo(plugTo);
    slider.setMoveable(false);
    /*Textlabel t = controlP5.addTextlabel("l_"+theName, plugTo.getClass().getName()+"."+theName, cP5X+cP5Lx, cP5Y+cP5Ly);
    t.moveTo(ornamentControlWindow);
    t.setMoveable(false);*/
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
  private boolean moveURight = true;
  
  public void draw(){
    //println("draw ornament");
    addition.setXY(mouseX, mouseY);
    soundSensor.update();
    
    
    //float deltaU = soundSensor.getAvg0()/20;
    //float deltaU = moveURight?soundSensor.getSlowAvgDelta0():-soundSensor.getSlowAvgDelta0();
    
    SoundSensor.SoundAverage[] averages = soundSensor.getTheAverages();
    float slowAvgDelta = averages[0].getAverage()-averages[0].getSlowAverage();
    float deltaU = moveURight?slowAvgDelta:-soundSensor.getSlowAvgDelta0();
    float newU = patternInput.getU()+deltaU*deltaU;
    newU = norm(newU, floor(newU), ceil(newU));
    /*
    if(newU >= 1){
      newU = newU - floor(newU);
    }
    else if(newU < 0){
      newU = newU - floor(newU)+1;
    }
    */
    /*
    if(newU > 1){
      newU = 1;
      moveURight = false;
    }
    else if(newU < 0){
      newU = 0;
      moveURight = true;
    }
    */
    /*
    if(moveURight){
      newU += deltaU;
      if(newU > 1){
        newU = 1;
        moveURight = false;
      }
    }
    else{
      newU -= deltaU;
      if(newU < 0){
        newU = 0;
        moveURight = true;
      }
    }
    */
    
    /*float newU = patternInput.getU()+avg0/10;
    if (newU > 1){
      newU -= newU%1.0;
    }
    */
    patternInput.setU(newU);
    float slowAvgDelta2 = averages[1].getAverage()-averages[1].getSlowAverage();
    patternInput.setAngle(patternInput.getAngle()+slowAvgDelta2);
    //patternInput.setAngle(patternInput.getAngle()+soundSensor.getSlowAvgDelta2());
    //patternInput.setU(avg0/10);
    
    patternInput.updateUV();
    
    //pattern.setTileHeight(50+floor(soundSensor.fft.getAvg(1)*100));
    
    pattern.render(patternInput);
    addition.render(pattern);
  }
  
  public void dispose(){
    System.out.println("closed via control window");
    exit();
  }
  
  public void exit(){
    //controlP5.save();
    soundSensor.close();
    super.exit();
  }

}