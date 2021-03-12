int x, y;

float a = 0;
float bx, by;
float bvx,bvy;
int br;

int selectable;

int lvl = 0;

ArrayList<Rectangle> Grectangles = new ArrayList<Rectangle>();
ArrayList<Rectangle> Rrectangles = new ArrayList<Rectangle>();




void setup() {

  size(400, 800);
  background(255);

  reset();


  Grectangles.add(new Rectangle(width/2, height/2, 80, 100));
  Grectangles.add(new Rectangle(0, 0,150, 30));
  
  Rrectangles.add(new Rectangle(width/2-80, height/7, 80, 100));
}

void draw() {
  //background(255);




  if (mousePressed && selectable < 2) {
    selectable = 1;
    background(255);
    stroke(0, 0, 0, 60);
    line(x, y, mouseX, mouseY);

    //println((atan2(y-mouseY, mouseX-x)+ TWO_PI ) % TWO_PI);
    a = (atan2(y-mouseY, mouseX-x)+ TWO_PI ) % TWO_PI;
  } else {
    if (selectable == 1) {
      selectable = 2;
      bvx = cos(a) * 6;
      bvy = -1*sin(a) * 6;
    }
    x = width/2;
    y = int(height*0.9);
  }

  fill(0, 255, 0, 60);
  noStroke();
  ellipse(bx, by, 2 * br, 2 * br);

  bx += bvx;
  by += bvy;

  if (bx > width - br || bx < br) {
    bvx *= -1;
  }
  if (by > height - br ) {
    bvy *= -1;
  }
  if (by < br ) {
    lvl++;
    reset();
  }


  for (int i = 0; i < Grectangles.size(); i++) {

    Rectangle rectangle = Grectangles.get(i);

    //check X movment bounce
    if (bx + 2*br + bvx > rectangle.x && 
      bx + bvx < rectangle.x + rectangle.rectWidth && 
      by + 2*br > rectangle.y && 
      by < rectangle.y + rectangle.rectHeight) {

      bvx *= -1;
      
    }

    //check Y movement bounce
    if (bx + 2*br > rectangle.x && 
      bx < rectangle.x + rectangle.rectWidth && 
      by + 2*br + bvy > rectangle.y && 
      by + bvy < rectangle.y + rectangle.rectHeight) {


      bvy *= -1;
    }

    fill(0, 255, 0, 60);
    noStroke();
    rect(rectangle.x, rectangle.y, rectangle.rectWidth, rectangle.rectHeight,30);
  }
  
  for (int i = 0; i < Rrectangles.size(); i++) {

    Rectangle rectangle = Rrectangles.get(i);

    //check X movment bounce
    if (bx + 2*br + bvx > rectangle.x && 
      bx + bvx < rectangle.x + rectangle.rectWidth && 
      by + 2*br > rectangle.y && 
      by < rectangle.y + rectangle.rectHeight) {

      reset();
      
    }

    //check Y movement bounce
    if (bx + 2*br > rectangle.x && 
      bx < rectangle.x + rectangle.rectWidth && 
      by + 2*br + bvy > rectangle.y && 
      by + bvy < rectangle.y + rectangle.rectHeight) {


      reset();
    }

    fill(255, 0, 0, 60);
    noStroke();
    rect(rectangle.x, rectangle.y, rectangle.rectWidth, rectangle.rectHeight,30);
  }

  fill(210, 52, 235 ,60);
  textSize(width/10);
  text(lvl, width*0.1, height*0.1);
}


void reset() {
  background(255);
  bx = width/2;
  by = int(height*0.9);
  bvx = 0;
  bvy = 0;
  
  br = width/40;
  selectable = 0;
}




class Rectangle {
  float x;
  float y;
  float rectWidth;
  float rectHeight;

  public Rectangle(float x, float y, float rectWidth, float rectHeight) {
    this.x = x;
    this.y = y;
    this.rectWidth = rectWidth;
    this.rectHeight = rectHeight;
  }
}
