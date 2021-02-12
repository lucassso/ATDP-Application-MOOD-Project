package org.headroyce.lross2024;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * represents lifesaver inherited from ball
 */
public class Lifesaver extends Ball {

    /**
     * Creates a ball with a radius of one
     */
    public Lifesaver() {
        this(1);
    }

    /**
     * Creates a ball with a custom radius (in pixels)
     *
     * @param radius the radius (in pixels) to set of the ball; Non-positives are reset to one
     */
    public Lifesaver(double radius) {
        super(radius);
        setColor(Color.GREEN);
    }

    /**
     * Damages Mob.
     *
     * @return how much damage it does
     */
    public int damage() {
        this.hp -= 1;
        return 1;
    }
}

