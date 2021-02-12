package org.headroyce.lross2024;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Represents the logic of our game
 */
public class GameLogic {

    //enum which contains all directional elements
    public enum DIRECTION {
        LEFT,
        UP,
        RIGHT,
        DOWN,
        STOP,
        NONE
    }

    // The game step in milliseconds
    public static final int GAME_STEP_TIMER = 17;
    private GameTimer gameTimer;

    private boolean gameOver;

    private Random rand;
    private double width40;

    // The player
    private Ball player;
    private HashMap<DIRECTION, Boolean> forcesOnPlayer;

    private SpikedWall enemy = null;

    private int TIME_ELAPSED = 0;

    private static final int PLAYER_FLASH_TIME = 500;
    private int flashTimer = 0;

    private static final int PLAYER_SCORING_TIME = 1000;
    private int PLAYER_SCORING_TIMER = 1000;

    private static final int ENEMY_SPAWN_TIME = 150;
    private static final int ENEMY_DIRECTION_PROBABILITY = 5;
    private static final int ENEMY_SPAWN_PROBABILITY = 8;
    private static final int OBSTACLE_SPAWN_PROBABILITY = 10;
    private int ENEMY_SPAWN_TIMER = 200;

    private static final int OBS_SPAWN_TIME = 150;
    private static final int OBS_SPAWN_PROBABILITY = 5;
    private int OBS_SPAWN_TIMER = 200;

    private static final int COIN_SPAWN_TIME = 600;
    private static final int COIN_SPAWN_PROBABILITY = 2;
    private int COIN_SPAWN_TIMER = 700;

    // Enemy Elements
    private ArrayList<Mob> enemies;

    // Width and height of the canvas
    private double width, height;

    /**
     * new logic object which makes player and enemies, resets objects and runs methods, starts new game timer, and randomizer.
     * @param width width of the canvas
     * @param height height of the canvas
     */
    public GameLogic(double width, double height){
        rand = new Random();

        gameTimer = new GameTimer();

        this.width = Math.abs(width);
        this.height = Math.abs(height);

        player = new Ball();
        enemies = new ArrayList<>();

        forcesOnPlayer = new HashMap<>();

        reset();
    }

    /**
     * Renders the game elements onto a canvas
     * @param canvas the canvas to render onto
     */
    public void render(Canvas canvas){

        // Update width and height
        width = canvas.getWidth();
        height = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        player.render(canvas);
        for( int i = 0; i < enemies.size(); i++ ){
            Mob enemy = enemies.get(i);

            int min = (int)enemy.getWidth();
            if( enemy.x < 0 ){
                int maxW = (int)(width-min+1);
                enemy.x = rand.nextInt(maxW-min+1)+min;
            }

            enemy.render(canvas);
        }

        // Draw lives and score last so that the balls go under them
        Text lives = new Text("Lives: " + Math.round(player.getHP()));

        gc.strokeText("Score: " + player.score, 10, 30);
        gc.strokeText(lives.getText(),width - 10 - lives.getLayoutBounds().getWidth(), 20);
    }

    /**
     * Pause or unpause the game
     * @param setPaused true to pause, false otherwise
     */
    public void pause(boolean setPaused ){
        if( setPaused ){
            gameTimer.stop();
        }
        else {
            gameTimer.start();
        }
    }

    /**
     * reset's player attributes, clears player forces and enemies array, sets gameover boolean false. runs at beginning
     * of program or when reset button used.
     */
    public void reset(){
        player.x = 200;
        player.y = 400;
        player.setRadius(10);

        player.velX = player.velY = 0;
        player.setVelocityBoundX(-7, 7);
        player.setVelocityBoundY(-7,7);


        player.addHP(3);
        enemies.clear();
        forcesOnPlayer.clear();

        enemy = null;

        gameOver = false;
        player.score = 0;
    }

    /**
     * if the player lives is <= 0, returns boolean true.
     * @return gameOver boolean if game ends.
     */
    public boolean isGameOver(){
        return gameOver;
    }

    private boolean collideWalls(Mob player){

        boolean collided = false;

        // Keep player with the window

        if( player == this.player ) {
            if (player.y + player.getHeight() > height) {
                player.y = height - player.getHeight();
                player.bounceY();
                collided = true;
            }

            if (player.y < 0) {
                player.y = 0;
                player.bounceY();
                collided = true;
            }
        }


        if( player.x + player.getWidth() > width ){
            player.x = width - player.getWidth();
            player.bounceX();
            collided = true;
        }
        if( player.x < 0 ){
            player.x = 0;
            player.bounceX();
            collided = true;
        }

        return collided;
    }

    /**
     * applies more force if button is held.
     * @param direction direction that the force is applied to.
     */
    public void applyForce( DIRECTION direction ) {
        forcesOnPlayer.put(direction, true);
    }

    /**
     * applies less force if button is released.
     * @param direction direction that the force is released from.
     */
    public void removeForce(DIRECTION direction){
        forcesOnPlayer.remove(direction);
    }

    /**
     * gets the total time elapsed (in seconds).
     * @return int seconds that the game has lasted for.
     */
    public int getTimeElapsed(){
        return TIME_ELAPSED;
    }

    /**
     * gets final score of player (run when gameOver is true).
     * @return player's score (int)
     */
    public int getFinalScore(){
        return player.score;
    }

    /**
     * not required, but a snarky death message depending on how much your final score was.
     * @return string message of the snarky comment.
     */
    public String deathMessage(){
        if (player.score < 0){
            return "Were you even trying???";
        }
        if (player.score > 0 && player.score <= 1000){
            return "Have you ever played a video game before?";
        }
        if (player.score > 1000 && player.score <= 5000){
            return "You call THAT an attempt? Wow.";
        }
        if (player.score > 5000 && player.score <= 10000){
            return "That was painful to watch.";
        }
        if (player.score > 10000 && player.score <= 15000){
            return "You're getting there... not for a while though.";
        }
        if (player.score > 15000 && player.score <= 20000){
            return "Ok that was decent. You're not horrible at this.";
        }
        if (player.score > 20000 && player.score <= 30000){
            return "That was a good run, but it was all luck.";
        }
        if (player.score > 30000 && player.score <= 50000){
            return "Dang that was pretty good. You'll probably fail next time though.";
        }
        if (player.score > 50000 && player.score <= 75000){
            return "Are you cheating? There's no way you did that well...";
        }
        if (player.score > 75000 && player.score <= 100000){
            return "You're godlike!!! Too bad that was a one-time thing.";
        }
        if (player.score < 100000){
            return "WOW YOU ARE AMAZING!!! YOUR SKILL GOES UNMATCHED!";
        }
        return "rtn";
    }

    /**
     * Runs once per game tick which is set dynamically by the GAME_STEP_TIMER
     */
    private class GameTimer extends AnimationTimer {
        // The last nanosecond
        private long lastUpdate;

        /**
         * resets last update timer to zero.
         */
        public GameTimer() {
            lastUpdate = 0;
        }

        @Override

        /**
         * runs every frame of game timer (17ms) and updates entities, checks for events, and runs event handlers.
         */
        public void handle(long now) {

            // Covert the time_elapsed from nanoseconds to milliseconds
            long time_elapsed = (now - lastUpdate)/1000000;


            flashTimer -= time_elapsed;
            if( flashTimer < 0 ){
                player.setColor(Color.BLACK);
            }

            PLAYER_SCORING_TIMER -= time_elapsed;
            if( PLAYER_SCORING_TIMER < 0 ){
                PLAYER_SCORING_TIMER = PLAYER_SCORING_TIME;
                player.addScore(10);
                TIME_ELAPSED++;
            }

            ENEMY_SPAWN_TIMER -= time_elapsed;
            if( ENEMY_SPAWN_TIMER < 0 ){
                int chance = rand.nextInt(100);
                if( chance < OBSTACLE_SPAWN_PROBABILITY ){

                    if( chance < ENEMY_SPAWN_PROBABILITY ) {
                        Ball enemy = new Ball();
                        enemy.setRadius(10);
                        enemy.hp = 1;

                        enemy.x = -1;
                        enemy.y = -enemy.getRadius();  // off screen
                        enemy.setVelocityBoundX(-5,5);
                        enemy.setVelocityBoundY(0,5);

                        enemy.setColor(Color.RED);

                        enemy.velX = rand.nextInt(5) + 2;
                        enemy.velY = rand.nextInt(5) + 2;
                        enemies.add(enemy);
                    }
                    else{
                        Lifesaver lifesaver = new Lifesaver();
                        lifesaver.setRadius(10);
                        lifesaver.hp = 1;

                        lifesaver.x = -1;
                        lifesaver.y = -lifesaver.getRadius();  // off screen
                        lifesaver.setVelocityBoundX(-5,5);
                        lifesaver.setVelocityBoundY(0,5);

                        lifesaver.velX = rand.nextInt(5) + 2;
                        lifesaver.velY = rand.nextInt(5) + 2;
                        enemies.add(lifesaver);
                    }

                }

                ENEMY_SPAWN_TIMER = ENEMY_SPAWN_TIME;
            }

            OBS_SPAWN_TIMER -= time_elapsed;
            if( OBS_SPAWN_TIMER < 0 ){
                int chance = rand.nextInt(120);
                if( chance < OBSTACLE_SPAWN_PROBABILITY ){

                    if( chance < OBS_SPAWN_PROBABILITY) {
                        Obstacle enemy = new Obstacle();
                        enemy.hp = 1;
                        enemy.x = rand.nextInt((int)width);
                        enemy.y = -enemy.getHeight();  // off screen
                        enemy.setVelocityBoundX(-5,5);
                        enemy.setVelocityBoundY(0,5);

                        enemy.velY = 5;
                        enemies.add(enemy);
                    } else {
                        if (enemy == null){
                            boolean adjWall = rand.nextBoolean();
                            int randWidth = (int)(Math.random() * ((width40-5)+5));
                            enemy = new SpikedWall(randWidth, 50);
                            if (adjWall){
                                enemy.x = width - enemy.getWidth();
                                //right
                            } else{
                                enemy.x = 0;
                                //left
                            }
                            enemy.hp = 100000;
                            enemy.y = -enemy.getHeight();  // off screen
                            enemy.setVelocityBoundX(-3,3);
                            enemy.setVelocityBoundY(0,5);

                            enemy.velY = 3;
                            enemy.velX = 0;
                            enemies.add(enemy);
                        }
                    }

                }

                OBS_SPAWN_TIMER = OBS_SPAWN_TIME;
            }

            COIN_SPAWN_TIMER -= time_elapsed;
            if( COIN_SPAWN_TIMER < 0 ){
                int chance = rand.nextInt(100);
                if( chance < COIN_SPAWN_PROBABILITY ){
                    Coin enemy = new Coin();
                    enemy.setRadius(25);
                    enemy.hp = 1;

                    enemy.x = -1;
                    enemy.y = -enemy.getRadius();  // off screen
                    enemy.setVelocityBoundX(-8,8);
                    enemy.setVelocityBoundY(0,8);

                    enemy.velX = rand.nextInt(6) + 2;
                    enemy.velY = rand.nextInt(6) + 2;
                    enemies.add(enemy);

                }

                COIN_SPAWN_TIMER = COIN_SPAWN_TIME;
            }

            if( time_elapsed > GameLogic.GAME_STEP_TIMER) {
                // Game steps go here

                if( forcesOnPlayer.containsKey(DIRECTION.LEFT) ){
                    player.velX--;
                }
                if( forcesOnPlayer.containsKey(DIRECTION.RIGHT) ){
                    player.velX++;
                }
                if( forcesOnPlayer.containsKey(DIRECTION.UP) ){
                    player.velY--;
                }
                if( forcesOnPlayer.containsKey(DIRECTION.DOWN) ){
                    player.velY++;
                }

                if( forcesOnPlayer.containsKey(DIRECTION.STOP) ){
                    player.velX -= Math.signum(player.velX);
                    player.velY -= Math.signum(player.velY);
                }

                // MOVE EVERYTHING
                player.move();
                for( int i = 0; i < enemies.size(); i++ ){
                    Mob enemy = enemies.get(i);

                    if(enemy instanceof Ball){
                        if( rand.nextInt(100) < ENEMY_DIRECTION_PROBABILITY) {
                            double changeX = Math.signum(player.x - enemy.x);
                            enemy.velX = changeX * Math.abs(enemy.velX);
                        }
                    }

                    enemy.move();
                }
                for( int i = 0; i < enemies.size(); i++ ){
                    Mob enemy = enemies.get(i);
                    enemy.move();
                }

                // CHECK WALLS ON EVERYTHING
                boolean playerCollided = collideWalls(player);
                if (playerCollided){
                    player.addHP(-1);
                }
                for( int i = 0; i < enemies.size(); i++ ){
                    Mob enemy = enemies.get(i);
                    collideWalls(enemy);

                    if( enemy.y > height ){
                        player.addScore(enemy.scored());
                        enemy.damage();
                    }
                }

                // Remove obstacles if they go past the end of the window
                for( int i = 0; i < enemies.size(); i++ ) {
                    Mob enemy = enemies.get(i);
                    if( enemy.y > height ){
                        enemy.damage();
                    }
                }

                // CHECK BALL COLLISIONS ON EVERYTHING
                    for( int i = 0; i < enemies.size(); i++ ) {
                        Mob enemy = enemies.get(i);
                        for( int j = i + 1; j < enemies.size(); j++ ) {
                            Mob enemy2 = enemies.get(j);
                            if(enemy.intersects(enemies.get(j))) {
                                if (enemy instanceof SpikedWall){
                                    enemy.bounceOff(enemy2);
                                } else if (enemy2 instanceof SpikedWall){
                                    enemy2.bounceOff(enemy);
                                } else {
                                    enemy.bounceOff(enemy2);
                                }
                                enemy.damage();
                                enemy2.damage();
                            }
                        }
                        boolean enemyRemove = enemy.intersects(player);
                        if( enemyRemove ){
                            enemy.damage();
                            enemy.bounceOff(player);
                            player.addHP(enemy.damage());
                        }
                        playerCollided =  enemyRemove || playerCollided;
                }

                if (enemy != null && enemy.y > height){
                    enemies.remove(enemy);
                    enemy = null;
                }

                if( playerCollided ){
                    // if player collides then lose life
                    player.addScore(-100);
                    if( player.getHP() <= 0 ) {
                        gameOver = true;
                        pause(true);
                    }
                    flashTimer = PLAYER_FLASH_TIME;
                    player.setColor(Color.GREEN);
                }


                width40 = (width/10)*4;

                for( int i = 0; i < enemies.size(); i++ ) {
                    Mob enemy = enemies.get(i);
                    if (enemy.hp <= 0){
                        enemies.remove(enemy);
                        i--;
                    }
                }

                lastUpdate = now;
            }
        }
    }
}
