import processing.core.*;
class PatternInput implements PConstants{
  private PImage img;
  private float u, v, r, a; //u and v are normalized coordinates, r radius, and a rotation angle
  private boolean changed = true;
  private PApplet pApplet;
  
  public float getU(){
    return u;
  }
  public float getV(){
    return v;
  }
  public void setU(float theU){
    u = theU;
    changed = true;
  }
  public void setV(float theV){
    v = theV;
    changed = true;
  }
  public void setRadius(float theR){
    r = theR;
    changed = true;
  }
  public float getRadius(){
    return r;
  }
  public void setAngle(float theA){
    a = theA % TWO_PI;
    changed = true;
  }
  public float getAngle(){
    return a;
  }
  
  //P6M specific
  int u1, v1, u2, v2, u3, v3; //uv coordinates of the triangle. 1:30degrees 2:90degrees 3:60degrees
  void updateUV(){
    if(changed){
      if (a == 0.0f){
        u2 = xNormToAbs(u);
        v2 = yNormToAbs(v);
        u1 = u2;
        int triL = yNormToAbs(r);
        v1 = v2 + triL;
        int triS = PApplet.floor(JPOrnament.TAN30 * triL);
        u3 = u2 + triS;
        v3 = v2;
      }
      else{
        u2 = xNormToAbs(u);
        v2 = yNormToAbs(v);
        u1 = xNormToAbs(u + r * pApplet.cos(a+pApplet.radians(90)));
        v1 = yNormToAbs(v + r * pApplet.sin(a+pApplet.radians(90)));
        u3 = xNormToAbs(u + r * JPOrnament.TAN30 * pApplet.cos(a));
        v3 = yNormToAbs(v + r * JPOrnament.TAN30 * pApplet.sin(a));
      }
      changed = false;
    }
  }
  
  public PatternInput(PApplet theApplet, String imageName){
    pApplet = theApplet;
    img = pApplet.loadImage(imageName);
    u = 0.7f;
    v = 0.2f;
    r = 0.05f;
    a = 0.0f;
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
    g.resize(p.width, 0);
    //g.resize(0, p.height);
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
  public PImage getImage() {
    return img;
  }
  public void setImage(PImage img){
    this.img = img;
  }
}
