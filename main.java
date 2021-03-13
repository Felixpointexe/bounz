import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 



//game
game gm;
menu mn;

//user settings
boolean darkmode = true;

//app data
int page = 0;   //0 = menu ; 1 = game

public void setup() {

  
  background(255);


  gm = new game(5);
  mn = new menu();
}

public void draw() {
  
  if (page == 0) {
    mn.run();
  }

  if (page == 1) {
    gm.run();
  }
}

class bounce{
  float x,y;
  public float r;
  
  public bounce( float x, float y){
    this.x = x;
    this.y = y;
    r = 0;
  }
  
  public void run(){
    fill(133, 255, 171,255*30/r-20);
    ellipse(x,y,r,r);
    r+= 3;
    
  }
  
  
}
class button {
  int mode;
  int x, y;
  int r , col;
  int click = 0;

  public button(int x, int y, int mode, int r , int c) {
    this.x = x;
    this.y = y;
    this.mode = mode;
    this.r = r;
    this.col = c;
  }

  public void run() {
    int c;
    if (darkmode) {
      c = (1-col)*255;
    } else {
      c = (col)*255;
    }
    if (mode == 3) {
      fill(c, 60);
      ellipse(x, y, 3*r, 3*r);
      fill(c);
      triangle(x-r, y, x+cos(PI/3)*r, y+sin(PI/3)*r, x+cos(PI/3)*r, y+sin(-1*PI/3)*r);
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
  }

  public boolean pressed() {
    if (dist(x, y, mouseX, mouseY) <= 1.5f*r && mousePressed) {
      return true;
    }
    return false;
  }
}
class game {
  public int lvl;

  ArrayList<level> levels = new ArrayList<level>();

  game(int sLvl) {
    lvl = sLvl;

    //adds lvls
    /*
     *   height*0.04
     *
     */
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{{0, width*0, height*0.38f, width*0.6f, height*0.04f}}));
    levels.add(new level(new float[][]{{0, width*0, height*0.38f, width*0.6f, height*0.04f}, {0, width*0.4f, height*0.18f, width*0.6f, height*0.04f}}));
    levels.add(new level(new float[][]{{0, width*0, height*0.38f, width*0.6f, height*0.04f}, {0, width*0.4f, height*0.18f, width*0.6f, height*0.04f}, {0, width*0.4f, height*0.58f, width*0.6f, height*0.04f}}));
    levels.add(new level(new float[][]{{0, width*0, height*0.5f, width*0.7f, height*0.04f}, {0, width*0.3f, height*0.2f, width*0.7f, height*0.04f}, {0, width*0.3f, height*0.2f, height*0.04f, height*0.15f}, {0, width*0.7f-height*0.04f, height*0.39f,height*0.04f, height*0.15f}}));
    levels.add(new level(new float[][]{{0, width*0, height*0.38f, width*0.5f, height*0.04f}, {1, width*0.5f, height*0.18f, width*0.5f, height*0.04f}}));
    levels.add(new level(new float[][]{{1, width*0.5f-height*0.02f, height*0.25f, height*0.04f, height*0.2f}, {1, width*0.5f-height*0.02f, height*0.55f, height*0.04f, height*0.2f}, {0, 0, height*0.71f, width*0.5f+height*0.02f, height*0.04f}, {0, width*0.5f-height*0.02f, height*0.25f, width*0.5f+height*0.02f, height*0.04f}}));
    levels.add(new level(new float[][]{{0, width*0, height*0.3f, width*0.2f, height*0.04f}, {0, width*0.4f, height*0.3f, width*0.6f, height*0.04f}, {0, 0, height*0.6f, width*0.6f, height*0.04f}, {0, width*0.8f, height*0.6f, width*0.2f, height*0.04f}}));
    levels.add(new level(new float[][]{{0, width*0, height*0.2f, width*0.43f, height*0.04f}, {0, width*0.57f, height*0.2f, width*0.43f, height*0.04f}}));
    levels.add(new level(new float[][]{{0, width*0.48f, height*0.45f, width*0.04f, height*0.05f}, {0, width*0.48f, height*0.6f, width*0.04f, height*0.05f}, {0, width*0.48f, height*0.75f, width*0.04f, height*0.05f}, {1, 0, height*0.42f, width*0.7f, height*0.02f}, {1, width*0.5f, height*0.3f, width*0.5f, height*0.02f}}));
    levels.add(new level(new float[][]{}));
  }

  public void run() {

    if (lvl <= levels.size()) {
      level level = levels.get(lvl-1);
      level.run();
    }
  }
  
  public void reset(){
    level level = levels.get(lvl-1);
    level.reset();
  }
}

/*

 class: level
 
 */


class level {

  button back_btn;
  button restart_btn;

  float bx, by;   //ball x  &  ball y
  float bvx, bvy;   //ball x speed & ball y speed
  int br;   //ball radius
  float ba = 0;   //ball angle

  int t = 0;

  int status = 0;   //status of game | 0 = waiting for player to give direction ; 1 = waiting for player to release mouse ; 2 = waiting for reset()

  ArrayList<Rectangle> Grectangles = new ArrayList<Rectangle>();   //list of all "good" rectangles
  ArrayList<Rectangle> Rrectangles = new ArrayList<Rectangle>();   //list of all "bad" rectangles
  ArrayList<bounce> bounces = new ArrayList<bounce>();

  /*
     constructor(int[][])
   -level data
   */
  level(float[][] data) {
    for (int i = 0; i < data.length; i++) {

      if (data[i][0] == 0) {   //if 1st parameter is 0 = "good" rectangle
        Grectangles.add(new Rectangle(data[i][1], data[i][2], data[i][3], data[i][4]));
      }
      if (data[i][0] == 1) {   //if 2nd parameter is 1 = "bad" rectangle
        Rrectangles.add(new Rectangle(data[i][1], data[i][2], data[i][3], data[i][4]));
      }
    }

    reset();   //resets level


    back_btn = new button(PApplet.parseInt(width*0.1f), PApplet.parseInt(height*0.05f), 3, PApplet.parseInt(width*0.03f),1);
    restart_btn = new button(PApplet.parseInt(width*0.9f), PApplet.parseInt(height*0.05f), 0, PApplet.parseInt(width*0.03f),1);
  }

  /*
     methode run()
   : one tic 
   */
  public void run() {

    if (restart_btn.pressed()) {
      reset();
    }
    if (back_btn.pressed()) {
      page = 0;
      reset();
    }

    t++;

    if (darkmode) {
      background(0);
    } else {
      background(255);
    }



    if (mousePressed && status < 2 && t > 10) {   //if mousePressed & status is "waiting for player to give direction" or "waiting for player to release mouse"

      status = 1;   //sets staus to "waiting for player to release mouse"

      if (darkmode) {   //line color
        stroke(255);
      } else {
        stroke(0);
      }   
      strokeWeight(width/400);
      //line(width/2, int(height*0.9), mouseX, mouseY);   //line for aming

      for (int i = 0; i < 100; i++) {
        if ((i+t*(width/40-1)) % (width/40) < width/80) {
          line(width/2 + i*(mouseX-width/2)/100, height*0.9f - i*(height*0.9f-mouseY)/100, width/2 + (i-1)*(mouseX-width/2)/100, height*0.9f - (i-1)*(height*0.9f-mouseY)/100);
        }
      }

      ba = (atan2(height*0.9f-mouseY, mouseX-width/2)+ TWO_PI ) % TWO_PI;   //calculates the angle for the ball
    } else {

      if (status == 1) {   //if status is "waiting for player to release mouse"
        status = 2;   //sets status to "waiting for reset()"

        bvx = cos(ba) * width/66;   //calculates ball x speed
        bvy = -1*sin(ba) * width/66;   //calculates ball y speed
      }
    }




    if (bx > width - br || bx < br) {   //if ball have to bounce on right or left wall
      bvx *= -1;
      bounces.add(new bounce(bx, by));
    }
    if (by > height*0.98f - br ) {   //if ball is at buttom screen
      reset();
    }
    if (by < br + height/10 ) {   //if buttom is at top screen
      reset();
      gm.lvl++;
      gm.reset();
    }


    for (int i = 0; i < Grectangles.size(); i++) {   //all "good" rectangles

      Rectangle rectangle = Grectangles.get(i);

      //check X movment bounce
      if (bx + 1*br + bvx > rectangle.x && 
        bx + bvx < rectangle.x + rectangle.rectWidth && 
        by + 1*br > rectangle.y && 
        by < rectangle.y + rectangle.rectHeight) {

        bvx *= -1;
        bounces.add(new bounce(bx, by));
      }

      //check Y movement bounce
      if (bx + 1*br > rectangle.x && 
        bx < rectangle.x + rectangle.rectWidth && 
        by + 1*br + bvy > rectangle.y && 
        by + bvy < rectangle.y + rectangle.rectHeight) {


        bvy *= -1;
        bounces.add(new bounce(bx, by));
      }

      noStroke();

      fill(66, 170, 245);
      rect(rectangle.x, rectangle.y, rectangle.rectWidth, rectangle.rectHeight, 80);
      fill(66, 170, 245, 60);
      rect(rectangle.x-width/20, rectangle.y-width/20, rectangle.rectWidth+width/10, rectangle.rectHeight+width/10, 80);
    }

    for (int i = 0; i < Rrectangles.size(); i++) {   //all "bad" rectangles

      Rectangle rectangle = Rrectangles.get(i);

      //check X movment bounce
      if (bx + 2*br + bvx > rectangle.x && 
        bx + bvx < rectangle.x + rectangle.rectWidth && 
        by + 2*br > rectangle.y && 
        by < rectangle.y + rectangle.rectHeight) {

        reset();
      }

      //check Y movement bounce
      if (bx + 2*br > rectangle.x && 
        bx < rectangle.x + rectangle.rectWidth && 
        by + 2*br + bvy > rectangle.y && 
        by + bvy < rectangle.y + rectangle.rectHeight) {


        reset();
      }
      noStroke();


      fill(255, 115, 129);
      rect(rectangle.x, rectangle.y, rectangle.rectWidth, rectangle.rectHeight, 80);
      fill(255, 115, 129, 60);
      rect(rectangle.x-width/20, rectangle.y-width/20, rectangle.rectWidth+width/10, rectangle.rectHeight+width/10, 80);
    }

    for (int i = 0; i < bounces.size(); i++) {
      if ( bounces.get(i).r < width/2) {
        bounces.get(i).run();
      }
    }


    noStroke();

    fill(66, 170, 245);
    rect(0, 0, width, height/10);   //tob bar
    fill(133, 255, 171);
    rect(0, height/10, width, height*0.02f);
    fill(133, 255, 171, 120);
    rect(0, height*0.12f, width, height*0.02f);

    fill(255, 115, 129);   //buttom bar
    rect(0, height*0.98f, width, height*0.02f);
    fill(255, 115, 129, 60);
    rect(0, height*0.96f, width, height*0.02f);

    if (darkmode) {
      fill(0);
    } else {
      fill(255);
    }
    textSize(width/10);
    textAlign(CENTER);
    text(gm.lvl, width/2, height*0.07f);

    fill(133, 255, 171, 120);
    ellipse(width/2, PApplet.parseInt(height*0.9f), 4 * br, 4 * br);   //starting point

    fill(133, 255, 171);
    ellipse(bx, by, 2 * br, 2 * br);   //ball



    bx += bvx;   //calculates new ball x
    by += bvy;   //calculates new ball y

    back_btn.run();
    restart_btn.run();
  }


  public void reset() {
    background(255);
    bvx = 0;
    bvy = 0;
    t = 0;

    bx = width/2;
    by = PApplet.parseInt(height*0.9f);

    br = width/40;
    status = 0;
  }
}
class menu {

  button darkmode_btn;

  ArrayList<button> buttons = new ArrayList<button>();

  menu() {


    
    darkmode_btn = new button(PApplet.parseInt(width*0.9f), PApplet.parseInt(height*0.05f), 4, PApplet.parseInt(width*0.03f), 1);

    for ( int i = 0; i < gm.levels.size(); i++) {
      buttons.add(new button(i % 3 * 100 + ( width/2 - 100), PApplet.parseInt((i - i%3) / 3 * 100 + height*0.2f), 0, width/10, 0));
    }
  }

  public void run() {


    if (darkmode) {
      background(0);
    } else {
      background(255);
    }

    noStroke();

    fill(66, 170, 245);
    rect(0, 0, width, height/10);   //tob bar

    darkmode_btn.run();

    for (int i = 0; i < buttons.size(); i++) {
      buttons.get(i).run();
      if (buttons.get(i).pressed()) {
        gm.lvl = i+1;
        gm.reset();
        page = 1;
      }
    }

    if (darkmode_btn.pressed()) {

      if (darkmode_btn.click == 0) {
        darkmode_btn.click = 1;
        if (darkmode) {
          darkmode = false;
        } else {
          darkmode = true;
        }
      }
    } else {
      darkmode_btn.click = 0;
    }
  }
}
class Rectangle {
  float x;
  float y;
  float rectWidth;
  float rectHeight;

  public Rectangle(float x, float y, float rectWidth, float rectHeight) {
    this.x = x;
    this.y = y;
    this.rectWidth = rectWidth;
    this.rectHeight = rectHeight;
  }
}

