class button {
  int mode;
  int x, y;
  int r ;
  int click = 0;
  color dc,lc,loc;
  String txt = "";
  
  boolean locked;
  

  public button(int x, int y, int mode, int r , color dc , color lc , color loc) {
    this.x = x;
    this.y = y;
    this.mode = mode;
    this.r = r;
    this.dc = dc;
    this.lc = lc;
    this.loc = loc;
  }
  
  void addTxt(String txt){
    this.txt = txt;
  }

  void run() {
    noStroke();
    int c;
    if (darkmode) {
      c = dc;
    } else {
      c = lc;
    }
    
    if(locked){
      c = loc;
    }
    if (mode == 3) {
      fill(c, 60);
      ellipse(x, y, 3*r, 3*r);
      fill(c);
      triangle(x-r, y, x+cos(PI/3)*r, y+sin(PI/3)*r, x+cos(PI/3)*r, y+sin(-1*PI/3)*r);
    }
    if (mode == 31) {
      fill(c, 60);
      ellipse(x, y, 3*r, 3*r);
      fill(c);
      triangle(x+r, y, x-cos(PI/3)*r, y+sin(PI/3)*r, x-cos(PI/3)*r, y+sin(-1*PI/3)*r);
    }
    if (mode == 0) {
      fill(c, 60);
      ellipse(x, y, 3*r, 3*r);
      fill(c);
      ellipse(x, y, 2*r, 2*r);
    }
    if (mode == 4) {
      fill(c, 60);
      ellipse(x, y, 3*r, 3*r);
      fill(c);
      quad(x-r, y, x, y+r, x+r, y,x,y-r);
    }
    if (mode == 2) {
      fill(c, 60);
      ellipse(x, y, 3*r, 3*r);
      fill(c);
      stroke(c);
      strokeWeight(width/66);
      line(x, y+r-width/66, x, y-r+width/66);
    }
    
    if (darkmode) {
      fill(0);
    } else {
      fill(255);
    }
    textSize(r*0.8);
    textAlign(CENTER);
    text(txt,x,y+r*0.3);
  }

  boolean pressed() {
    if (dist(x, y, mouseX, mouseY) <= 1.5*r && mousePressed) {
      return true;
    }
    return false;
  }
}
