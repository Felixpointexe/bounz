class menu {

  button darkmode_btn;
  button music_btn;
  button help_btn;

  int y, ys;
  int Mstatus;

  ArrayList<button> buttons = new ArrayList<button>();

  menu() {

    y = 0;

    darkmode_btn = new button(int(width*0.9), int(height*0.05), 4, int(width*0.03), color(0), color(255), color(255));
    music_btn = new button(int(width*0.1), int(height*0.05), 31, int(width*0.03), color(green), color(green), color(red));
    help_btn = new button(int(width*0.5), int(height*0.05), 2, int(width*0.03), color(0), color(255), color(255));

    for ( int i = 0; i < gm.levels.size(); i++) {
      buttons.add(new button(i % 3 * width/4 + ( width/2 - width/4), int((i - i%3) / 3 * width/4 + height*0.2), 0, width/13, color(green), color(green), color(red)));
      buttons.get(i).addTxt(str(i+1));
    }
  }

  void run() {


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
      buttons.get(i).y = int((i - i%3) / 3 * width/4 + height*0.2 + y);
    }
    
    
    int l = ((gm.levels.size()-1)-(gm.levels.size()-1)%3) /3;
    //println(ball l);

    if (mousePressed && mouseY > height*0.1) {

      if (Mstatus == 0) {
        Mstatus = 1;
        ys = mouseY-y;
      } else {

        y = mouseY - ys;
        if(y > 0) y = 0;
        if(y < -1 * l * width/4 + height*0.8 - width/4) y = int(-1 * l * width/4 + height*0.8 - width/4);
        
      }
    } else {
      Mstatus = 0;
    }
  }
}
