/*
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
int unlockedLvl = 50;
String unlockedLvlFile;
float g = 0.1;


//colors
color blue = color(66, 170, 245);
color green = color(133, 255, 171);
color red = color(255, 115, 129);
color yellow = color(242, 247, 96);
color gray = color(138, 138, 138);
color purple = color(188, 66, 245);
color pink = color(245, 66, 218);
color orange = color(255, 170, 79);


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
  
  try{
    mp = new MediaPlayer();
    afd = context.getAssets().openFd("name.mp3");
    mp.setDataSource(afd.getFileDescriptor());
    mp.prepare();
  }
  catch(IOException e){
    println("file did not load");
  }
  mp.start;

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
