import processing.core.*;
abstract class Pattern implements PConstants{
  
  protected PApplet pApplet;
  public PGraphics squareTile;
  
  public Pattern(PApplet theApplet){
    pApplet = theApplet;
    squareTile = pApplet.createGraphics(2048, 2048, P3D);
  }
  
  public void render(PatternInput patternInput){
  }
  
  public int getPixel(int x, int y){
    int tx = x % (squareTile.width*2);
    int ty = y % (squareTile.height*2);
    if(tx<0) tx+=squareTile.width*2;
    if(ty<0) ty+=squareTile.height*2;
    if(tx > squareTile.width){
      tx = squareTile.width - (tx-squareTile.width);
    }
    if(ty > squareTile.height){
      ty = squareTile.height - (ty-squareTile.height);
    }
    
    /*
    int tx = x%squareTile.width;
    int ty = y%squareTile.height;
    */
    //return color(255, 0, 0);
    //return 2;
    return squareTile.get(tx, ty);
  }
  
  public abstract void setTileHeight(int h);
  
  public int getTileHeight(){
    return squareTile.height;
  }
  
  
  void wrapImage(PImage tile){
    int repX = 1 + PApplet.floor(pApplet.width / tile.width);
    int repY = 1 + PApplet.floor(pApplet.height / tile.height);
    for(int x=0; x<repX; x++){
      for(int y=0; y<repY; y++){
        pApplet.image(tile, x*tile.width, y*tile.height);
      }
    } 
  }

  void checkerWrapImage(PImage tile){
    int repX = 1 + PApplet.floor(pApplet.width / tile.width);
    int repY = 1 + PApplet.floor(pApplet.height / tile.height);
    int v = 0;
    int u = 0;
    pApplet.noStroke();
    boolean swapX = true;
    
    for(int x=0; x<repX; x++){
      pApplet.beginShape(QUAD_STRIP);
      pApplet.texture(tile);
      
      boolean swapY = true;
      for(int y=0; y<=repY; y++){  
        pApplet.vertex(x*tile.width, y*tile.height, swapX?tile.width:0, swapY?tile.height:0);
        pApplet.vertex((x+1)*tile.width, y*tile.height, swapX?0:tile.width, swapY?tile.height:0);
        
        swapY = !swapY;
      }
      pApplet.endShape();
      swapX = !swapX;
    }
    pApplet.image(tile, 0, 0);
    
  }
}
