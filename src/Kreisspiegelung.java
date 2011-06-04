import processing.core.*;

class Kreisspiegelung implements PConstants{
  private int x, y, r;
  private PImage img;
  Kreisspiegelung(PApplet pApplet, int thex, int they, int ther){
    setXY(thex, they);
    setR(pApplet, ther);
  }
  void setR(PApplet pApplet, int newR){
    r = newR;
    img = pApplet.createImage(2*r, 2*r, ARGB);
  }
  void mirror(PApplet pApplet, Pattern pattern){
    for(int i=0; i<img.pixels.length; i++){
      int px = i % img.width;
      int py = (i - px)/img.width;
      float pr = PApplet.dist(px, py, r, r);//distance form center to desired pixel
      if(pr<r){
        //img.pixels[i] = color(255, 0, 0, 255);
        //img.pixels[i] = pattern.getPixel(x-r+px, y-r+py);
        
        float d = r*r/pr; //distance from center to the mirrorPixel
        if(r!=0){
          img.pixels[i] = pattern.getPixel(x+PApplet.round((d*(px))/r) ,y+PApplet.round((d*(py))/r));
        }
        /*
        if(px-r!=0 && py-r!=0){
          img.pixels[i] = pattern.getPixel(r*r/(px-r), r*r/(py-r));
        }
        */
        else{
          img.pixels[i] = pApplet.color(0, 0, 0, 0);
        }
      }
      else{
        img.pixels[i] = pApplet.color(0, 0, 0, 0);
      }
    }
  }
  void setXY(int newX, int newY){
    x = newX;
    y = newY;
  }
  void render(PApplet pApplet, Pattern pattern){
    mirror(pApplet, pattern);
    //img.filter(OPAQUE);
    pApplet.image(img, x-r, y-r);
  }
}
