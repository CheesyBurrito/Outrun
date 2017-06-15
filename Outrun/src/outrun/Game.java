/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outrun;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    Line[] lines = new Line[1600];
    Canvas pane;
    GraphicsContext gc;
    double[] arrayX = new double[4];
    double[] arrayY = new double[4];
    
    Game(Canvas pane){
        this.pane = pane;
        this.gc = pane.getGraphicsContext2D();
        initialSetup();
    }
    
    public void initialSetup(){
        for(int i = 0; i < 1600; i++){
            Line line = new Line();
            lines[i] = line;
        }
    }
    
    public void drawQuad(GraphicsContext w, Color c, double x1, double y1, double w1, double x2, double y2, double w2){
        
        arrayX[0] = x1 - w1;
        arrayX[1] = x2 - w2;
        arrayX[2] = x2 + w2;
        arrayX[3] = x1 + w1;
        
        arrayY[0] = y1;
        arrayY[1] = y2;
        arrayY[2] = y2;
        arrayY[3] = y1;
        
        gc.setFill(c);
        
        gc.fillPolygon(arrayX, arrayY, 4);
       
        gc.stroke();
}
    
    
    @Override
    public void handle(long now) {
        
        
        for(int i = 0; i < 1600; i++){
            lines[i].z = i * segL;
            if (i > 300 && i < 700) lines[i].curve = 0.5;
            if (i > 1100) lines[i].curve =- 0.7;
            if (i > 750 ) lines[i].y = Math.sin(i / 30.0) * 1500;
        }
        
        int N = lines.length;
        
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
        
        int startPos = pos / segL;
        double camH = (lines[startPos].y + H);
        
        
        
        int maxy = height;
        double x = 0, dx = 0;
        
          for(int n = startPos; n < startPos + 300; n++){
      
            Line l = lines[(n % N)];
            l.project(playerX * roadW - x, camH, startPos * segL - (n >= N ? N * segL : 0));
            x += dx;
            dx += l.curve;

            //l.clip = maxy;
            
           if (l.Y>=maxy) continue;
            maxy = (int) l.Y;
            
            
            Color grass  = (n / 3) % 2 == 0 ? Color.rgb(16, 200, 16) : Color.rgb(0, 154, 0);
            Color rumble = (n / 3) % 2 == 0 ? Color.rgb(255, 255, 255) : Color.rgb(0, 0, 0);
            Color road   = (n / 3) % 2 == 0 ? Color.rgb(107, 107, 107) : Color.rgb(105, 105, 105);
    
            Line p = lines[((n - 1) % N)];
            
            
            drawQuad(gc, grass, 0, p.Y, width, 0, l.Y, width);
            drawQuad(gc, rumble, p.X, p.Y, p.W * 1.2, l.X, l.Y, l.W * 1.2);
            drawQuad(gc, road,  p.X, p.Y, p.W, l.X, l.Y, l.W);
   }
    }
    
}
