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
    levels.add(new level(new float[][]{{0,0,height*0.5,width*0.6,height*0.04}}));
    levels.add(new level(new float[][]{{0,0,height*0.4,width*0.6,height*0.04},{0,width*0.4,height*0.6,width*0.6,height*0.04}}));
    levels.add(new level(new float[][]{{0,0,height*0.3,width*0.6,height*0.04},{0,width*0.4,height*0.5,width*0.6,height*0.04},{0,0,height*0.7,width*0.6,height*0.04}}));
    levels.add(new level(new float[][]{{1,0,height*0.3,width*0.6,height*0.04},{0,width*0.4,height*0.6,width*0.6,height*0.04}}));
    levels.add(new level(new float[][]{{2,width*0.2,height*0.7,height*0.04,height*0.04},{2,width*0.8,height*0.4,height*0.04,height*0.04},{1,0,height*0.5,width,height*0.04}}));
    levels.add(new level(new float[][]{{3,width*0.5,height*0.5,width/2,width/2}}));
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

  void run() {

    if (lvl <= levels.size()) {
       levels.get(lvl-1).run();
    }
  }

  void reset() {
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
