/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outrun;

/**
 *
 * @author willi
 */
class Line {
    public int width = 1024;
    public int height = 768;
    public int roadW = 2000;
    public int segL = 200; //segment length
    public double camD = 0.84; //camera depth
    
    public double x, y, z;
    public double X, Y, W;
    public double curve, scale;
    
    public Line(){
          curve = x = y = z = 0;
      }

    public void project(double camX, double camY, double camZ){
        scale = camD / (z - camZ);
        X = (1 + scale*(x - camX)) * width / 2;
        Y = (1 - scale*(y - camY)) * height / 2;
        W = scale * roadW  * width / 2;
  }
}
