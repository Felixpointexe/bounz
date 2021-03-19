class help {

  button back_btn;

  int y, ys;
  int Mstatus;

  help() {
    back_btn = new button(int(width*0.1), int(height*0.05), 3, int(width*0.03), color(0), color(255), color(0));
  }

  void run() {

    if (darkmode) {
      background(0);
    } else {
      background(255);
    }

    noStroke();



    if (mousePressed && mouseY > height*0.1) {

      if (Mstatus == 0) {
        Mstatus = 1;
        ys = mouseY-y;
      } else {

        y = mouseY - ys;
        if (y > 0) y = 0;
        if(y < -1* width*3.1 + height*0.8) y = int(-1* width*3.1+ height*0.8);
      }
    } else {
      Mstatus = 0;
    }
    
    if (darkmode) {
      fill(255);
    } else {
      fill(0);
    }
    textSize(width*0.05);
    textAlign(LEFT);

    text("The goal is it to shoot the green ball to the top of the sreen by tipping or draging the aming line. When released the ball will shoot automaticly.", width*0.1, height*0.15+y, width*0.8, height);
    
    fill(blue);
    rect(width*0.1,width*0.7+y,width*0.1,height*0.04,30);
    text("good boxes let the ball bounce", width*0.25, width*0.7+y, width*0.7, height);
    
    fill(red);
    rect(width*0.1,width*0.9+y,width*0.1,height*0.04,30);
    text("bad boxes let the ball reset", width*0.25, width*0.9+y, width*0.8, height);
    
    fill(yellow);
    ellipse(width*0.15,width*1.15+y,width*0.1,width*0.1);
    text("portals let the ball teleport", width*0.25, width*1.1+y, width*0.8, height);
    
    fill(purple);
    ellipse(width*0.15,width*1.35+y,width*0.1,width*0.1);
    text("tornados will get the ball move in", width*0.25, width*1.3+y, width*0.7, height);
    
    fill(pink);
    ellipse(width*0.15,width*1.55+y,width*0.1,width*0.1);
    text("hills will repel the ball", width*0.25, width*1.5+y, width*0.7, height);
    
    fill(orange);
    ellipse(width*0.15,width*1.75+y,width*0.1,width*0.1);
    text("turners will rotate the ball", width*0.25, width*1.7+y, width*0.7, height);
    
    if (darkmode) {
      stroke(255);
    } else {
      stroke(0);
    }
    strokeWeight(height/400);
    line(0,width*1.9+y,width,width*1.9+y);
    
    noStroke();
    button help_back_btn = new button(int(width*0.15),int( width*2.1+y), 3, int(width*0.03), color(255), color(0), color(0));
    help_back_btn.run();
    button help_info_btn = new button(int(width*0.15),int( width*2.3+y), 2, int(width*0.03), color(255), color(0), color(0));
    help_info_btn.run();
    button help_darkmode_btn = new button(int(width*0.15),int( width*2.5+y), 4, int(width*0.03), color(255), color(0), color(0));
    help_darkmode_btn.run();
    button help_music_btn = new button(int(width*0.15),int( width*2.7+y), 31, int(width*0.03), color(255), color(0), color(0));
    help_music_btn.run();
    button help_reset_btn = new button(int(width*0.15),int( width*2.9+y), 0, int(width*0.03), color(255), color(0), color(0));
    help_reset_btn.run();
    
    if (darkmode) {
      fill(255);
    } else {
      fill(0);
    }
    textSize(width*0.05);
    textAlign(LEFT);
     
    text("go back to menu", width*0.25, width*2.06+y, width*0.7, height);
    text("about the game (you are currently here)", width*0.25, width*2.26+y, width*0.7, height);
    text("toggle darkmode on/off", width*0.25, width*2.46+y, width*0.7, height);
    text("toggle music on/off", width*0.25, width*2.66+y, width*0.7, height);
    text("restarts the current level", width*0.25, width*2.86+y, width*0.7, height);
    
    if (darkmode) {
      stroke(255);
    } else {
      stroke(0);
    }
    strokeWeight(height/400);
    line(0,width*3.1+y,width,width*3.1+y);
    
    if (darkmode) {
      fill(255);
    } else {
      fill(0);
    }
    textSize(width*0.037);
    textAlign(CENTER);

    text("https://github.com/Felixpointexe/bounz/", width*0.1, width*3.2+y, width*0.8, height);
    
   

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
