import processing.core.*;

class Kreisspiegelung implements PConstants{
  private int x, y, r;
  private PImage img;
  private PApplet pApplet;
  public Kreisspiegelung(PApplet pApplet, int thex, int they, int ther){
    this.pApplet = pApplet;
    setXY(thex, they);
    setR(ther);
  }
  public void addToR(int dr){
    setR(r+dr);
  }
  void setR(int newR){
    r = newR;
    img = pApplet.createImage(2*r, 2*r, ARGB);
  }
  void mirror(Pattern pattern){
    for(int i=0; i<img.pixels.length; i++){
      int px = i % img.width;
      int py = (i - px)/img.width;
      int pcx = px-r;
      int pcy = py-r;
      float pr = PApplet.dist(pcx, pcy, 0, 0);//distance form center to desired pixel
      if(pr<r){
        //img.pixels[i] = color(255, 0, 0, 255);
        //img.pixels[i] = pattern.getPixel(x-r+px, y-r+py);
        
        float d = r*r/pr; //distance from center to the mirrorPixel
        // d / dy = pr / pcy
        // 1 / dy = pr / (pcy*d)
        // 1 = pr dy / pcy d
        // pcy d / pr = dy
        float dx = pcx * d / pr;//x coordinates of the mirror pixel relative to the center
        float dy = pcy * d / pr;//y coordinates of the mirror pixel relative to the center
          
        if(r!=0){
          //img.pixels[i] = pattern.getPixel(x+PApplet.round((d*(pcx))/r) ,y+PApplet.round((d*(pcy))/r));
          img.pixels[i] = pattern.getPixel(PApplet.round(dx+x), PApplet.round(dy+y));
        }
        else{
          img.pixels[i] = pApplet.color(0, 0, 0, 0);
        }
        
        
        //img.pixels[i] = pattern.getPixel(x+pcx-pattern.squareTile.width*2, y+pcy);//same pixels with x offset
      }
      else{
        img.pixels[i] = pApplet.color(0, 0, 0, 0);
      }
    }
    img.updatePixels();
  }
  void setXY(int newX, int newY){
    x = newX;
    y = newY;
  }
  void render(Pattern pattern){
    mirror(pattern);
    //img.filter(OPAQUE);
    pApplet.image(img, x-r, y-r);
  }
}
