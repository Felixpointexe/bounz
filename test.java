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




import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
import android.app.Activity;

Activity act;
Context cnt;
SharedPreferences sp;
SharedPreferences.Editor editor;



//game
game gm;
menu mn;
help hlp;

//user settings
boolean darkmode = false;
boolean music =true;

//app data
int page = 0;   //0 = menu ; 1 = game
int unlockedLvl = 10;
String unlockedLvlFile;


//colors
int blue = color(66, 170, 245);
int green = color(133, 255, 171);
int red = color(255, 115, 129);
int yellow = color(242, 247, 96);
int gray = color(138, 138, 138);


public void setup() {

  
  background(255);
  orientation(PORTRAIT);  


  gm = new game(5);
  mn = new menu();
  hlp = new help();
  
  
  act = this.getActivity();
  cnt = act.getApplicationContext();
  sp = PreferenceManager.getDefaultSharedPreferences(cnt);
  editor = sp.edit();

  unlockedLvlFile = "unlockedLvl";
  unlockedLvl = load(unlockedLvlFile);
  
}

public void draw() {

  if (page == 0) {
    mn.run();
  }

  if (page == 1) {
    gm.run();
  }
  
  if (page == 2) {
    hlp.run();
  }
}


void save(int value, String name) {
  //editor.clear();
  editor.putInt(name, value);
  editor.commit();
}

int load(String name) {
  return sp.getInt(name, 0);
}

class ball {

  float x, y;   //ball x  &  ball y
  float vx, vy;   //ball x speed & ball y speed
  int r;   //ball radius
  float a = 0;   //ball angle
  float v = 0;

  ball(float x, float y, float vx, float vy, int r, float v) {
    this.x = x;
    this.y = y;
    this.vx = vx;
    this.vy = vy;
    this.r = r;
    this.v = v;
  }

  public void run() {

    if (x > width - r || x < r) {   //if ball have to bounce on right or left wall
      a = PI - a;
      gm.levels.get(gm.lvl-1).bounces.add(new bounce(x, y));
    }
    if (y > height*0.98f - r ) {   //if ball is at buttom screen
      gm.levels.get(gm.lvl-1).reset();
    }
    if (y < r + height/10 ) {   //if buttom is at top screen
      //println(y);
      gm.reset();
      gm.lvl++;
      unlockedLvl ++;
      save(unlockedLvl,unlockedLvlFile);
      gm.reset();
    }

    for (int i = 0; i < gm.levels.get(gm.lvl-1).Grectangles.size(); i++) {   //all "good" rectangles

      Rectangle rectangle = gm.levels.get(gm.lvl-1).Grectangles.get(i);

      //check X movment bounce
      if (x + 1*r + vx > rectangle.x && 
        x + vx < rectangle.x + rectangle.rectWidth && 
        y + 1*r > rectangle.y && 
        y < rectangle.y + rectangle.rectHeight) {

        println("ball #1");

        a = PI - a;
        gm.levels.get(gm.lvl-1).bounces.add(new bounce(x, y));
      }

      //check Y movement bounce
      if (x + 1*r > rectangle.x && 
        x < rectangle.x + rectangle.rectWidth && 
        y + 1*r + vy > rectangle.y && 
        y + vy < rectangle.y + rectangle.rectHeight) {

        println("ball #2");


        a = TWO_PI - a;
        gm.levels.get(gm.lvl-1).bounces.add(new bounce(x, y));
      }
    }
    for (int i = 0; i < gm.levels.get(gm.lvl-1).Rrectangles.size(); i++) {   //all "bad" rectangles

      Rectangle rectangle = gm.levels.get(gm.lvl-1).Rrectangles.get(i);

      //check X movment bounce
      if (x + 2*r + vx > rectangle.x && 
        x + vx < rectangle.x + rectangle.rectWidth && 
        y + 2*r > rectangle.y && 
        y < rectangle.y + rectangle.rectHeight) {

        gm.levels.get(gm.lvl-1).reset();
      }

      //check Y movement bounce
      if (x + 2*r > rectangle.x && 
        x < rectangle.x + rectangle.rectWidth && 
        y + 2*r + vy > rectangle.y && 
        y + vy < rectangle.y + rectangle.rectHeight) {


        gm.levels.get(gm.lvl-1).reset();
      }
    }
    for (int i = 0; i < gm.levels.get(gm.lvl-1).portales.size(); i++) {   

      portal portal = gm.levels.get(gm.lvl-1).portales.get(i);

      noStroke();

      if (dist(portal.x, portal.y, x, y) < portal.portalWidth + r) {
        if (portal.locked == false) {
          if (gm.levels.get(gm.lvl-1).portales.size() > 1) {
            int r = i;
            while (i == r) {
              r = PApplet.parseInt(random(0, gm.levels.get(gm.lvl-1).portales.size()));
            }
            gm.levels.get(gm.lvl-1).portales.get(r).locked = true;
            x = gm.levels.get(gm.lvl-1).portales.get(r).x;
            y = gm.levels.get(gm.lvl-1).portales.get(r).y;
          }
        }
      } else {
        gm.levels.get(gm.lvl-1).portales.get(i).locked = false;
      }
    }
    for (int i = 0; i < gm.levels.get(gm.lvl-1).tornados.size(); i++) {   

      tornado tornado = gm.levels.get(gm.lvl-1).tornados.get(i);

      float toM = (atan2(tornado.y-y, tornado.x-x) + TWO_PI ) % TWO_PI;
      float dir = ((TWO_PI-a) + TWO_PI ) % TWO_PI;

      stroke(0);
      line(width/2, height/2, width/2 + cos(toM)*100, height/2 + sin(toM)*100);
      line(width/2, height/2, width/2 + cos(dir)*100, height/2 + sin(dir)*100);

      println(dir);

      if (toM-dir < PI) {
        line(width/2, height/2, width/2 + cos((toM-dir)*0.5f + dir)*100, height/2 + sin(((toM-dir))*0.5f + dir)*100);
      } else {
        line(width/2, height/2, width/2 + cos(dir- (PI-(toM-dir)*0.5f ))*100, height/2 + sin(dir- (PI-(toM-dir)*0.5f))*100);
      }



      noStroke();

      if (dist(tornado.x, tornado.y, x, y) < tornado.tornadoWidth/2 + r) {
        float n = 0.05f * ( dist(tornado.x, tornado.y, x, y) / (tornado.tornadoWidth/2));


        if (toM-dir < PI) {
          dir = (toM-dir)*n+dir;
        } else {
          dir = dir - (PI - (toM-dir)*n);
        }


        a = TWO_PI - dir;
      } else {
      }
    }



    //println(a);
    noStroke();

    fill(green, 120);
    ellipse(width/2, PApplet.parseInt(height*0.9f), 4 * r, 4 * r);   //starting point

    fill(green);
    ellipse(x, y, 2 * r, 2 * r);   //ball


    vx = cos(a) * v;   //calculates ball x speed
    vy = -1*sin(a) * v;   //calculates ball y speed

    x += vx;   //calculates new ball x
    y += vy;   //calculates new ball y
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
    r+= width/100;
    
  }
  
  
}
class button {
  int mode;
  int x, y;
  int r ;
  int click = 0;
  int dc,lc,loc;
  String txt = "";
  
  boolean locked;
  

  public button(int x, int y, int mode, int r , int dc , int lc , int loc) {
    this.x = x;
    this.y = y;
    this.mode = mode;
    this.r = r;
    this.dc = dc;
    this.lc = lc;
    this.loc = loc;
  }
  
  public void addTxt(String txt){
    this.txt = txt;
  }

  public void run() {
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
    textSize(r*0.8f);
    textAlign(CENTER);
    text(txt,x,y+r*0.3f);
  }

  public boolean pressed() {
    if (dist(x, y, mouseX, mouseY) <= 1.5f*r && mousePressed) {
      return true;
    }
    return false;
  }
}
class Ellipse {
  float x;
  float y;
  float ellipseWidth;
  float ellipseHeight;

  public Ellipse(float x, float y, float ellipseWidth, float ellipseHeight) {
    this.x = x;
    this.y = y;
    this.ellipseWidth = ellipseWidth;
    this.ellipseHeight = ellipseHeight;
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
    levels.add(new level(new float[][]{{0,0,height*0.5f,width*0.6f,height*0.04f}}));
    levels.add(new level(new float[][]{{0,0,height*0.4f,width*0.6f,height*0.04f},{0,width*0.4f,height*0.6f,width*0.6f,height*0.04f}}));
    levels.add(new level(new float[][]{{0,0,height*0.3f,width*0.6f,height*0.04f},{0,width*0.4f,height*0.5f,width*0.6f,height*0.04f},{0,0,height*0.7f,width*0.6f,height*0.04f}}));
    levels.add(new level(new float[][]{{1,0,height*0.3f,width*0.6f,height*0.04f},{0,width*0.4f,height*0.6f,width*0.6f,height*0.04f}}));
    levels.add(new level(new float[][]{{2,width*0.2f,height*0.7f,height*0.04f,height*0.04f},{2,width*0.8f,height*0.4f,height*0.04f,height*0.04f},{1,0,height*0.5f,width,height*0.04f}}));
    levels.add(new level(new float[][]{{3,width*0.9f,height*0.55f,width/2,width/2}}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
    levels.add(new level(new float[][]{}));
  }

  public void run() {

    if (lvl <= levels.size()) {
       levels.get(lvl-1).run();
    }
  }

  public void reset() {
    if (lvl <= levels.size()) {
      level level = levels.get(lvl-1);
      level.reset();
      println(levels.get(lvl-1).t);
    } else {
      mn.y = 0;
      page = 0;
    }
  }
}
class help {

  button back_btn;

  int y, ys;
  int Mstatus;

  help() {
    back_btn = new button(PApplet.parseInt(width*0.1f), PApplet.parseInt(height*0.05f), 3, PApplet.parseInt(width*0.03f), color(0), color(255), color(0));
  }

  public void run() {

    if (darkmode) {
      background(0);
    } else {
      background(255);
    }

    noStroke();



    if (mousePressed && mouseY > height*0.1f) {

      if (Mstatus == 0) {
        Mstatus = 1;
        ys = mouseY-y;
      } else {

        y = mouseY - ys;
        if (y > 0) y = 0;
        if(y < -1* width*2.5f + height*0.8f) y = PApplet.parseInt(-1* width*2.5f+ height*0.8f);
      }
    } else {
      Mstatus = 0;
    }
    
    if (darkmode) {
      fill(255);
    } else {
      fill(0);
    }
    textSize(width*0.05f);
    textAlign(LEFT);

    text("The goal is it to shoot the green ball to the top of the sreen by tipping or draging the aming line. When released the ball will shoot automaticly.", width*0.1f, height*0.15f+y, width*0.8f, height);
    text("blue boxes are 'good' boxes which let the ball bounce. If the ball touches a red box, the level will reset.", width*0.1f, width*0.7f+y, width*0.8f, height);
    
    stroke (blue);
    strokeWeight(width/120);
    line(0,width*1.1f+y , width,width*1.1f+y);
    
    text("Das Ziel ist es den grünen Ball an den oberen Bildschirmrand zu schießen, indem man durch tipen oder ziehen die Ziel-linie bewegt. Wenn man los lässt schießt der Ball automatisch", width*0.1f, width*1.2f+y, width*0.8f, height);
    text("Blau Boxen sind 'gute' Boxen, welche den Ball abprallen lassen. Wenn der Ball eine rote Box berührt, wird das level zurück gesetzt.", width*0.1f, width*1.7f+y, width*0.8f, height);
    
    line(0,width*2.3f+y , width,width*2.3f+y);
    
    textAlign(CENTER);

    text("made by Felix", width*0.1f, width*2.4f+y, width*0.8f, height);
    textSize(width*0.035f);
    text("https://github.com/Felixpointexe/bounz", width*0.1f, width*2.5f+y, width*0.8f, height);


    fill(blue);
    noStroke();
    rect(0, 0, width, height/10);   //tob bar

    back_btn.run();

    if (back_btn.pressed()) {
      mn.y = 0;
      page = 0;
    }


    
  }
}
/*

 class: level
 
 */


class level {

  button back_btn;
  button restart_btn;

  ball bll;



  int t = 0;

  int status = 0;   //status of game | 0 = waiting for player to give direction ; 1 = waiting for player to release mouse ; 2 = waiting for reset()

  ArrayList<Rectangle> Grectangles = new ArrayList<Rectangle>();   //list of all "good" rectangles
  ArrayList<Rectangle> Rrectangles = new ArrayList<Rectangle>();   //list of all "bad" rectangles
  ArrayList<portal> portales = new ArrayList<portal>();   //list of all portales
  ArrayList<tornado> tornados = new ArrayList<tornado>();   //list of all portales
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
      if (data[i][0] == 2 ) {
        portales.add(new portal(data[i][1], data[i][2], data[i][3], data[i][4]));
      }
      if (data[i][0] == 3 ) {
        tornados.add(new tornado(data[i][1], data[i][2], data[i][3], data[i][4]));
      }
    }



    reset();   //resets level


    back_btn = new button(PApplet.parseInt(width*0.1f), PApplet.parseInt(height*0.05f), 3, PApplet.parseInt(width*0.03f), color(0), color(255), color(0));
    restart_btn = new button(PApplet.parseInt(width*0.9f), PApplet.parseInt(height*0.05f), 0, PApplet.parseInt(width*0.03f), color(0), color(255), color(0));
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
      mn.y = 0;
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

      bll.a = (atan2(height*0.9f-mouseY, mouseX-width/2)+ TWO_PI ) % TWO_PI;   //calculates the angle for the ball
    } else {

      if (status == 1) {   //if status is "waiting for player to release mouse"
        status = 2;   //sets status to "waiting for reset()"

        bll.v = width/300;
      }
    }





    noStroke();

    for (int i = 0; i < Grectangles.size(); i++) {   //all "good" rectangles
      fill(blue);
      rect(Grectangles.get(i).x, Grectangles.get(i).y, Grectangles.get(i).rectWidth, Grectangles.get(i).rectHeight, 80);
      fill(blue, 60);
      rect(Grectangles.get(i).x-width/20, Grectangles.get(i).y-width/20, Grectangles.get(i).rectWidth+width/10, Grectangles.get(i).rectHeight+width/10, 80);
    }

    for (int i = 0; i < Rrectangles.size(); i++) {   //all "bad" rectangles
      fill(red);
      rect(Rrectangles.get(i).x, Rrectangles.get(i).y, Rrectangles.get(i).rectWidth, Rrectangles.get(i).rectHeight, 80);
      fill(red, 60);
      rect(Rrectangles.get(i).x-width/20, Rrectangles.get(i).y-width/20, Rrectangles.get(i).rectWidth+width/10, Rrectangles.get(i).rectHeight+width/10, 80);
    }

    for (int i = 0; i < portales.size(); i++) {   
      fill(yellow);
      ellipse(portales.get(i).x, portales.get(i).y, portales.get(i).portalWidth, portales.get(i).portalHeight);
      fill(yellow, 60);
      ellipse(portales.get(i).x, portales.get(i).y, portales.get(i).portalWidth+width/10, portales.get(i).portalHeight+width/10);
    }

    for (int i = 0; i < tornados.size(); i++) {   
      fill(gray);
      ellipse(tornados.get(i).x, tornados.get(i).y, tornados.get(i).tornadoWidth, tornados.get(i).tornadoHeight);
      fill(gray, 60);
      ellipse(tornados.get(i).x, tornados.get(i).y, tornados.get(i).tornadoWidth+width/10, tornados.get(i).tornadoHeight+width/10);
    }



    for (int i = 0; i < bounces.size(); i++) {
      if ( bounces.get(i).r < width/2) {
        bounces.get(i).run();
      }
    }


    noStroke();

    fill(blue);
    rect(0, 0, width, height/10);   //tob bar
    fill(green);
    rect(0, height/10, width, height*0.02f);
    fill(green, 120);
    rect(0, height*0.12f, width, height*0.02f);

    fill(red);   //buttom bar
    rect(0, height*0.98f, width, height*0.02f);
    fill(red, 60);
    rect(0, height*0.96f, width, height*0.02f);

    if (darkmode) {
      fill(0);
    } else {
      fill(255);
    }
    textSize(width/10);
    textAlign(CENTER);
    text(gm.lvl, width/2, height*0.07f);



    bll.run();
    //println("bll:" + bll.x + " " + bll.y);



    back_btn.run();
    restart_btn.run();
  }


  public void reset() {
    //background(255);

    bll = new ball(width*0.5f, height*0.9f, 0, 0, width/44,0);
    status = 0;
    t = 0;
  }
}

class menu {

  button darkmode_btn;
  button music_btn;
  button help_btn;

  int y, ys;
  int Mstatus;

  ArrayList<button> buttons = new ArrayList<button>();

  menu() {

    y = 0;

    darkmode_btn = new button(PApplet.parseInt(width*0.9f), PApplet.parseInt(height*0.05f), 4, PApplet.parseInt(width*0.03f), color(0), color(255), color(255));
    music_btn = new button(PApplet.parseInt(width*0.1f), PApplet.parseInt(height*0.05f), 31, PApplet.parseInt(width*0.03f), color(green), color(green), color(red));
    help_btn = new button(PApplet.parseInt(width*0.5f), PApplet.parseInt(height*0.05f), 2, PApplet.parseInt(width*0.03f), color(0), color(255), color(255));

    for ( int i = 0; i < gm.levels.size(); i++) {
      buttons.add(new button(i % 3 * width/4 + ( width/2 - width/4), PApplet.parseInt((i - i%3) / 3 * width/4 + height*0.2f), 0, width/13, color(green), color(green), color(red)));
      buttons.get(i).addTxt(str(i+1));
    }
  }

  public void run() {


    if (darkmode) {
      background(0);
    } else {
      background(255);
    }

    noStroke();

    
    
    


    for (int i = 0; i < buttons.size(); i++) {
      buttons.get(i).run();
      if (unlockedLvl < i) {
        buttons.get(i).locked = true;
      } else {
        buttons.get(i).locked = false;
      }
      if (buttons.get(i).pressed() && buttons.get(i).locked == false && Mstatus == 0) {
        gm.lvl = i+1;
        gm.reset();
        page = 1;
      }
    }

    if (darkmode_btn.pressed() && Mstatus == 0) {

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


    if (music_btn.pressed()) {

      if (music_btn.click == 0) {
        music_btn.click = 1;
        if (music) {
          music = false;
          music_btn.locked = true;
        } else {
          music = true;
          music_btn.locked = false;
        }
      }
    } else {
      music_btn.click = 0;
    }
    
    if (help_btn.pressed()) {
      page = 2;
    }
    
    fill(blue);
    rect(0, 0, width, height/10);   //tob bar


    music_btn.run();
    darkmode_btn.run();
    help_btn.run();



    for ( int i = 0; i < gm.levels.size(); i++) {
      buttons.get(i).y = PApplet.parseInt((i - i%3) / 3 * width/4 + height*0.2f + y);
    }
    
    
    int l = ((gm.levels.size()-1)-(gm.levels.size()-1)%3) /3;
    //println(ball l);

    if (mousePressed && mouseY > height*0.1f) {

      if (Mstatus == 0) {
        Mstatus = 1;
        ys = mouseY-y;
      } else {

        y = mouseY - ys;
        if(y > 0) y = 0;
        if(y < -1 * l * width/4 + height*0.8f - width/4) y = PApplet.parseInt(-1 * l * width/4 + height*0.8f - width/4);
        
      }
    } else {
      Mstatus = 0;
    }
  }
}
class portal{
  float x;
  float y;
  float portalWidth;
  float portalHeight;
  
  boolean locked = false;

  public portal(float x, float y, float portalWidth, float portalHeight) {
    this.x = x;
    this.y = y;
    this.portalWidth = portalWidth;
    this.portalHeight = portalHeight;
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
class tornado {
  float x;
  float y;
  float tornadoWidth;
  float tornadoHeight;

  public tornado(float x, float y, float tornadoWidth, float tornadoHeight) {
    this.x = x;
    this.y = y;
    this.tornadoWidth = tornadoWidth;
    this.tornadoHeight = tornadoHeight;
  }
}


