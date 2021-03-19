class game {
  public int lvl;

  ArrayList<level> levels = new ArrayList<level>();

  game(int sLvl) {
    lvl = sLvl;

    //adds lvls
    /*
     *   height*0.04
     *   0 = blue ; 1 = red ; 2 = portal ; 3 = tornado ; 4 = hill ; 5 = turner
     *
     */
    levels.add(new level(new float[][]{{0,0,height*0.5,width*0.6,height*0.04}}));
    levels.add(new level(new float[][]{{0,0,height*0.4,width*0.6,height*0.04},{0,width*0.4,height*0.6,width*0.6,height*0.04}}));
    levels.add(new level(new float[][]{{0,0,height*0.3,width*0.6,height*0.04},{0,width*0.4,height*0.5,width*0.6,height*0.04},{0,0,height*0.7,width*0.6,height*0.04}}));
    levels.add(new level(new float[][]{{1,0,height*0.3,width*0.6,height*0.04},{0,width*0.4,height*0.6,width*0.6,height*0.04}}));
    levels.add(new level(new float[][]{{2,width*0.2,height*0.7,height*0.04,height*0.04},{2,width*0.8,height*0.4,height*0.04,height*0.04},{1,0,height*0.5,width,height*0.04}}));
    levels.add(new level(new float[][]{{2,width*0.2,height*0.7,height*0.04,height*0.04},{2,width*0.8,height*0.4,height*0.04,height*0.04},{1,width*0.5,height*0.25,width*0.5,height*0.04},{1,0,height*0.5,width*0.75,height*0.04}}));
    levels.add(new level(new float[][]{{1,0,height*0.4,width*0.1,height*0.04},{1,width*0.4,height*0.4,width*0.6,height*0.04},{3,width*0.4,height*0.42,width*0.6,width*0.6}}));
    levels.add(new level(new float[][]{{0,0,height*0.58,width*0.5,height*0.04},{4,width,height*0.6,width*0.5,width*0.5},{1,width*0.5,height*0.58-width*0.35,width*0.5,height*0.04},{4,0,height*0.6-width*0.35,width*0.5,width*0.5}}));
    levels.add(new level(new float[][]{{0,width*0.5-height*0.02,height*0.3,height*0.04,height*0.4},{0,0,height*0.3,width*0.5+height*0.02,height*0.04},{0,0,height*0.66,width*0.5+height*0.02,height*0.04},{2,width*0.75,height*0.45,height*0.04,height*0.04},{2,width*0.25,height*0.45,height*0.04,height*0.04},{1,width*0.5,height*0.66,width*0.2,height*0.04}}));
    levels.add(new level(new float[][]{{0,width*0.2,height*0.55,height*0.04,height*0.25},{0,width*0.2,height*0.2,height*0.04,height*0.25},{3,width*0.2+height*0.04,height*0.35,height*0.3,height*0.3},{1,width*0.2+height*0.04,height*0.55,width*0.8-height*0.04,height*0.04}}));
    levels.add(new level(new float[][]{{0,width*0.35,height*0.3,width*0.65,height*0.04},{4,width,height*0.5,height*0.2,height*0.2},{4,height*0.1,height*0.6,height*0.2,height*0.2},{1,width-height*0.04,height*0.7,height*0.04,height*0.3}}));
    levels.add(new level(new float[][]{{1,0,height*0.4,width,height*0.04},{2,width*0.5,height*0.49,height*0.04,height*0.04},{2,width*0.5,height*0.35,height*0.04,height*0.04},{0,width*0.5,height*0.8,width*0.5,height*0.04},{0,0,height*0.67,width*0.5,height*0.04},{0,width*0.5,height*0.54,width*0.5,height*0.04},{0,0,height*0.25,width*0.5,height*0.04},{0,width*0.5,height*0.12,width*0.5,height*0.04}}));
    levels.add(new level(new float[][]{{3,width*0.7,height*0.75,width*0.5,width*0.5},{3,width*0.3,height*0.55,width*0.5,width*0.5},{3,width*0.7,height*0.35,width*0.5,width*0.5},{1,width*0.5,height*0.55,width*0.5,height*0.04},{1,0,height*0.73,width*0.5,height*0.04},{0,0,height*0.33,width*0.5,height*0.04},{0,width*0.5,height*0.51,width*0.5,height*0.04}}));
    levels.add(new level(new float[][]{{4,width*0.25,height*0.6,width*0.5,width*0.5},{4,width*0.75,height*0.6-width*0.5,width*0.5,width*0.5},{3,width*0.75,height*0.6,width*0.5,width*0.5},{3,width*0.25,height*0.6-width*0.5,width*0.5,width*0.5}}));
    levels.add(new level(new float[][]{{2,width*0.1,height*0.5,height*0.04,height*0.04},{2,width*0.9,height*0.5,height*0.04,height*0.04},{0,0,height*0.4,width*0.9,height*0.04},{0,width*0.5-height*0.04,height*0.4,height*0.04,height*0.3}}));
    levels.add(new level(new float[][]{{1,0,height*0.1,height*0.04,height*0.4},{1,0,height*0.6,height*0.04,height*0.3},{1,width-height*0.04,height*0.4,height*0.04,height*0.3},{0,width*0.5,height*0.53,width*0.5,height*0.04},{0,0,height*0.33,width*0.5,height*0.04}}));
    levels.add(new level(new float[][]{{3,width*0.75,height*0.5,width*0.5,width*0.5},{2,width*0.9,height*0.3,width*0.1,width*0.1},{2,width*0.1,height*0.3,width*0.1,width*0.1},{1,width*0.5,height*0.28,width*0.3,height*0.04},{1,width*0.2,height*0.32,width*0.3,height*0.04},{1,width*0.8,height*0.24,width*0.2,height*0.04},{1,0,height*0.5,height*0.04,height*0.5}}));
    levels.add(new level(new float[][]{{0,0,height*0.9-width*0.143*0.5,width/7,width/7},{0,width/7,height*0.9-width*0.143*2.5,width/7,width/7},{0,width/7*2,height*0.9-width*0.143*4.5,width/7,width/7},{0,width/7*3,height*0.9-width*0.143*6.5,width/7,width/7},{0,width/7*4,height*0.9-width*0.143*4.5,width/7,width/7},{0,width/7*5,height*0.9-width*0.143*2.5,width/7,width/7},{0,width-width/7,height*0.9-width*0.143*0.5,width/7,width/7},{1,0,height*0.9-width*0.143*6.5,width/7,width*0.143*6},{1,width-width/7,height*0.9-width*0.143*6.5,width/7,width*0.143*6}}));
    levels.add(new level(new float[][]{{5,width*0.1,height*0.5,height*0.04,PI*3/2},{5,width*0.5,height*0.5,height*0.04,PI/2},{1,width*0.3,height*0.3,width*0.7,height*0.04},{1,width*0.3,height*0.6,height*0.04,height*0.3}}));
    levels.add(new level(new float[][]{{1,0,height*0.15,width*0.4,height*0.04},{1,width*0.6,height*0.15,width*0.4,height*0.04},{1,0,height*0.23,width*0.4,height*0.04},{1,width*0.6,height*0.23,width*0.4,height*0.04},{5,width*0.5,height*0.33,height*0.04,HALF_PI},{1,0,height*0.23,height*0.04,height*0.1}}));
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
