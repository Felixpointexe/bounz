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

public class sketch_210312c extends PApplet {


int lvl = 1;

ArrayList<Rectangle> Grectangles = new ArrayList<Rectangle>();
ArrayList<Rectangle> Rrectangles = new ArrayList<Rectangle>();

ArrayList<level> levels = new ArrayList<level>();






public void setup() {

  
  background(255);



  levels.add(new level(new float[][]{{1, 0.5f, 0.5f, 0.2f, 0.25f}, {0, 0, 0, 0.8f, 0.1f}}));
  levels.add(new level(new float[][]{{0, 0, 0.5f, 0.9f, 0.05f}, {0, 0.2f, 0.4f, 0.8f, 0.05f},{0, 0, 0.3f, 0.9f, 0.05f}, {0, 0.2f, 0.2f, 0.8f, 0.05f}}));
  levels.add(new level(new float[][]{{1, 0.5f, 0.5f, 0.2f, 0.25f}, {0, 0, 0, 0.8f, 0.1f}}));
}

public void draw() {
  if (lvl< levels.size()) {

    level level = levels.get(lvl-1);
    level.run();
  }
}
class level {

  int x, y;

  float a = 0;
  float bx, by;
  float bvx, bvy;
  int br;

  int selectable;

  ArrayList<Rectangle> Grectangles = new ArrayList<Rectangle>();
  ArrayList<Rectangle> Rrectangles = new ArrayList<Rectangle>();

  level(float[][] data) {
    for (int i = 0; i < data.length; i++) {
      if (data[i][0] == 0) {
        Grectangles.add(new Rectangle(width*data[i][1], height*data[i][2], width*data[i][3], height*data[i][4]));
      }
      if (data[i][0] == 1) {
        Rrectangles.add(new Rectangle(width*data[i][1], height*data[i][2], width*data[i][3], height*data[i][4]));
      }
    }

    reset();
  }

  public void run() {


    if (mousePressed && selectable < 2) {
      selectable = 1;
      background(255);
      stroke(0, 0, 0, 60);
      line(x, y, mouseX, mouseY);

      //println((atan2(y-mouseY, mouseX-x)+ TWO_PI ) % TWO_PI);
      a = (atan2(y-mouseY, mouseX-x)+ TWO_PI ) % TWO_PI;
    } else {
      if (selectable == 1) {
        selectable = 2;
        bvx = cos(a) * 6;
        bvy = -1*sin(a) * 6;
      }
      x = width/2;
      y = PApplet.parseInt(height*0.9f);
    }

    fill(0, 255, 0, 60);
    noStroke();
    ellipse(bx, by, 2 * br, 2 * br);

    bx += bvx;
    by += bvy;

    if (bx > width - br || bx < br) {
      bvx *= -1;
    }
    if (by > height - br ) {
      bvy *= -1;
    }
    if (by < br ) {
      lvl++;
      reset();
    }


    for (int i = 0; i < Grectangles.size(); i++) {

      Rectangle rectangle = Grectangles.get(i);

      //check X movment bounce
      if (bx + 2*br + bvx > rectangle.x && 
        bx + bvx < rectangle.x + rectangle.rectWidth && 
        by + 2*br > rectangle.y && 
        by < rectangle.y + rectangle.rectHeight) {

        bvx *= -1;
      }

      //check Y movement bounce
      if (bx + 2*br > rectangle.x && 
        bx < rectangle.x + rectangle.rectWidth && 
        by + 2*br + bvy > rectangle.y && 
        by + bvy < rectangle.y + rectangle.rectHeight) {


        bvy *= -1;
      }

      fill(0, 255, 0, 60);
      noStroke();
      rect(rectangle.x, rectangle.y, rectangle.rectWidth, rectangle.rectHeight, 30);
    }

    for (int i = 0; i < Rrectangles.size(); i++) {

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

      fill(255, 0, 0, 60);
      noStroke();
      rect(rectangle.x, rectangle.y, rectangle.rectWidth, rectangle.rectHeight, 30);
    }
    

    fill(210, 52, 235, 60);
    textSize(width/10);
    noStroke();
    text(lvl, width*0.1f, height*0.1f);
  }


  public void reset() {
    background(255);
    bx = width/2;
    by = PApplet.parseInt(height*0.9f);
    bvx = 0;
    bvy = 0;

    br = width/40;
    selectable = 0;
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
  public void settings() {  size(400, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_210312c" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
