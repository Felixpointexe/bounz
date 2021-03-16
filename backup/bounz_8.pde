/*
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
import android.app.Activity;

Activity act;
Context cnt;
SharedPreferences sp;
SharedPreferences.Editor editor;

*/

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
color blue = color(66, 170, 245);
color green = color(133, 255, 171);
color red = color(255, 115, 129);
color yellow = color(242, 247, 96);
color gray = color(138, 138, 138);


void setup() {

  size(400, 800);
  background(255);
  //orientation(PORTRAIT);  


  gm = new game(5);
  mn = new menu();
  hlp = new help();
  
  /*
  act = this.getActivity();
  cnt = act.getApplicationContext();
  sp = PreferenceManager.getDefaultSharedPreferences(cnt);
  editor = sp.edit();

  unlockedLvlFile = "unlockedLvl";
  unlockedLvl = load(unlockedLvlFile);
  */
}

void draw() {

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

/*
void save(int value, String name) {
  //editor.clear();
  editor.putInt(name, value);
  editor.commit();
}

int load(String name) {
  return sp.getInt(name, 0);
}
*/
