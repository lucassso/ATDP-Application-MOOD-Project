package org.headroyce.lross2024;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * spiked wall, moves adjacent to wall and doesn't die.
 */
public class SpikedWall extends Obstacle{


    /**
     * Creates an obstacle with a custom width and height (in pixels)
     * @param width the width (in pixels) to set of the obstacle; Non-positives are reset to ten
     * @param height the height (in pixels) to set of the obstacle; Non-positives are reset to ten
     */
    public SpikedWall( double width, double height ){
        super(width, height);
        setColor(Color.DODGERBLUE);
    }
    /**
     * since SW cannot die, you do not remove hp, just adds to sw.
     * @return how much damage it does to other mob.
     */
    public int damage(){
        this.hp += 1;
        return -1;
    }

    /**
     * makes other balls bounce off without killing it. if other is under SW, it is teleported out and gains speed.
     * @param other other mob that this mob bounces off of.
     */
    public void bounceOff (Mob other){
        if (other.velY <= this.velY && other.y - other.getHeight() > this.y) {
            other.y += 18;
            other.velY = this.velY * 2;
        } else {
            other.velY *= -1;
            other.velX *= -1;
        }
    }
}
