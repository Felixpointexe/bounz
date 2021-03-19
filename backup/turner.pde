class turner {
  float x;
  float y;
  float turnerWidth;
  float angle;
  boolean locked = false;

  public turner(float x, float y, float turnerWidth, float angle) {
    this.x = x;
    this.y = y;
    this.turnerWidth = turnerWidth;
    this.angle = angle;
    locked = false;
  }
}
