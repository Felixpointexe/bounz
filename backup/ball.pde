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

  void run() {

    if (x > width - r || x < r) {   //if ball have to bounce on right or left wall
      a = PI - a;
      gm.levels.get(gm.lvl-1).bounces.add(new bounce(x, y));
    }
    if (y > height*0.98 - r ) {   //if ball is at buttom screen
      gm.levels.get(gm.lvl-1).reset();
    }
    if (y < r + height/10 ) {   //if buttom is at top screen
      //println(y);
      gm.reset();
      gm.lvl++;
      unlockedLvl ++;
      //save(unlockedLvl,unlockedLvlFile);
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
              r = int(random(0, gm.levels.get(gm.lvl-1).portales.size()));
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

      float toM = TWO_PI-(atan2(tornado.y-y, tornado.x-x) + TWO_PI ) % TWO_PI;
      float dir = a;

      println("bll.toM, bll.dir: " + toM + " " + dir);

      stroke(0);
      line(width/2, height/2, width/2 + cos(toM)*100, height/2 + sin(toM)*-100);
      line(width/2, height/2, width/2 + cos(dir)*100, height/2 + sin(dir)*-100);



    



      noStroke();

      println("bll.dir:" + a +" " +((toM-dir+TWO_PI)%TWO_PI) + " " +((dir-toM+TWO_PI)%TWO_PI));

      if (dist(tornado.x, tornado.y, x, y) < tornado.tornadoWidth/2 + r) {


      float n = 0.1 * ( ((tornado.tornadoWidth/2)-dist(tornado.x, tornado.y, x, y)) / (tornado.tornadoWidth/2));
  


      if ((toM-dir+TWO_PI)%TWO_PI < (dir-toM+TWO_PI)%TWO_PI) {
        dir = ((toM-dir+TWO_PI)%TWO_PI)*n+dir;
        stroke(0, 255, 0);
        line(width/2, height/2, width/2 + cos(dir)*100, height/2 + sin(dir)*-100);
      } else {
        dir = dir-((dir-toM+TWO_PI)%TWO_PI)*n;
         stroke(255, 0, 0);
         line(width/2, height/2, width/2 + cos(dir)*100, height/2 + sin(dir)*-100);
      }


      a = (dir+TWO_PI)%TWO_PI;
      //} else {
      }
    }



    //println(a);
    noStroke();

    fill(green, 120);
    ellipse(width/2, int(height*0.9), 4 * r, 4 * r);   //starting point

    fill(green);
    ellipse(x, y, 2 * r, 2 * r);   //ball


    vx = cos(a) * v;   //calculates ball x speed
    vy = -1*sin(a) * v;   //calculates ball y speed

    x += vx;   //calculates new ball x
    y += vy;   //calculates new ball y
  }
}
