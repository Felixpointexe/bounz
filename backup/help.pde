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
        if(y < -1* width*2.5 + height*0.8) y = int(-1* width*2.5+ height*0.8);
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
    text("blue boxes are 'good' boxes which let the ball bounce. If the ball touches a red box, the level will reset.", width*0.1, width*0.7+y, width*0.8, height);
    
    stroke (blue);
    strokeWeight(width/120);
    line(0,width*1.1+y , width,width*1.1+y);
    
    text("Das Ziel ist es den grünen Ball an den oberen Bildschirmrand zu schießen, indem man durch tipen oder ziehen die Ziel-linie bewegt. Wenn man los lässt schießt der Ball automatisch", width*0.1, width*1.2+y, width*0.8, height);
    text("Blau Boxen sind 'gute' Boxen, welche den Ball abprallen lassen. Wenn der Ball eine rote Box berührt, wird das level zurück gesetzt.", width*0.1, width*1.7+y, width*0.8, height);
    
    line(0,width*2.3+y , width,width*2.3+y);
    
    textAlign(CENTER);

    text("made by Felix", width*0.1, width*2.4+y, width*0.8, height);
    textSize(width*0.035);
    text("https://github.com/Felixpointexe/bounz", width*0.1, width*2.5+y, width*0.8, height);


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
