import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
import android.app.Activity;

Activity act;
Context cnt;
SharedPreferences sp;
SharedPreferences.Editor editor;

int score;
String myScoreFile;

String baufile;

void setup () {
  orientation(PORTRAIT);
  textSize(75);
  textAlign (CENTER);
  act = this.getActivity();
  cnt = act.getApplicationContext();
  sp = PreferenceManager.getDefaultSharedPreferences(cnt);
  editor = sp.edit();

  myScoreFile = "Scores";
  score = loadScore(myScoreFile);

  baufile = "build";
  bau = loadScore(baufile);
}

void settings() {
  fullScreen();
}

void draw() {
  background(255); 

  fill(0);
  text(str(myScore), width/2, height/14);
}



int loadScore(String name) {
  int getScore = sp.getInt(name, 0);
  return getScore;
}

void mousePressed() {
  score ++;
  saveScore(score , myScoreFile);
}

void saveScore(int score, String name) {
  //editor.clear();
  editor.putInt(name, score);
  editor.commit();
}
