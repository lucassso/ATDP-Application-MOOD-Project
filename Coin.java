package org.headroyce.lross2024;

import javafx.scene.paint.Color;

/**
 * represents coin inherited from ball.
 */
public class Coin extends Ball{
    /**
     * Creates a ball with a radius of one
     */
    public Coin() {
        this(1);
    }

    /**
     * Creates a ball with a custom radius (in pixels)
     *
     * @param radius the radius (in pixels) to set of the ball; Non-positives are reset to one
     */
    public Coin(double radius) {
        super(radius);
        setColor(Color.DARKGOLDENROD);
    }

    /**
     * Add to the current score of the ball. (0, since it is an optional mob)
     */
    public int scored(){
        return 0;
    }

    /**
     * player does NOT bounce off coin, rather just consumes it and gains 1000 pts (coin property).
     * @param other other mob that this mob bounces off of.
     */
    public void bounceOff (Mob other){
        other.addScore(1000);
    }

    /**
     * Damages Mob.
     * @return how much damage it does
     */
    public int damage() {
        this.hp -= 1;
        return 0;
    }
}
