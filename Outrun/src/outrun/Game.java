/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outrun;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

/**
 *
 * @author willi
 */
public class Game extends AnimationTimer{
    
    int width = 1024;
    int height = 768;
    
    Pane pane;
    
    Game(Pane pane){
        this.pane = pane;
    }
    
    public void drawQuad(Pane w, Color c, int x1,int y1,int w1,int x2,int y2,int w2){
    Polygon shape = new Polygon(4);
    
    shape.setFill(c);
    //shape.localToScreen(Point2D.ZERO)
    shape.localToScreen(new Point2D(x1-w1,y1));
    shape.localToScreen(new Point2D(x2-w2,y2));
    shape.localToScreen(new Point2D(x2+w2,y2));
    shape.localToScreen(new Point2D(x1+w1,y1));
    w.getChildren().add(shape);
}
    
    struct Line
{
  float x,y,z; //3d center of line
  float X,Y,W; //screen coord
  float curve,spriteX,clip,scale;
  Sprite sprite;

  Line()
  {spriteX=curve=x=y=z=0;}

  void project(int camX,int camY,int camZ)
  {
    scale = camD/(z-camZ);
    X = (1 + scale*(x - camX)) * width/2;
    Y = (1 - scale*(y - camY)) * height/2;
    W = scale * roadW  * width/2;
  }
    
    @Override
    public void handle(long now) {
        int maxy = height;
        float x=0,dx=0;
        int startPos = 0;
  ///////draw road////////
  for(int n = startPos; n < startPos + 300; n++){
      
    Line &l = lines[n%N];
    l.project(0, 15, 0);
    x+=dx;
    dx+=l.curve;

    l.clip=maxy;
    if (l.Y>=maxy) continue;
    maxy = l.Y;

    Color grass  = (n/3)%2?Color(16,200,16):Color(0,154,0);
    Color rumble = (n/3)%2?Color(255,255,255):Color(0,0,0);
    Color road   = (n/3)%2?Color(107,107,107):Color(105,105,105);

    Line p = lines[(n-1)%N]; //previous line

    drawQuad(app, grass, 0, p.Y, width, 0, l.Y, width);
    drawQuad(app, rumble,p.X, p.Y, p.W*1.2, l.X, l.Y, l.W*1.2);
    drawQuad(app, road,  p.X, p.Y, p.W, l.X, l.Y, l.W);
   }
    }
    
}
