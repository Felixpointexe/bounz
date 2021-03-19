class bounce{
  float x,y;
  public float r;
  
  public bounce( float x, float y){
    this.x = x;
    this.y = y;
    r = 0;
  }
  
  void run(){
    fill(133, 255, 171,255*30/r-20);
    ellipse(x,y,r,r);
    r+= width/100;
    
  }
  
  
}
