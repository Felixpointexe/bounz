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
import android.media.MediaPlayer;
import android.content.res.AssetFileDescriptor;

Activity act;
Context cnt;
SharedPreferences sp;
SharedPreferences.Editor editor;
AssetFileDescriptor afd;
MediaPlayer mp;



//game
game gm;
menu mn;
help hlp;
end end;
demoLevel dmLvl;

//user settings
boolean darkmode = false;
boolean music =true;

//app data
int page = 0;   //0 = menu ; 1 = game
int unlockedLvl = 50;
String unlockedLvlFile;
float g = 0.1f;
int btnTimeout = 0;


//colors
int blue = color(66, 170, 245);
int green = color(133, 255, 171);
int red = color(255, 115, 129);
int yellow = color(242, 247, 96);
int gray = color(138, 138, 138);
int purple = color(188, 66, 245);
int pink = color(245, 66, 218);
int orange = color(255, 170, 79);


public void setup() {

  
  background(255);
  orientation(PORTRAIT);  


  gm = new game(5);
  mn = new menu();
  hlp = new help();
  end = new end();
  dmLvl = new demoLevel();
  
  
  act = this.getActivity();
  cnt = act.getApplicationContext();
  sp = PreferenceManager.getDefaultSharedPreferences(cnt);
  editor = sp.edit();
  
  try{
    bckGrnd_sound = new MediaPlayer();
    afd = cnt.getAssets().openFd("background.mp3");
    bckGrnd_sound.setDataSource(afd.getFileDescriptor());
    bckGrnd_sound.prepare();
  }
  catch(IOException e){
    println("file did not load");
  }
  bckGrnd_sound.loop();

  unlockedLvlFile = "unlockedLvl";
  unlockedLvl = load(unlockedLvlFile);
  
}

public void draw() {
  
  btnTimeout ++;

  if (page == 0) {
    mn.run();
  }

  if (page == 1) {
    gm.run();
  }
  
  if (page == 2) {
    hlp.run();
  }
  
  if (page == 3) {
    end.run();
  }
  
  if (page == 4) {
    dmLvl.run();
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
  boolean demo = false;

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
      gm.levels.get(gm.lvl-1).bounces.add(new bounce(x, y, green));
    }
    if (y > height*0.98f - r ) {   //if ball is at buttom screen
      if (demo) {
        dmLvl.lvl.reset();
      } else {
        gm.levels.get(gm.lvl-1).reset();
      }
    }
    if (y < r + height/10 ) {   //if buttom is at top screen
      if (demo) {
        dmLvl.lvl.reset();
      } else {
        //println(y);
        gm.reset();
        gm.lvl++;
        unlockedLvl ++;
        save(unlockedLvl,unlockedLvlFile);
        gm.reset();
      }
    }

    if ( demo) {
      testGrects(dmLvl.lvl.Grectangles);
      testRrects(dmLvl.lvl.Rrectangles);
      testPortals(dmLvl.lvl.portales);
      testTornado(dmLvl.lvl.tornados);
      testHills(dmLvl.lvl.hills);
      testTurners(dmLvl.lvl.turners);
      testEnemys(dmLvl.lvl.enemys);
    } else {
      testGrects(gm.levels.get(gm.lvl-1).Grectangles);
      testRrects(gm.levels.get(gm.lvl-1).Rrectangles);
      testPortals(gm.levels.get(gm.lvl-1).portales);
      testTornado(gm.levels.get(gm.lvl-1).tornados);
      testHills(gm.levels.get(gm.lvl-1).hills);
      testTurners(gm.levels.get(gm.lvl-1).turners);
      testEnemys(gm.levels.get(gm.lvl-1).enemys);
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


  public void testGrects(ArrayList<Rectangle> rects) {
    for (int i = 0; i < rects.size(); i++) {   //all "good" rectangles

      Rectangle rectangle = rects.get(i);

      //check X movment bounce
      if (x + 1*r + vx > rectangle.x && 
        x + vx < rectangle.x + rectangle.rectWidth && 
        y + 1*r > rectangle.y && 
        y < rectangle.y + rectangle.rectHeight) {

        println("ball #1");

        a = PI - a;
        gm.levels.get(gm.lvl-1).bounces.add(new bounce(x, y, green));
      }

      //check Y movement bounce
      if (x + 1*r > rectangle.x && 
        x < rectangle.x + rectangle.rectWidth && 
        y + 1*r + vy > rectangle.y && 
        y + vy < rectangle.y + rectangle.rectHeight) {

        println("ball #2");


        a = TWO_PI - a;
        gm.levels.get(gm.lvl-1).bounces.add(new bounce(x, y, green));
      }
    }
  }



  public void testRrects(ArrayList<Rectangle> rects) {
    for (int i = 0; i < rects.size(); i++) {   //all "bad" rectangles

      Rectangle rectangle = rects.get(i);

      //check X movment bounce
      if (x + 2*r + vx > rectangle.x && 
        x + vx < rectangle.x + rectangle.rectWidth && 
        y + 2*r > rectangle.y && 
        y < rectangle.y + rectangle.rectHeight) {
        if (demo) {
          dmLvl.lvl.reset();
        } else {
          gm.levels.get(gm.lvl-1).reset();
        }
      }

      //check Y movement bounce
      if (x + 2*r > rectangle.x && 
        x < rectangle.x + rectangle.rectWidth && 
        y + 2*r + vy > rectangle.y && 
        y + vy < rectangle.y + rectangle.rectHeight) {

        if (demo) {
          dmLvl.lvl.reset();
        } else {
          gm.levels.get(gm.lvl-1).reset();
        }
      }
    }
  }


  public void testPortals(ArrayList<portal> ports) {
    for (int i = 0; i < ports.size(); i++) {   

      portal portal = ports.get(i);

      noStroke();

      if (dist(portal.x, portal.y, x, y) < portal.portalWidth + r) {
        if (portal.locked == false) {
          if (ports.size() > 1) {
            int r = i;
            while (i == r) {
              r = PApplet.parseInt(random(0, ports.size()));
            }
            if (demo) {
              dmLvl.lvl.portales.get(r).locked = true;
            } else {
              gm.levels.get(gm.lvl-1).portales.get(r).locked = true;
            }
            x = ports.get(r).x;
            y = ports.get(r).y;
          }
        }
      } else {
        if (demo) {
          dmLvl.lvl.portales.get(i).locked = false;
        } else {
          gm.levels.get(gm.lvl-1).portales.get(i).locked = false;
        }
      }
    }
  }

  public void testTornado(ArrayList<tornado> tornados) {
    for (int i = 0; i < tornados.size(); i++) {   

      tornado tornado = tornados.get(i);

      float dirToMiddle = TWO_PI-(atan2(tornado.y-y, tornado.x-x) + TWO_PI ) % TWO_PI;
      float dirOfMove = a;

      noStroke();

      if (dist(tornado.x, tornado.y, x, y) < tornado.tornadoWidth/2 + r) {

        float n = g * ( ((tornado.tornadoWidth/2)-dist(tornado.x, tornado.y, x, y)) / (tornado.tornadoWidth/2));

        if ((dirToMiddle-dirOfMove+TWO_PI)%TWO_PI < (dirOfMove-dirToMiddle+TWO_PI)%TWO_PI) {
          a = ((dirToMiddle-dirOfMove+TWO_PI)%TWO_PI)*n+dirOfMove;
        } else {
          a = dirOfMove-((dirOfMove-dirToMiddle+TWO_PI)%TWO_PI)*n;
        }
      }
    }
  }

  public void testHills(ArrayList<tornado> hills) {
    for (int i = 0; i < hills.size(); i++) {   

      tornado hill = hills.get(i);

      float dirToMiddle = TWO_PI-(atan2(hill.y-y, hill.x-x) + TWO_PI ) % TWO_PI;
      float dirOfMove = a;

      noStroke();

      if (dist(hill.x, hill.y, x, y) < hill.tornadoWidth/2 + r) {

        float n = g * ( ((hill.tornadoWidth/2)-dist(hill.x, hill.y, x, y)) / (hill.tornadoWidth/2));

        if ((dirToMiddle-dirOfMove+TWO_PI)%TWO_PI > (dirOfMove-dirToMiddle+TWO_PI)%TWO_PI) {
          a = ((dirToMiddle-dirOfMove+TWO_PI)%TWO_PI)*n+dirOfMove;
        } else {
          a = dirOfMove-((dirOfMove-dirToMiddle+TWO_PI)%TWO_PI)*n;
        }
      }
    }
  }

  public void testTurners(ArrayList<turner> turners) {
    for (int i = 0; i < turners.size(); i++) {  

      turner turner = turners.get(i);

      noStroke();

      if (dist(turner.x, turner.y, x, y) < turner.turnerWidth + r) {
        if (turner.locked == false) {
          a += turner.angle;
          if (demo) {
            dmLvl.lvl.turners.get(i).locked = true;
          } else {
            gm.levels.get(gm.lvl-1).turners.get(i).locked = true;
          }
          
          x = turners.get(i).x;
          y = turners.get(i).y;
        }
      } else {
        if (demo) {
          dmLvl.lvl.turners.get(i).locked = false;
        } else {
          gm.levels.get(gm.lvl-1).turners.get(i).locked = false;
        }
      }
    }
  }


  public void testEnemys(ArrayList<enemy> enemys) {
    for (int i = 0; i < enemys.size(); i++) {   //all "bad" rectangles

      enemy enemy = enemys.get(i);

      //check X movment bounce
      if (x + 2*r + vx > enemy.x && 
        x + vx < enemy.x + height*0.04f && 
        y + 2*r > enemy.y && 
        y < enemy.y + height*0.04f) {
        if (demo) {
          dmLvl.lvl.reset();
        } else {
          gm.levels.get(gm.lvl-1).reset();
        }
      }

      //check Y movement bounce
      if (x + 2*r > enemy.x && 
        x < enemy.x + height*0.04f && 
        y + 2*r + vy > enemy.y && 
        y + vy < enemy.y + height*0.04f) {

        if (demo) {
          dmLvl.lvl.reset();
        } else {
          gm.levels.get(gm.lvl-1).reset();
        }
      }
    }
  }
}

class bounce{
  float x,y;
  public float r;
  int c;
  
  public bounce( float x, float y , int c){
    this.x = x;
    this.y = y;
    r = 0;
    this.c = c;
  }
  
  public void run(){
    fill(c,255-(r*255)/(width*0.5f));
    ellipse(x,y,r,r);
    r+= width/100;
    
  }
  
  
}
class button {
  int mode;
  int x, y;
  int r ;
  int click = 0;
  int dc, lc, loc;
  String txt = "";

  boolean locked;


  public button(int x, int y, int mode, int r, int dc, int lc, int loc) {
    this.x = x;
    this.y = y;
    this.mode = mode;
    this.r = r;
    this.dc = dc;
    this.lc = lc;
    this.loc = loc;
  }

  public void addTxt(String txt) {
    this.txt = txt;
  }

  public void run() {
    noStroke();
    int c;
    if (darkmode) {
      c = dc;
    } else {
      c = lc;
    }

    if (locked) {
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
      quad(x-r, y, x, y+r, x+r, y, x, y-r);
    }
    if (mode == 41) {
      fill(c, 60);
      ellipse(x, y, 3*r, 3*r);
      fill(c);
      quad(x - r*0.7f, y - r*0.7f, x - r*0.7f, y + r*0.7f, x + r*0.7f, y + r*0.7f, x + r*0.7f, y - r*0.7f);
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
    text(txt, x, y+r*0.3f);
  }

  public boolean pressed() {
    if (dist(x, y, mouseX, mouseY) <= 1.5f*r && mousePressed) {
      return true;
    }
    return false;
  }
}

class demoLevel{
  
  level lvl;
  float data[][] = new float[][]{{3,width*0.1f,height*0.3f,width*0.5f,width*0.5f},{1,0,height*0.6f,height*0.04f,height*0.4f},{4,width,height*0.6f,width*0.4f,width*0.4f},{0,width*0.6f,height*0.4f,width*0.4f,height*0.04f},{6,width*0.8f,height*0.3f,0,width/66},{2,width*0.9f,height*0.8f,height*0.04f,height*0.04f},{2,width*0.14f,height*0.9f,height*0.04f,height*0.04f},{5,width*0.1f,height*0.5f,height*0.04f,PI+HALF_PI}};
  
  demoLevel(){
    lvl = new level(data);
    lvl.bll.demo = true;
    lvl.demo = true;
  }
  
  public void run(){
    lvl.run();
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
class end {

  button back_btn;
  int t = 0;
  int y, ys;
  int Mstatus;

  end() {
    back_btn = new button(PApplet.parseInt(width*0.1f), PApplet.parseInt(height*0.05f), 3, PApplet.parseInt(width*0.03f), color(0), color(255), color(0));
  }

  public void run() {
    t++;
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
        if (y < -1* width*1.6f + height*0.8f) y = PApplet.parseInt(-1* width*1.6f+ height*0.8f);
      }
    } else {
      Mstatus = 0;
    }

    textSize(width*0.09f);
    textAlign(CENTER);

    fill(random(0, 255), random(0, 255), random(0, 255), 200);
    text("congratulation!", width*0.1f+sin(t*0.1f)*width*0.1f, width*0.4f+sin(t*0.07f)*width*0.05f+y, width*0.8f, height);

    textSize(width*0.05f);
    textAlign(CENTER);

    fill(blue);
    text("you passed all " + gm.levels.size() + " levels", width*0.1f, width*0.6f+y, width*0.8f, height);

    fill(blue);
    text("please let me know your impressions of the game", width*0.1f, width*0.9f+y, width*0.8f, height);
    text("Discord: Feljx#0260", width*0.1f, width*1.1f+y, width*0.8f, height);
    text("I would be happy about new level ideas", width*0.1f, width*1.4f+y, width*0.8f, height);
    text("-Felix-", width*0.1f, width*1.7f+y, width*0.8f, height);



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

class enemy {
  float x;
  float y;
  float sx;
  float sy;
  float vx;
  float vy;
  float svx;
  float svy;
  float r = width/44;

  boolean demo = false;

  public enemy(float sx, float sy, float svx, float svy) {
    this.x = sx;
    this.y = sy;
    this.sx = sx;
    this.sy = sy;
    this.vx = svx;
    this.vy = svy;
    this.svx = svx;
    this.svy = svy;
  }

  public void run() {
    //println("e_bll: " + vx + " " + vy);
    x += vx;
    y += vy;

    if (x > width - r || x < r) {   //if ball have to bounce on right or left wall
      vx *= -1;
      if (x < r) {
        x = r;
      } else {
        x = width-r;
      }

      //gm.levels.get(gm.lvl-1).bounces.add(new bounce(x, y, red));
    }
    if (y > height*0.98f - r || y < r + height*0.12f ) {   //if ball is at buttom screen
      vy *= -1;

      if (y < r + height*0.12f) {
        y = r + height*0.12f;
      } else {
        y = height*0.98f - r;
      }
      //gm.levels.get(gm.lvl-1).bounces.add(new bounce(x, y, red));
    }

    if (demo) {
      testGrects(dmLvl.lvl.Grectangles);
      testRrects(dmLvl.lvl.Rrectangles);
    } else {
      testGrects(gm.levels.get(gm.lvl-1).Grectangles);
      testRrects(gm.levels.get(gm.lvl-1).Rrectangles);
    }
  }


  public void testGrects(ArrayList<Rectangle> rects) {
    for (int i = 0; i < rects.size(); i++) {   //all "good" rectangles

      Rectangle rectangle = rects.get(i);

      //check X movment bounce
      if (x + 1*r + vx > rectangle.x && 
        x + vx < rectangle.x + rectangle.rectWidth && 
        y + 1*r > rectangle.y && 
        y < rectangle.y + rectangle.rectHeight) {

        println("ball #1");

        vx *= -1;
      }

      //check Y movement bounce
      if (x + 1*r > rectangle.x && 
        x < rectangle.x + rectangle.rectWidth && 
        y + 1*r + vy > rectangle.y && 
        y + vy < rectangle.y + rectangle.rectHeight) {

        println("ball #2");


        vy *= -1;
      }
    }
  }



  public void testRrects(ArrayList<Rectangle> rects) {
    for (int i = 0; i < rects.size(); i++) {   //all "bad" rectangles

      Rectangle rectangle = rects.get(i);

      //check X movment bounce
      if (x + 2*r + vx > rectangle.x && 
        x + vx < rectangle.x + rectangle.rectWidth && 
        y + 2*r > rectangle.y && 
        y < rectangle.y + rectangle.rectHeight) {

        vx *= -1;
      }

      //check Y movement bounce
      if (x + 2*r > rectangle.x && 
        x < rectangle.x + rectangle.rectWidth && 
        y + 2*r + vy > rectangle.y && 
        y + vy < rectangle.y + rectangle.rectHeight) {

        vy *= -1;
      }
    }
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
     *   0 = blue ; 1 = red ; 2 = portal ; 3 = tornado ; 4 = hill ; 5 = turner ; 6 = enemy
     *
     */
    levels.add(new level(new float[][]{{0,0,height*0.5f,width*0.6f,height*0.04f}}));
    levels.add(new level(new float[][]{{0,0,height*0.4f,width*0.6f,height*0.04f},{0,width*0.4f,height*0.6f,width*0.6f,height*0.04f}}));
    levels.add(new level(new float[][]{{0,0,height*0.3f,width*0.6f,height*0.04f},{0,width*0.4f,height*0.5f,width*0.6f,height*0.04f},{0,0,height*0.7f,width*0.6f,height*0.04f}}));
    levels.add(new level(new float[][]{{1,0,height*0.3f,width*0.6f,height*0.04f},{0,width*0.4f,height*0.6f,width*0.6f,height*0.04f}}));
    levels.add(new level(new float[][]{{2,width*0.2f,height*0.7f,height*0.04f,height*0.04f},{2,width*0.8f,height*0.4f,height*0.04f,height*0.04f},{1,0,height*0.5f,width,height*0.04f}}));
    levels.add(new level(new float[][]{{2,width*0.2f,height*0.7f,height*0.04f,height*0.04f},{2,width*0.8f,height*0.4f,height*0.04f,height*0.04f},{1,width*0.5f,height*0.25f,width*0.5f,height*0.04f},{1,0,height*0.5f,width*0.75f,height*0.04f}}));
    levels.add(new level(new float[][]{{1,0,height*0.4f,width*0.1f,height*0.04f},{1,width*0.4f,height*0.4f,width*0.6f,height*0.04f},{3,width*0.4f,height*0.42f,width*0.6f,width*0.6f}}));
    levels.add(new level(new float[][]{{0,0,height*0.58f,width*0.5f,height*0.04f},{4,width,height*0.6f,width*0.5f,width*0.5f},{1,width*0.5f,height*0.58f-width*0.35f,width*0.5f,height*0.04f},{4,0,height*0.6f-width*0.35f,width*0.5f,width*0.5f}}));
    levels.add(new level(new float[][]{{0,width*0.5f-height*0.02f,height*0.3f,height*0.04f,height*0.4f},{0,0,height*0.3f,width*0.5f+height*0.02f,height*0.04f},{0,0,height*0.66f,width*0.5f+height*0.02f,height*0.04f},{2,width*0.75f,height*0.45f,height*0.04f,height*0.04f},{2,width*0.25f,height*0.45f,height*0.04f,height*0.04f},{1,width*0.5f,height*0.66f,width*0.2f,height*0.04f}}));
    levels.add(new level(new float[][]{{0,width*0.2f,height*0.55f,height*0.04f,height*0.25f},{0,width*0.2f,height*0.2f,height*0.04f,height*0.25f},{3,width*0.2f+height*0.04f,height*0.35f,height*0.3f,height*0.3f},{1,width*0.2f+height*0.04f,height*0.55f,width*0.8f-height*0.04f,height*0.04f}}));
    levels.add(new level(new float[][]{{0,width*0.35f,height*0.3f,width*0.65f,height*0.04f},{4,width,height*0.5f,height*0.2f,height*0.2f},{4,height*0.1f,height*0.6f,height*0.2f,height*0.2f},{1,width-height*0.04f,height*0.7f,height*0.04f,height*0.3f}}));
    levels.add(new level(new float[][]{{1,0,height*0.4f,width,height*0.04f},{2,width*0.5f,height*0.49f,height*0.04f,height*0.04f},{2,width*0.5f,height*0.35f,height*0.04f,height*0.04f},{0,width*0.5f,height*0.8f,width*0.5f,height*0.04f},{0,0,height*0.67f,width*0.5f,height*0.04f},{0,width*0.5f,height*0.54f,width*0.5f,height*0.04f},{0,0,height*0.25f,width*0.5f,height*0.04f},{0,width*0.5f,height*0.12f,width*0.5f,height*0.04f}}));
    levels.add(new level(new float[][]{{3,width*0.7f,height*0.75f,width*0.5f,width*0.5f},{3,width*0.3f,height*0.55f,width*0.5f,width*0.5f},{3,width*0.7f,height*0.35f,width*0.5f,width*0.5f},{1,width*0.5f,height*0.55f,width*0.5f,height*0.04f},{1,0,height*0.73f,width*0.5f,height*0.04f},{0,0,height*0.33f,width*0.5f,height*0.04f},{0,width*0.5f,height*0.51f,width*0.5f,height*0.04f}}));
    levels.add(new level(new float[][]{{4,width*0.25f,height*0.6f,width*0.5f,width*0.5f},{4,width*0.75f,height*0.6f-width*0.5f,width*0.5f,width*0.5f},{3,width*0.75f,height*0.6f,width*0.5f,width*0.5f},{3,width*0.25f,height*0.6f-width*0.5f,width*0.5f,width*0.5f}}));
    levels.add(new level(new float[][]{{2,width*0.1f,height*0.5f,height*0.04f,height*0.04f},{2,width*0.9f,height*0.5f,height*0.04f,height*0.04f},{0,0,height*0.4f,width*0.9f,height*0.04f},{0,width*0.5f-height*0.04f,height*0.4f,height*0.04f,height*0.3f}}));
    levels.add(new level(new float[][]{{1,0,height*0.1f,height*0.04f,height*0.4f},{1,0,height*0.6f,height*0.04f,height*0.3f},{1,width-height*0.04f,height*0.4f,height*0.04f,height*0.3f},{0,width*0.5f,height*0.53f,width*0.5f,height*0.04f},{0,0,height*0.33f,width*0.5f,height*0.04f}}));
    levels.add(new level(new float[][]{{3,width*0.75f,height*0.5f,width*0.5f,width*0.5f},{2,width*0.9f,height*0.3f,width*0.1f,width*0.1f},{2,width*0.1f,height*0.3f,width*0.1f,width*0.1f},{1,width*0.5f,height*0.28f,width*0.3f,height*0.04f},{1,width*0.2f,height*0.32f,width*0.3f,height*0.04f},{1,width*0.8f,height*0.24f,width*0.2f,height*0.04f},{1,0,height*0.5f,height*0.04f,height*0.5f}}));
    levels.add(new level(new float[][]{{0,0,height*0.9f-width*0.143f*0.5f,width/7,width/7},{0,width/7,height*0.9f-width*0.143f*2.5f,width/7,width/7},{0,width/7*2,height*0.9f-width*0.143f*4.5f,width/7,width/7},{0,width/7*3,height*0.9f-width*0.143f*6.5f,width/7,width/7},{0,width/7*4,height*0.9f-width*0.143f*4.5f,width/7,width/7},{0,width/7*5,height*0.9f-width*0.143f*2.5f,width/7,width/7},{0,width-width/7,height*0.9f-width*0.143f*0.5f,width/7,width/7},{1,0,height*0.9f-width*0.143f*6.5f,width/7,width*0.143f*6},{1,width-width/7,height*0.9f-width*0.143f*6.5f,width/7,width*0.143f*6}}));
    levels.add(new level(new float[][]{{5,width*0.1f,height*0.5f,height*0.04f,PI*3/2},{5,width*0.5f,height*0.5f,height*0.04f,PI/2},{1,width*0.3f,height*0.3f,width*0.7f,height*0.04f},{1,width*0.3f,height*0.6f,height*0.04f,height*0.3f}}));
    levels.add(new level(new float[][]{{1,0,height*0.15f,width*0.35f,height*0.04f},{1,width*0.65f,height*0.15f,width*0.35f,height*0.04f},{1,0,height*0.23f,width*0.4f,height*0.04f},{1,width*0.6f,height*0.23f,width*0.4f,height*0.04f},{5,width*0.5f,height*0.33f,height*0.04f,HALF_PI},{1,0,height*0.23f,height*0.04f,height*0.12f}}));
    levels.add(new level(new float[][]{{6,width*0.25f,height*0.4f,width*0.015f,0},{6,width*0.5f,height*0.5f,width*0.015f,0},{6,width*0.75f,height*0.6f,width*0.015f,0},{6,width*0.75f,height*0.45f,width*-0.015f,0},{6,width*0.5f,height*0.55f,width*-0.015f,0},{6,width*0.25f,height*0.65f,width*-0.015f,0}}));
    levels.add(new level(new float[][]{{6,width-height*0.04f,height*0.5f,0,width*0.015f},{6,width-height*0.04f,height*0.1f,0,width*0.015f},{5,width*0.5f,height*0.4f,height*0.04f,7*PI/4},{1,0,height*0.38f,width*0.4f,height*0.04f},{1,width-height*0.12f,0,height*0.04f,height*0.35f},{1,width-height*0.12f,height*0.45f,height*0.04f,height*0.55f},{2,width*0.5f,height*0.6f,height*0.04f,height*0.04f},{2,width*0.1f,height*0.5f,height*0.04f,height*0.04f}}));
    levels.add(new level(new float[][]{{2,width*0.9f,height*0.9f,height*0.04f,height*0.04f},{2,width*0.1f,height*0.6f,height*0.04f,height*0.04f},{5,width*0.9f,height*0.6f,height*0.04f,PI+HALF_PI*0.7f},{0,width*0.1f-height*0.02f,height*0.88f,height*0.04f,height*0.04f},{5,width*0.5f,height*0.75f,height*0.04f,PI+HALF_PI*0.7f},{1,width*0.4f-height*0.04f,0,height*0.04f,height*0.5f},{1,width*0.6f,0,height*0.04f,height*0.5f},{0,0,height*0.28f,width*0.4f,height*0.04f},{0,width*0.6f,height*0.28f,width*0.4f,height*0.04f}}));
    levels.add(new level(new float[][]{{0,width*0.3f,height*0.7f,width*0.4f,height*0.04f},{6,width*0.5f,height*0.5f,0,width*0.015f},{1,width*0.6f,height*0.5f,width*0.4f,height*0.04f},{1,0,height*0.54f,width*0.4f,height*0.04f},{1,0,height*0.2f,width*0.4f,height*0.04f},{2,width*0.1f,height*0.9f,height*0.04f,height*0.04f},{2,width*0.62f,height*0.4f,height*0.04f,height*0.04f}}));
    levels.add(new level(new float[][]{{2,width*0.2f,height*0.6f,height*0.04f,height*0.04f},{2,width*0.5f,height*0.5f,height*0.04f,height*0.04f},{2,width*0.8f,height*0.6f,height*0.04f,height*0.04f},{1,width*0.4f-height*0.04f,0,height*0.04f,height*0.4f},{1,width*0.6f,0,height*0.04f,height*0.4f},{0,0,height*0.3f,width*0.4f,height*0.04f},{1,width*0.6f,height*0.3f,width*0.4f,height*0.04f},{0,width*0.6f,height*0.88f,width*0.4f,height*0.04f}}));
    levels.add(new level(new float[][]{{5,width*0.1f,height*0.8f,height*0.04f,PI+HALF_PI*0.3f},{3,width*0.6f,height*0.5f,width*0.5f,width*0.5f},{1,width*0.2f,height*0.3f,width*0.8f,height*0.04f},{1,width*0.3f,height*0.7f,height*0.04f,height*0.2f}}));
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
      //println(levels.get(lvl-1).t);
    } else {
      lvl = 1;
      mn.y = 0;
      page = 3;
    }
  }
}
class help {

  button back_btn;
  button demoLvl_btn;

  int y, ys;
  int Mstatus;

  help() {
    back_btn = new button(PApplet.parseInt(width*0.1f), PApplet.parseInt(height*0.05f), 3, PApplet.parseInt(width*0.03f), color(0), color(255), color(0));
    demoLvl_btn = new button(PApplet.parseInt(width*0.9f), PApplet.parseInt(height*0.05f), 41, PApplet.parseInt(width*0.03f), color(0), color(255), color(0));
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
        if(y < -1* width*3.3f + height*0.8f) y = PApplet.parseInt(-1* width*3.3f+ height*0.8f);
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
    
    fill(blue);
    rect(width*0.1f,width*0.7f+y,width*0.1f,height*0.04f,30);
    text("good boxes let the ball bounce", width*0.25f, width*0.7f+y, width*0.7f, height);
    
    fill(red);
    rect(width*0.1f,width*0.9f+y,width*0.1f,height*0.04f,30);
    text("bad boxes let the ball reset", width*0.25f, width*0.9f+y, width*0.8f, height);
    
    fill(yellow);
    ellipse(width*0.15f,width*1.15f+y,width*0.1f,width*0.1f);
    text("portals let the ball teleport", width*0.25f, width*1.1f+y, width*0.8f, height);
    
    fill(purple);
    ellipse(width*0.15f,width*1.35f+y,width*0.1f,width*0.1f);
    text("tornados will get the ball move in", width*0.25f, width*1.3f+y, width*0.7f, height);
    
    fill(pink);
    ellipse(width*0.15f,width*1.55f+y,width*0.1f,width*0.1f);
    text("hills will repel the ball", width*0.25f, width*1.5f+y, width*0.7f, height);
    
    fill(orange);
    ellipse(width*0.15f,width*1.75f+y,width*0.1f,width*0.1f);
    text("turners will rotate the ball", width*0.25f, width*1.7f+y, width*0.7f, height);
    
    if (darkmode) {
      stroke(255);
    } else {
      stroke(0);
    }
    strokeWeight(height/400);
    line(0,width*1.9f+y,width,width*1.9f+y);
    
    noStroke();
    button help_back_btn = new button(PApplet.parseInt(width*0.15f),PApplet.parseInt( width*2.1f+y), 3, PApplet.parseInt(width*0.03f), color(255), color(0), color(0));
    help_back_btn.run();
    button help_info_btn = new button(PApplet.parseInt(width*0.15f),PApplet.parseInt( width*2.3f+y), 2, PApplet.parseInt(width*0.03f), color(255), color(0), color(0));
    help_info_btn.run();
    button help_darkmode_btn = new button(PApplet.parseInt(width*0.15f),PApplet.parseInt( width*2.5f+y), 4, PApplet.parseInt(width*0.03f), color(255), color(0), color(0));
    help_darkmode_btn.run();
    button help_music_btn = new button(PApplet.parseInt(width*0.15f),PApplet.parseInt( width*2.7f+y), 31, PApplet.parseInt(width*0.03f), color(255), color(0), color(0));
    help_music_btn.run();
    button help_reset_btn = new button(PApplet.parseInt(width*0.15f),PApplet.parseInt( width*2.9f+y), 0, PApplet.parseInt(width*0.03f), color(255), color(0), color(0));
    help_reset_btn.run();
    button help_demo_btn = new button(PApplet.parseInt(width*0.15f),PApplet.parseInt( width*3.1f+y), 41, PApplet.parseInt(width*0.03f), color(255), color(0), color(0));
    help_demo_btn.run();
    
    if (darkmode) {
      fill(255);
    } else {
      fill(0);
    }
    textSize(width*0.05f);
    textAlign(LEFT);
     
    text("go back to menu", width*0.25f, width*2.06f+y, width*0.7f, height);
    text("about the game (you are currently here)", width*0.25f, width*2.26f+y, width*0.7f, height);
    text("toggle darkmode on/off", width*0.25f, width*2.46f+y, width*0.7f, height);
    text("toggle music on/off", width*0.25f, width*2.66f+y, width*0.7f, height);
    text("restarts the current level", width*0.25f, width*2.86f+y, width*0.7f, height);
    text("runs a demo level", width*0.25f, width*3.06f+y, width*0.7f, height);
    
    if (darkmode) {
      stroke(255);
    } else {
      stroke(0);
    }
    strokeWeight(height/400);
    line(0,width*3.3f+y,width,width*3.3f+y);
    
    if (darkmode) {
      fill(255);
    } else {
      fill(0);
    }
    textSize(width*0.037f);
    textAlign(CENTER);

    text("https://github.com/Felixpointexe/bounz/", width*0.1f, width*3.4f+y, width*0.8f, height);
    
   

    fill(blue);
    noStroke();
    rect(0, 0, width, height/10);   //tob bar

    back_btn.run();

    if (back_btn.pressed() && btnTimeout > 20) {
      mn.music_btn.click = 1;
      mn.y = 0;
      page = 0;
    }
    
    demoLvl_btn.run();

    if (demoLvl_btn.pressed()) {
      page = 4;
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

  boolean demo = false;



  int t = 0;

  int status = 0;   //status of game | 0 = waiting for player to give direction ; 1 = waiting for player to release mouse ; 2 = waiting for reset()

  ArrayList<Rectangle> Grectangles = new ArrayList<Rectangle>();   //list of all "good" rectangles
  ArrayList<Rectangle> Rrectangles = new ArrayList<Rectangle>();   //list of all "bad" rectangles
  ArrayList<portal> portales = new ArrayList<portal>();   //list of all portales
  ArrayList<tornado> tornados = new ArrayList<tornado>();   //list of all tornados
  ArrayList<tornado> hills = new ArrayList<tornado>();   //list of all tornados
  ArrayList<turner> turners = new ArrayList<turner>();   //list of all tornados
  ArrayList<enemy> enemys = new ArrayList<enemy>();   //list of all tornados
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
      if (data[i][0] == 4 ) {
        hills.add(new tornado(data[i][1], data[i][2], data[i][3], data[i][4]));
      }
      if (data[i][0] == 5 ) {
        turners.add(new turner(data[i][1], data[i][2], data[i][3], data[i][4]));
      }
      if (data[i][0] == 6 ) {
        enemys.add(new enemy(data[i][1], data[i][2], data[i][3], data[i][4]));
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
      if (demo) {
        btnTimeout = 0;
        page = 2;
      } else {
        mn.y = 0;
        page = 0;
        reset();
      }
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

        bll.v = width/66;
      }
    }





    noStroke();

    for (int i = 0; i < tornados.size(); i++) {  
      for (int j = 0; j < tornados.get(i).tornadoWidth + 10; j += tornados.get(i).tornadoWidth / 10) {
        fill(purple, 80);
        ellipse(tornados.get(i).x, tornados.get(i).y, j, j);
      }
      fill(purple, 60);
      ellipse(tornados.get(i).x, tornados.get(i).y, tornados.get(i).tornadoWidth+width/10, tornados.get(i).tornadoHeight+width/10);
    }

    for (int i = 0; i < hills.size(); i++) {  
      for (int j = 0; j < hills.get(i).tornadoWidth +10; j += hills.get(i).tornadoWidth / 10) {
        fill(pink, 80);
        ellipse(hills.get(i).x, hills.get(i).y, j, j);
      }
      fill(pink, 60);
      ellipse(hills.get(i).x, hills.get(i).y, hills.get(i).tornadoWidth+width/10, hills.get(i).tornadoHeight+width/10);
    }

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

    for (int i = 0; i < turners.size(); i++) {   
      fill(orange);
      ellipse(turners.get(i).x, turners.get(i).y, turners.get(i).turnerWidth, turners.get(i).turnerWidth);
      fill(orange, 60);
      ellipse(turners.get(i).x, turners.get(i).y, turners.get(i).turnerWidth+width/10, turners.get(i).turnerWidth+width/10);
      fill(201, 134, 62);
      arc(turners.get(i).x, turners.get(i).y, turners.get(i).turnerWidth*0.75f, turners.get(i).turnerWidth*0.75f, -HALF_PI, PI-(turners.get(i).angle-HALF_PI));
      fill(orange);
      ellipse(turners.get(i).x, turners.get(i).y, turners.get(i).turnerWidth*0.5f, turners.get(i).turnerWidth*0.5f);
    }

    for (int i = 0; i < enemys.size(); i++) {   

      if (demo) {
       enemys.get(i).demo = true;
      }


      fill(red);
      rect(enemys.get(i).x-height*0.02f, enemys.get(i).y-height*0.02f, height*0.04f, height*0.04f, 80);
      fill(red, 60);
      rect(enemys.get(i).x-height*0.02f-width/20, enemys.get(i).y-height*0.02f-width/20, height*0.04f+width/10, height*0.04f+width/10, 80);
      enemys.get(i).run();
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
    if (demo) {
      text("demo", width/2, height*0.07f);
    } else {
      text(gm.lvl, width/2, height*0.07f);
    }


    
    //println("bll:" + bll.x + " " + bll.y);



    back_btn.run();
    restart_btn.run();

    if (demo) {
      bll.demo = true;
    }
    
    bll.run();
  }


  public void reset() {
    //background(255);

    for (int i = 0; i < enemys.size(); i++) {   
      enemys.get(i).x = enemys.get(i).sx;
      enemys.get(i).y = enemys.get(i).sy;
      enemys.get(i).vx = enemys.get(i).svx;
      enemys.get(i).vy = enemys.get(i).svy;
    }

    bll = new ball(width*0.5f, height*0.9f, 0, 0, width/44, 0);
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
      //mp.start;
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

class turner {
  float x;
  float y;
  float turnerWidth;
  float angle;
  boolean locked = false;

  public turner(float x, float y, float turnerWidth, float angle) {
    this.x = x;
    this.y = y;
    this.turnerWidth = turnerWidth;
    this.angle = angle;
    locked = false;
  }
}

