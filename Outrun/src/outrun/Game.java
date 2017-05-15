/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outrun;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import static javafx.scene.input.KeyCode.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author willi
 */
public class Game extends AnimationTimer{
    
    int width = 1024;
    int height = 768;
    int roadW = 2000;
    int segL = 200; //segment length
    double camD = 0.84; //camera depth
    int speed = 0;
    double playerX = 0;
    ArrayList<Line> lines = new ArrayList<>();
    Pane pane;
    
    Game(Pane pane){
        this.pane = pane;
    }
    
    public void drawQuad(Pane w, Color c, double x1, double y1, double w1, double x2, double y2, double w2){
        Point2D x11 = new Point2D(x1 - w1, y1);
        Point2D y22 = new Point2D(x2 - w2, y2);
        Point2D x22 = new Point2D(x2 + w2, y2);
        Point2D y11 = new Point2D(x1 + w1, y1);
        
        //Polyline shape = new Polyline();
        Polygon shape = new Polygon();
        
        //shape.setStrokeWidth(0);
        
        shape.setStroke(c);
        
        shape.setFill(c);
        
        
        Double[] array = {
            x11.getX(), x11.getY(),
            y22.getX(), y22.getY(),   
            x22.getX(), x22.getY(),
            y11.getX(), y11.getY()
        };
        shape.getPoints().addAll(array);
        
        w.getChildren().add(shape);
}
    
    
    @Override
    public void handle(long now) {
        
        lines.clear();
        
        
        for(int i = 0; i < 1600; i++){
            Line line = new Line();
            line.z = i * segL;
            if (i > 300 && i < 700) line.curve = 0.5;
            if (i > 1100) line.curve =- 0.7;
            if (i > 750 ) line.y = Math.sin(i / 30.0) * 1500;
            
            lines.add(line);
        }
        
        int N = lines.size();
        
        int pos = 0;
        
        int H = 1500;
        
        
        
        pane.setOnKeyPressed(e ->{
            if(e.getCode() == RIGHT){
                playerX += 0.1;
            }
            
            if(e.getCode() == LEFT){
                playerX -= 0.1;
            }
            
            if(e.getCode() == UP){
                speed += 200;
            }
            
            if(e.getCode() == DOWN){
                speed += -200;
            }
        });
        
        
        pos += speed;
        while (pos >= N*segL) pos-=N*segL;
        while (pos < 0) pos += N*segL;
        
        pane.getChildren().clear();
        //pane.getChildren().add(new Rectangle(1024, 768, Color.rgb(105, 205, 4)));
        
        int startPos = pos / segL;
        double camH = (lines.get(startPos).y + H);
        
        
        
        int maxy = height;
        double x = 0, dx = 0;
        
        
        
        
          for(int n = startPos; n < startPos + 300; n++){
      
            Line l = lines.get(n % N);
            l.project(playerX * roadW - x, camH, startPos * segL - (n >= N ? N * segL : 0));
            x += dx;
            dx += l.curve;

            //l.clip = maxy;
            
           if (l.Y>=maxy) continue;
            maxy = (int) l.Y;
            
            
            Color grass  = (n / 3) % 2 == 0 ? Color.rgb(16, 200, 16) : Color.rgb(0, 154, 0);
            Color rumble = (n / 3) % 2 == 0 ? Color.rgb(255, 255, 255) : Color.rgb(0, 0, 0);
            Color road   = (n / 3) % 2 == 0 ? Color.rgb(107, 107, 107) : Color.rgb(105, 105, 105);
    
            Line p = lines.get((n - 1) % N);
            
            
            drawQuad(pane, grass, 0, p.Y, width, 0, l.Y, width);
            drawQuad(pane, rumble, p.X, p.Y, p.W * 1.2, l.X, l.Y, l.W * 1.2);
            drawQuad(pane, road,  p.X, p.Y, p.W, l.X, l.Y, l.W);
   }
    }
    
}
