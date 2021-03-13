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



game gm;

public void setup() {

  
  background(255);

  gm = new game(9);
}

public void draw() {

  gm.run();
}

class game {
  public int lvl;

  ArrayList<level> levels = new ArrayList<level>();

  game(int sLvl) {
    lvl = sLvl;

    //adds lvls
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{{0, 0, 0.38f, 0.6f, 0.04f}}));
    levels.add(new level(new float[][]{{0, 0, 0.38f, 0.6f, 0.04f}, {0, 0.4f, 0.18f, 0.6f, 0.04f}}));
    levels.add(new level(new float[][]{{0, 0, 0.38f, 0.6f, 0.04f}, {0, 0.4f, 0.18f, 0.6f, 0.04f}, {0, 0.4f, 0.58f, 0.6f, 0.04f}}));
    levels.add(new level(new float[][]{{0, 0, 0.5f, 0.7f, 0.04f}, {0, 0.3f, 0.2f, 0.7f, 0.04f}, {0, 0.3f, 0.2f, 0.1f, 0.15f}, {0, 0.6f, 0.39f, 0.1f, 0.15f}}));
    levels.add(new level(new float[][]{{0, 0, 0.38f, 0.5f, 0.04f}, {1, 0.5f, 0.18f, 0.5f, 0.04f}}));
    levels.add(new level(new float[][]{{1, 0.48f, 0.25f, 0.04f, 0.2f}, {1, 0.48f, 0.55f, 0.04f, 0.2f}, {0, 0, 0.71f, 0.52f, 0.04f}, {0, 0.48f, 0.25f, 0.54f, 0.04f}}));
    levels.add(new level(new float[][]{{0,0,0.3f,0.2f,0.04f},{0,0.4f,0.3f,0.6f,0.04f},{0,0,0.6f,0.6f,0.04f},{0,0.8f,0.6f,0.2f,0.04f}}));
    levels.add(new level(new float[][]{{0,0,0.2f,0.43f,0.04f},{0,0.57f,0.2f,0.43f,0.04f}}));
    levels.add(new level(new float[][]{{0, 0.48f, 0.45f, 0.04f, 0.05f}, {0, 0.48f, 0.6f, 0.04f, 0.05f},{0, 0.48f, 0.75f, 0.04f, 0.05f},{1,0,0.42f,0.7f,0.02f},{1,0.5f,0.3f,0.5f,0.02f}}));
    levels.add(new level(new float[][]{}));
  }

  public void run() {

    if (lvl <= levels.size()) {
      level level = levels.get(lvl-1);
      level.run();
    }
    
  }
}

/*

        class: level
        
*/


class level {

  float bx, by;   //ball x  &  ball y
  float bvx, bvy;   //ball x speed & ball y speed
  int br;   //ball radius
  float ba = 0;   //ball angle

  int status = 0;   //status of game | 0 = waiting for player to give direction ; 1 = waiting for player to release mouse ; 2 = waiting for reset()

  ArrayList<Rectangle> Grectangles = new ArrayList<Rectangle>();   //list of all "good" rectangles
  ArrayList<Rectangle> Rrectangles = new ArrayList<Rectangle>();   //list of all "bad" rectangles

  /*
     constructor(int[][])
     -level data
  */
  level(float[][] data) {
    for (int i = 0; i < data.length; i++) {
      
      if (data[i][0] == 0) {   //if 1st parameter is 0 = "good" rectangle
        Grectangles.add(new Rectangle(width*data[i][1], height*data[i][2], width*data[i][3], height*data[i][4]));
      }
      if (data[i][0] == 1) {   //if 2nd parameter is 1 = "bad" rectangle
        Rrectangles.add(new Rectangle(width*data[i][1], height*data[i][2], width*data[i][3], height*data[i][4]));
      }
      
    }

    reset();   //resets level
  }
  
  /*
     methode run()
     : one tic 
  */
  public void run() {

    background(255);
    
    if (mousePressed && status < 2) {   //if mousePressed & status is "waiting for player to give direction" or "waiting for player to release mouse"
      
      status = 1;   //sets staus to "waiting for player to release mouse"

      stroke(0, 0, 0, 60);   //line color
      line(width/2, PApplet.parseInt(height*0.9f), mouseX, mouseY);   //line for aming

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
    }
    if (by > height - br ) {   //if ball is at buttom screen
      reset();
    }
    if (by < br + height/10 ) {   //if buttom is at top screen
      reset();
      gm.lvl++;
    }


    for (int i = 0; i < Grectangles.size(); i++) {   //all "good" rectangles

      Rectangle rectangle = Grectangles.get(i);

      //check X movment bounce
      if (bx + 1*br + bvx > rectangle.x && 
        bx + bvx < rectangle.x + rectangle.rectWidth && 
        by + 1*br > rectangle.y && 
        by < rectangle.y + rectangle.rectHeight) {

        bvx *= -1;
      }

      //check Y movement bounce
      if (bx + 1*br > rectangle.x && 
        bx < rectangle.x + rectangle.rectWidth && 
        by + 1*br + bvy > rectangle.y && 
        by + bvy < rectangle.y + rectangle.rectHeight) {


        bvy *= -1;
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
    
    noStroke();
    
    fill(66, 170, 245);
    rect(0,0,width,height/10);   //tob bar

    fill(240, 171, 255);
    textSize(width/10);
    textAlign(CENTER);
    text(gm.lvl, width/2, height*0.07f);
    
    fill(133, 255, 171, 120);
    ellipse(width/2, PApplet.parseInt(height*0.9f), 4 * br, 4 * br);   //starting point
    
    fill(133, 255, 171);
    ellipse(bx, by, 2 * br, 2 * br);   //ball
    


    bx += bvx;   //calculates new ball x
    by += bvy;   //calculates new ball y
  }


  public void reset() {
    background(255);
    bvx = 0;
    bvy = 0;
    
    bx = width/2;
    by = PApplet.parseInt(height*0.9f);

    br = width/40;
    status = 0;
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

