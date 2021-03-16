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


    back_btn = new button(int(width*0.1), int(height*0.05), 3, int(width*0.03), color(0), color(255), color(0));
    restart_btn = new button(int(width*0.9), int(height*0.05), 0, int(width*0.03), color(0), color(255), color(0));
  }

  /*
     methode run()
   : one tic 
   */
  void run() {

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
          line(width/2 + i*(mouseX-width/2)/100, height*0.9 - i*(height*0.9-mouseY)/100, width/2 + (i-1)*(mouseX-width/2)/100, height*0.9 - (i-1)*(height*0.9-mouseY)/100);
        }
      }

      bll.a = (atan2(height*0.9-mouseY, mouseX-width/2)+ TWO_PI ) % TWO_PI;   //calculates the angle for the ball
    } else {

      if (status == 1) {   //if status is "waiting for player to release mouse"
        status = 2;   //sets status to "waiting for reset()"

        bll.v = width/66;
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
    rect(0, height/10, width, height*0.02);
    fill(green, 120);
    rect(0, height*0.12, width, height*0.02);

    fill(red);   //buttom bar
    rect(0, height*0.98, width, height*0.02);
    fill(red, 60);
    rect(0, height*0.96, width, height*0.02);

    if (darkmode) {
      fill(0);
    } else {
      fill(255);
    }
    textSize(width/10);
    textAlign(CENTER);
    text(gm.lvl, width/2, height*0.07);



    bll.run();
    //println("bll:" + bll.x + " " + bll.y);



    back_btn.run();
    restart_btn.run();
  }


  void reset() {
    //background(255);

    bll = new ball(width*0.5, height*0.9, 0, 0, width/44,0);
    status = 0;
    t = 0;
  }
}
