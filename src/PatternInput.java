import processing.core.*;
class PatternInput implements PConstants{
  PImage img;
  float u, v, r;
  
  //P6M specific
  int u1, v1, u2, v2, u3, v3; //uv coordinates of the triangle. 1:30degrees 2:90degrees 3:60degrees
  void updateUV(){
    u2 = xNormToAbs(u);
    v2 = yNormToAbs(v);
    u1 = u2;
    int triL = yNormToAbs(r);
    v1 = v2 + triL;
    int triS = PApplet.floor(JPOrnament.TAN30 * triL);
    u3 = u2 + triS;
    v3 = v2;
  }
  
  
  public PatternInput(PApplet pApplet, String imageName){
    img = pApplet.loadImage(imageName);
    u = 0.7f;
    v = 0.2f;
    r = 0.05f;
    updateUV();
  }
  
  
  void render(PApplet p){
    //render for monitor
    PGraphics g = p.createGraphics(img.width, img.height, P3D);
    g.beginDraw();
    g.image(img, 0, 0);
    g.rectMode(CORNERS);
    g.noFill();
    g.stroke(p.color(255, 0, 0));
    g.ellipseMode(CENTER);
    g.ellipse(xNormToAbs(u), yNormToAbs(v), xNormToAbs(r*2), yNormToAbs(r*2));
    g.ellipse(u3, v3, 10, 10);
    g.beginShape(TRIANGLES);
    g.vertex(u1, v1);
    g.vertex(u2, v2);
    g.vertex(u3, v3);
    g.endShape();
    //g.rect(xNormToAbs(0), yNormToAbs(0), xNormToAbs(1), yNormToAbs(1));
    g.endDraw();
    //g.resize(p.width, 0);
    g.resize(0, p.height);
    p.image(g, 0, 0);
  }
  
  int xNormToAbs(float n){
    if(img == null) return 0;
    else return PApplet.floor(n*img.width);
  }
  int yNormToAbs(float n){
    if(img == null) return 0;
    else return PApplet.floor(n*img.height);
  }
}
