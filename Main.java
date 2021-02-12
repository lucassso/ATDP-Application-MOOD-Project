package org.headroyce.lross2024;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * main class runs all arguments
 */
public class Main extends Application {

    @Override
    /**
     * creates a new javafx window (500 by 500) named "Templer"
     */
    public void start(Stage primaryStage) throws Exception{

        GameGUI root = new GameGUI();
        primaryStage.setTitle("Templer");

        Scene scene = new Scene(root, 500,500);
        primaryStage.setScene(scene);

        // Forward key events to the Game
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                root.handleKeyPress(keyEvent);
            }
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            /**
             * handles interaction between keyboard and the program.
             */
            public void handle(KeyEvent keyEvent) {
                root.handleKeyRelease(keyEvent);
            }
        });
        primaryStage.show();

        root.pause(false,false);
    }


    /**
     * launches arguments
     * @param args all arguments from other classes
     */
    public static void main(String[] args) {
        launch(args);
    }
    private class KeyPressHandler implements EventHandler<KeyEvent> {
        /**
         * handles key being pressed
         * @param event event that a key was pressed
         */
        public void handle(KeyEvent event){
            System.err.println("KEY PRESS");
        }
    }

    private class KeyReleaseHandler implements EventHandler<KeyEvent> {
        public void handle(KeyEvent event) {
            /**
             * handles key being released
             * @param event event that a key was released
             */
            System.err.println("KEY RELEASE");
        }
    }
}
