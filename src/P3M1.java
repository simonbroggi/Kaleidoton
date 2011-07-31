import processing.core.PApplet;
class P3M1 extends Pattern{
  
  private float triL, triS, triHypo, triHypoL, triHypoS, triHeight;
  
  public P3M1(PApplet pApplet){
    super(pApplet);
    setTriL(256);
  }
  
  public void setTileHeight(int h){
    setTriL((float)h);
  }
  
  void setTriL(float newTriL){
    if(newTriL<=1024 && triL != newTriL){
      triL = newTriL;
      triS = JPOrnament.TAN30 * triL;
      triHypo = PApplet.sqrt(PApplet.pow(triL, 2)+PApplet.pow(triS, 2));
      triHypoL = PApplet.pow(triL, 2) / triHypo;
      triHypoS = triHypo - triHypoL;
      triHeight = PApplet.sqrt(triHypoL*triHypoS);
      squareTile = pApplet.createGraphics(PApplet.round(triHypo+triS), PApplet.round(triL), P3D);
    }
  }
  public void render(PatternInput pIn){
    
    squareTile.beginDraw();
    
    int triL = 20;
    
    squareTile.stroke(squareTile.color(255, 0, 0));
    //squareTile.noStroke();
    squareTile.beginShape(TRIANGLE_FAN);
    squareTile.texture(pIn.getImage());
    squareTile.vertex(0, squareTile.height, pIn.u1, pIn.v1);
    squareTile.vertex(0, 0, pIn.u2, pIn.v2);
    squareTile.vertex(triS, 0, pIn.u3, pIn.v3);
    squareTile.vertex(triHypoL, triHeight, pIn.u2, pIn.v2);
    squareTile.vertex(triHypo, squareTile.height, pIn.u3, pIn.v3);
    squareTile.endShape();
    
    squareTile.beginShape(TRIANGLE_FAN);
    squareTile.texture(pIn.getImage());
    squareTile.vertex(squareTile.width, 0, pIn.u1, pIn.v1);
    squareTile.vertex(squareTile.width, squareTile.height, pIn.u2, pIn.v2);
    squareTile.vertex(triHypo, squareTile.height, pIn.u3, pIn.v3);
    squareTile.vertex(triHypoL, triHeight, pIn.u2, pIn.v2);
    squareTile.vertex(triS, 0, pIn.u3, pIn.v3);
    squareTile.endShape();
    
    squareTile.endDraw();
    //pApplet.image(squareTile,0,0,squareTile.width,squareTile.height);
    checkerWrapImage(squareTile);
    //pApplet.image(squareTile, 0, 0);
    
  }
  
}
