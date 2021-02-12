package org.headroyce.lross2024;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * represents all entities in the game, all variants of mobs inherit here.
 */
public class Mob {
    private Color color;

    public double hp;
    public int score;

    private double width, height;

    public double x, y;         // Center point of the circle
    public double velX, velY;

    // [0] - lower bound
    // [1] - upper bound
    private double[] boundX, boundY;

    /**
     * general constructor makes a new black object. (never used, just inherited)
     */
    public Mob(){
        setColor(Color.BLACK);
        boundX = new double[2];
        boundY = new double[2];
    }

    /**
     * Set the width of this object.  An obstacles's width must be positive.
     * @param w the new, positive, width of this object
     * @return true if the width is set, false if width is not changed
     */
    public boolean setWidth(double w){
        boolean rtn = false;

        if( w > 0 ) {
            this.width = w;
            rtn = true;
        }

        return rtn;
    }

    /**
     * Set the height of this object.  An obstacles's height must be positive.
     * @param h the new, positive, height of this object
     * @return true if the height is set, false if height is not changed
     */
    public boolean setHeight(double h){
        boolean rtn = false;

        if( h > 0 ) {
            this.height = h;
            rtn = true;
        }

        return rtn;
    }

    /**
     * Get the current width of this object
     * @return a positive width
     */
    public double getWidth() { return this.width; }

    /**
     * Get the current height of this object
     * @return a positive height
     */
    public double getHeight(){ return this.height;}

    /**
     * Changes the velocity bounds in the x direction
     * @param lower the lower limit
     * @param upper the upper limit
     * @return true if bounds have changed, false if lower > upper
     */
    public boolean setVelocityBoundX( double lower, double upper ) {
        if( lower > upper ) {
            return false;
        }
        boundX[0] = lower;
        boundX[1] = upper;
        return true;
    }


    /**
     * Changes the velocity bounds in the y direction
     * @param lower the lower limit
     * @param upper the upper limit
     * @return true if bounds have changed, false if lower > upper
     */
    public boolean setVelocityBoundY( double lower, double upper ) {
        if( lower > upper ) {
            return false;
        }
        boundY[0] = lower;
        boundY[1] = upper;
        return true;
    }


    /**
     * Get the bounds on the velocity in the X direction
     * @return a new array populated with the bounds of the ball in the x direction
     */
    public double[] getVelocityBoundX() {
        double[] rtn = new double[2];

        rtn[0] = boundX[0];
        rtn[1] = boundX[1];
        return rtn;
    }
    /**
     * Get the bounds on the velocity in the Y direction
     * @return a new array populated with the bounds of the ball in the y direction
     */
    public double[] getVelocityBoundY() {
        double[] rtn = new double[2];

        rtn[0] = boundY[0];
        rtn[1] = boundY[1];
        return rtn;
    }


    /**
     * Sets the color of the ball
     * @param c the new color of the ball (cannot be null)
     * @return true if the color has changed, false otherwise
     */
    public boolean setColor( Color c ){
        if( c == null ){
            return false;
        }

        color = c;
        return true;

    }

    /**
     * Get the current color of the ball
     * @return the current color of the ball
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * Get the current hit point value of the ball
     * @return a non-negative value representing the hit points of the ball
     */
    public double getHP(){ return hp; }

    /**
     * Add to the current hit points of the ball.  Hit points cannot go below zero.
     * @param deltaHP the value to add (or subtract) from the hitpoints of the ball
     */
    public void addHP( double deltaHP ){
        hp += deltaHP;
        if( hp < 0 ){
            hp = 0;
        }
    }

    /**
     * Add to the current score of the ball.
     * @param deltaScore how much to add to the score.
     */
    public void addScore(int deltaScore){
        score += deltaScore;
    }

    /**
     * Adds score to player based on what goes off screen
     * @return how much score the mob grants for going off screen.
     */
    public int scored(){
        return 100;
    }
    /**
     * Damages Mob.
     * @return how much damage it does
     */
    public int damage(){
        this.hp -= 1;
        return -1;
    }
    /**
     * Bounce the ball in the X direction
     */
    public void bounceX(){
        this.velX *= -1;
    }

    /**
     * Bounce the ball in the Y direction
     */
    public void bounceY() {
        this.velY *= -1;
    }

    /**
     * Move the ball along its trajectory vector
     */
    public void move(){
        double moveVelX = this.velX;
        double moveVelY = this.velY;

        double[] boundsX = getVelocityBoundX();
        double[] boundsY = getVelocityBoundY();

        // Clamp the x
        if( moveVelX < boundsX[0] ){
            moveVelX = boundsX[0];
        }
        else if( moveVelX > boundsX[1] ){
            moveVelX = boundsX[1];
        }

        // Clamp the y
        if( moveVelY < boundsY[0] ){
            moveVelY = boundsY[0];
        }
        else if( moveVelY > boundsY[1] ){
            moveVelY = boundsY[1];
        }

        this.x += moveVelX;
        this.y += moveVelY;
    }

    /**
     * Check to see if the obstacle overlaps with another obstacle
     * @param other the second Mob to check intersection with
     * @return true if this objects intersect, false otherwise
     */
    public boolean intersects(Mob other){
        if( this.x + this.getWidth() < other.x ){
            return false;
        }
        if( this.x > other.x + other.getWidth()){
            return false;
        }
        if( this.y + this.getHeight() < other.y ){
            return false;
        }
        if( this.y > other.y + other.getHeight()){
            return false;
        }

        return true;
    }

    /**
     * handles collision between two mobs, switches velocities.
     * @param other other mob that this mob bounces off of.
     */
    public void bounceOff (Mob other){
        double TX = this.velX;
        double TY = this.velY;

        this.velX = other.velX;
        this.velY = other.velY;

        other.velY = TY;
        other.velX = TX;
    }

    /**
     * Check to the see if a point in within the ball
     * @param point the 2D points to check
     * @return true if point is within this, false otherwise
     */
    public boolean contains(Point2D point){
        return false;
    }

    /**
     * unused, but renders mobs on canvas.
     * @param canvas where to render the mob.
     */
    public void render( Canvas canvas ){

    }

}
