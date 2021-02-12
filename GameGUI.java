package org.headroyce.lross2024;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;

import java.security.Key;

/**
 * Represents the view element of the game
 */
public class GameGUI extends StackPane {

    private GameLogic logic;

    private Canvas gameArea;
    private AnimationTimer animTimer;

    private Button reset;

    private Label message;
    private Label score;
    private Label time;
    private Label gameover;


    /**
     * constructor makes a new canvas to render entities, sets dimensions, makes animation timer, makes new object
     * from GameLogic.java, and adds you lose screen and canvas to window.
     */
    public GameGUI() {
        gameArea = new Canvas();
        gameArea.heightProperty().bind(this.heightProperty());
        gameArea.widthProperty().bind(this.widthProperty());

        animTimer = new AnimTimer();
        logic = new GameLogic(gameArea.getWidth(), gameArea.getHeight());

        this.getChildren().addAll(gameArea, gameoverScreen());
    }

    /**
     * Pause/Unpause the animation without touching the game timer
     * @param setAnimPause true to pause, false otherwise
     */
    public void pause(boolean setAnimPause) {
        if (setAnimPause) {
            animTimer.stop();
        } else {
            animTimer.start();
        }
    }

    /**
     * Pause/unpause teh animation and game timer
     * @param setAnimPause true to pause the animation timer
     * @param setGamePause true to pause the game timer
     */
    public void pause(boolean setAnimPause, boolean setGamePause ){
        this.pause(setAnimPause);
        logic.pause(setGamePause);
    }

    /**
     * Deal with key presses
     * @param event the event to handle
     */
    public void handleKeyPress(KeyEvent event){
        if( event.getCode() == KeyCode.A){
            logic.applyForce(GameLogic.DIRECTION.LEFT);
        }
        if( event.getCode() == KeyCode.D ) {
            logic.applyForce(GameLogic.DIRECTION.RIGHT);
        }
        if( event.getCode() == KeyCode.W ) {
            logic.applyForce(GameLogic.DIRECTION.UP);
        }
        if( event.getCode() == KeyCode.S ) {
            logic.applyForce(GameLogic.DIRECTION.DOWN);
        }

        if( event.getCode() == KeyCode.SPACE ){
            logic.applyForce(GameLogic.DIRECTION.STOP);
        }
    }

    /**
     * Deal with key releases
     * @param event the event to handle
     */
    public void handleKeyRelease(KeyEvent event){
        if( event.getCode() == KeyCode.A){
            logic.removeForce(GameLogic.DIRECTION.LEFT);
        }
        if( event.getCode() == KeyCode.D ) {
            logic.removeForce(GameLogic.DIRECTION.RIGHT);
        }
        if( event.getCode() == KeyCode.W ) {
            logic.removeForce(GameLogic.DIRECTION.UP);
        }
        if( event.getCode() == KeyCode.S ) {
            logic.removeForce(GameLogic.DIRECTION.DOWN);
        }

        if( event.getCode() == KeyCode.SPACE ){
            logic.removeForce(GameLogic.DIRECTION.STOP);
        }
    }

    /**
     * Updates final score, death message and time elapsed for game over screen.
     */
    public void updateGraphics() {
        GameGUI.this.score.setText("Final Score: " + logic.getFinalScore() + " points");
        GameGUI.this.time.setText("Time Elapsed: " + logic.getTimeElapsed() + " seconds");
        GameGUI.this.message.setText(logic.deathMessage());
    }
    /**
     * makes new VBox with gameover screen child nodes.
     * @return  game over text and button overlay
     */
    private VBox gameoverScreen() {
        VBox rtn = new VBox();

        gameover = new Label("GAME OVER");
        gameover.setPadding(new Insets(10, 10, 1, 10));
        Font big = Font.font("Verdana", FontWeight.BOLD, 28);
        gameover.setFont(big);
        Font italics = Font.font("Verdana", FontPosture.ITALIC, 14);
        message = new Label("unseen text");
        message.setPadding(new Insets(10, 10, 30, 10));
        message.setFont(italics);
        reset = new Button("Reset");
        reset.setPadding(new Insets(10, 10, 10, 10));
        reset.setOnAction(new ResetButton());
        score = new Label("Final Score: ");
        time = new Label("Time Elapsed: ");
        rtn.setAlignment(Pos.CENTER);

        rtn.getChildren().addAll(gameover, message, score, time, reset);

        rtn.setPadding(new Insets(20, 20, 20, 20));
        rtn.setMargin(score, new Insets(0, 20, 10, 20));
        rtn.setMargin(time, new Insets(0, 20, 10, 20));
        rtn.setMargin(reset, new Insets(0, 20, 20, 20));

        return rtn;
    }

    /**
     * Runs once per frame and handles all the drawing of each frame
     */
    private class AnimTimer extends AnimationTimer {

        @Override
        /**
         * runs once per animation frame and updates graphics/javafx nodes
         */
        public void handle(long now) {
            GraphicsContext gc = gameArea.getGraphicsContext2D();


            gc.clearRect(0,0, gameArea.getWidth(), gameArea.getHeight());

            if(logic.isGameOver()){
                updateGraphics();
                gameover.setVisible(true);
                reset.setVisible(true);
                message.setVisible(true);
                score.setVisible(true);
                time.setVisible(true);
                reset.toFront();
            }
            else {
                logic.render(gameArea);
                gameover.setVisible(false);
                reset.setVisible(false);
                score.setVisible(false);
                time.setVisible(false);
                message.setVisible(false);
            }

        }
    }

    /**
     * handles event, reset button pressed
     */

    private class ResetButton implements EventHandler<ActionEvent> {
        /**
         * runs if the reset button triggers event
         * @param e the event handler where the button press came from.
         */
        public void handle(ActionEvent e) {
            pause(false);
            logic.pause(false);
            logic.reset();
            reset.setVisible(false);
            message.setVisible(false);
            gameover.setVisible(false);
            time.setVisible(false);
            score.setVisible(false);
        }
    }



}
