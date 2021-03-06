import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import processing.core.*;
import processing.opengl.*;
import controlP5.*;

public class JPOrnament extends PApplet{
  
  private ControlP5 controlP5;
  private Slider tileHeightSlider;
  private Slider baseRadiusSlider;
  private ControlWindow ornamentControlWindow;
  private OrnamentControlCanvas occ;
  
  public static void main(String args[]){
    PApplet.main(new String[] {"--present", "JPOrnament"});
    //PApplet.main(new String[] {"JPOrnament"});
  }
  static float TAN30 = PApplet.tan(PApplet.radians(30));
  Pattern pattern, p6m, p3m1;
  PatternInput patternInput;
  SoundSensor soundSensor;
  //Kreisspiegelung addition;
  
  
  public void setup(){
    //size(1920,1080, OPENGL);
    size(1918,1078, OPENGL);
    //size(1440,990, OPENGL);
    //size(1024,768, OPENGL);
    
    
    
    //frame.setResizable(true);
    //frame.setUndecorated(true);
    
    frameRate(60);
    
    
    
    
    pattern = p6m = new P6M(this);
    p3m1 = new P3M1(this);
    //addition = new Kreisspiegelung(this, 200, 300, 180);
    patternInput = new PatternInput(this, "image.jpg");
    
    soundSensor = new SoundSensor(this);
    
    addOrnamentControll();
    
    load();
  }
  
  public void setFullscreen(){
    System.out.println("fullscreen is stil a problem "+screenWidth);
    
    
    //ornamentControlWindow.papplet().frame.setResizable(true);
    //ornamentControlWindow.papplet().resize(screenWidth, screenHeight);
    ornamentControlWindow.setUndecorated(!ornamentControlWindow.isUndecorated());
    
  }
  
  public void set_P6M(boolean flag){
    System.out.println("p6m set to "+flag);
  }
  private void addOrnamentControll(){
    
    controlP5 = new ControlP5(this);
    
    ornamentControlWindow = controlP5.addControlWindow("controlP5window",0,0, 1024, 768, 20);
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
    
    ControlGroup general = controlP5.addGroup("general", 600, 40, 360);
    general.setBackgroundHeight(400);
    general.setBackgroundColor(color(0,100));
    //general.setPosition(theX, theY)
    general.moveTo(ornamentControlWindow);
    
    tileHeightSlider = controlP5.addSlider("setTileHeight", 16, 512, pattern.getTileHeight(), 10, 20, 280, 15);
    tileHeightSlider.moveTo(general);
    tileHeightSlider.plugTo(pattern);

    baseRadiusSlider = controlP5.addSlider("setBaseRadius", 0.01f, 1.0f, patternInput.getBaseRadius(), 10, 50, 280, 15);
    baseRadiusSlider.moveTo(general);
    baseRadiusSlider.plugTo(patternInput);
    
    
    
    
    //RadioButton changePatternRadio = controlP5.addRadioButton("changePattern", 160, 150);
    RadioButton changePatternRadio = controlP5.addRadioButton("changePattern", 160, 150);
    changePatternRadio.setColorForeground(color(120));
    changePatternRadio.setColorActive(color(255));
    changePatternRadio.setColorLabel(color(255));
    changePatternRadio.setItemsPerRow(5);
    changePatternRadio.setSpacingColumn(50);
    addToRadioButton(changePatternRadio, "P6M", 1);
    addToRadioButton(changePatternRadio, "P3M1", 2);
    changePatternRadio.moveTo(general);
    
    /*
    Toggle toggle = controlP5.addToggle("set_P6M",true,100,160,20,20);
    //changePatternRadio.addItem("setP6M", 1.0f);
    changePatternRadio.addItem(toggle, 1.0f);
    
    //changePatternRadio.addItem("setP3M1", 1.0f);
    toggle = controlP5.addToggle("set_P3M1wat", false, 100, 190, 20, 20);
    changePatternRadio.addItem(toggle, 0.0f);
    changePatternRadio.moveTo(general);
    */
    
    
    Button b = controlP5.addButton("setFullscreen", 0.0f, 260, 340, 70, 40);
    b.moveTo(general);
    
    b = controlP5.addButton("chooseNewImage", 0.0f, 160, 340, 80, 40);
    b.moveTo(general);
    
    b = controlP5.addButton("save", 0.0f, 60, 340, 40, 40);
    b.moveTo(general);
    //b.plugTo(soundSensor);
    
    b = controlP5.addButton("load", 0.0f, 10, 340, 40, 40);
    b.moveTo(general);
    //b.plugTo(soundSensor);
    
    SoundSensor.SoundAverage[] avgs = soundSensor.getTheAverages();
    for(int i=0; i<avgs.length; i++){
    //for(int i=0; i<1; i++){
      ControlGroup c = addSoundAverageControl(avgs[i]);
      c.setPosition(10, 30+170*i);
      c.moveTo(ornamentControlWindow);
      
    }
    
  }
  private void addToRadioButton(RadioButton theRadioButton, String theName, int theValue ) {
    Toggle t = theRadioButton.addItem(theName,theValue);
    t.captionLabel().setColorBackground(color(80));
    t.captionLabel().style().movePadding(2,0,-1,2);
    t.captionLabel().style().moveMargin(-2,0,0,-3);
    t.captionLabel().style().backgroundWidth = 46;
  }
  public void changePattern(int newPatternNo){
    System.out.println("pattern changed to "+newPatternNo);
  }
  private int patternState = 1;
  private boolean patternStateChanged = false;
  public void controlEvent(ControlEvent theEvent) {
    if(theEvent.isGroup() && theEvent.group().name().equals("changePattern")){
      System.out.print("got an event from "+theEvent.group().name()+"\t");
      for(int i=0;i<theEvent.group().arrayValue().length;i++) {
        System.out.print(theEvent.group().arrayValue()[i]);
      }
      System.out.println("\t "+theEvent.group().value());
      patternState = (int)theEvent.group().value();
      patternStateChanged = true;
    }
  }
  private int theAvgNumber = 1;
  private ControlGroup addSoundAverageControl(SoundSensor.SoundAverage average){
    ControlGroup group = controlP5.addGroup(average.getName(), 10, 10, 530);
    group.setBackgroundHeight(140);
    group.setBackgroundColor(color(0,100));
    int w = 400;
    Slider slider = controlP5.addSlider("setLow", 30, 16384, average.getLow(), 10, 20, w, 15);
    slider.moveTo(group);
    slider.plugTo(average);
    average.lowSlider = slider;
    
    slider = controlP5.addSlider("setHigh", 30, 16384, average.getHigh(), 10, 40, w, 15);
    slider.moveTo(group);
    slider.plugTo(average);
    average.highSlider = slider;
    
    slider = controlP5.addSlider("setSlowAverageSpeed", 0.0f, 1.0f, average.getSlowAverageSpeed(), 10, 60, w, 15);
    slider.moveTo(group);
    slider.plugTo(average);
    average.slowAvgSSlider = slider;

    slider = controlP5.addSlider("setFactor", 0.0f, 30.0f, average.getFactor(), 10, 80, w, 15);
    slider.moveTo(group);
    slider.plugTo(average);
    average.factorSlider = slider;
    
    slider = controlP5.addSlider("setPower", 0.0f, 3.0f, average.getPower(), 10, 100, w, 15);
    slider.moveTo(group);
    slider.plugTo(average);
    average.powerSlider = slider;
    /*
    slider = controlP5.addSlider("setThreshhold", 0.0f, 10.0f, average.getThreshhold(), 10, 100, w, 15);
    slider.moveTo(group);
    slider.plugTo(average);
    */    
        
    
    /*
    Knob knob = controlP5.addKnob("setSlowAverageSpeed", 0.0f, 1.0f, 0.8f, 20, 90, 30);
    knob.moveTo(group);
    knob.plugTo(average);
    */
    theAvgNumber++;
    return group;
  }
  public void chooseNewImage(float v){
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {

          JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG & GIF Images", "png", "jpg", "gif");
        fc.setFileFilter(filter);
          int returnVal = fc.showOpenDialog(null);

          if (returnVal == JFileChooser.APPROVE_OPTION) {
            PImage tex = loadImage(fc.getSelectedFile().getAbsolutePath());
            patternInput.setImage(tex);
          } 
          else {
            println("Open command cancelled by user.");
          }
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }); 
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
  /*
  public void keyPressed() {
    if (key == CODED) {
      if (keyCode == UP) {
        addition.addToR(3);
      } else if (keyCode == DOWN) {
        addition.addToR(-3);
      } 
    }
  }
  */
  //private boolean moveURight = true;
  
  public void draw(){
    
    
    
    //println("draw ornament");
    //addition.setXY(mouseX, mouseY);
    soundSensor.update();
    
    if(patternStateChanged){
      switch(patternState){
      case 1:
        pattern = p6m;
        System.out.println("patternSet to p6m");
        break;
      case 2:
        pattern = p3m1;
        System.out.println("patternSet to p3m1");
        break;
      }
      patternStateChanged = false;
    }
    
    
    //float deltaU = soundSensor.getAvg0()/20;
    //float deltaU = moveURight?soundSensor.getSlowAvgDelta0():-soundSensor.getSlowAvgDelta0();
    
    SoundSensor.SoundAverage[] averages = soundSensor.getTheAverages();
    float deltaU = averages[0].getAverage()-averages[0].getSlowAverage();
    deltaU = pow(deltaU, 3);
    deltaU*=0.0002;
    float newU = patternInput.getU()+abs(deltaU);
    newU = norm(newU, floor(newU), ceil(newU));
    patternInput.setU(newU);
    
    
    float deltaV = averages[3].getAverage()-averages[3].getSlowAverage();
    deltaV = pow(deltaV, 3);
    deltaV*=0.0005;
    float newV = patternInput.getV()+abs(deltaV);
    newV = norm(newV, floor(newV), ceil(newV));
    patternInput.setV(newV);
    
    /*
    slowAvgDelta = averages[3].getAverage()-averages[3].getSlowAverage();
    float deltaV = moveUUp?slowAvgDelta:
    */
    //float slowAvgDelta2 = averages[1].getAverage()-averages[1].getSlowAverage();
    //float slowAvgDelta2 = (averages[2].getAverage()+averages[1].getAverage())-(averages[2].getSlowAverage()+averages[1].getSlowAverage());
    float slowAvgDelta2 = averages[2].getAverage()-averages[2].getSlowAverage();
    slowAvgDelta2*=0.3;
    float newAngle = patternInput.getAngle()+slowAvgDelta2;
    //patternInput.setAngle(pow(newAngle*0.1f, 2));
    //patternInput.setAngle(pow(newAngle, 2)*0.1f);
    patternInput.setAngle(newAngle);
    //patternInput.setAngle(patternInput.getAngle()+soundSensor.getSlowAvgDelta2());
    
    float slowAvgDelta1 = averages[1].getAverage()-averages[1].getSlowAverage();
    slowAvgDelta1*=0.01;
    //float newSize = patternInput.getRadius()+slowAvgDelta1;
    //patternInput.setRadius(abs(newSize));
    patternInput.setRadius(patternInput.getBaseRadius()+slowAvgDelta1);
    //TODO: average[2] should affect patternInput.setRadius in a way so that it can still be set with the slider...
    
    patternInput.updateUV();
    
    //pattern.setTileHeight(50+floor(soundSensor.fft.getAvg(1)*100));

    pattern.render(patternInput);
    //addition.render(pattern);
  }
  public void save(){
    try{
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      
      Element element = doc.createElement("kaleidoton");
      doc.appendChild(element);
      
      soundSensor.save(doc, element);
      
      Element patternElement = doc.createElement("Pattern");
      element.appendChild(patternElement);
      patternElement.setAttribute("tileHeight", ""+pattern.getTileHeight());
      
      Element patternInputElement = doc.createElement("PatternInput");
      element.appendChild(patternInputElement);
      patternInputElement.setAttribute("baseRadius", ""+patternInput.getBaseRadius());
      
      //write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result =  new StreamResult(new File("save.xml"));
      //transformer.setOutputProperty(arg0, arg1);
      transformer.transform(source, result);
    }
  
    catch(ParserConfigurationException pce){
      pce.printStackTrace();
    }
    catch(TransformerException tfe){
      tfe.printStackTrace();
    }
    catch(Exception e){
      e.printStackTrace();
    }

  }
  public void load(){
    try {
      File file = new File("save.xml");
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(file);
      doc.getDocumentElement().normalize();
      
      Element e = (Element)doc.getElementsByTagName("Pattern").item(0);
      int th = new Integer(e.getAttribute("tileHeight")).intValue();
      pattern.setTileHeight(th);
      System.out.println(th);
      tileHeightSlider.setValue(th);
      
      e = (Element)doc.getElementsByTagName("PatternInput").item(0);
      float br = new Float(e.getAttribute("baseRadius")).floatValue();
      System.out.println(br);
      patternInput.setBaseRadius(br);
      baseRadiusSlider.setValue(br);
      
      soundSensor.load(doc, null);
      
      System.out.println("Root element " + doc.getDocumentElement().getNodeName());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
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
