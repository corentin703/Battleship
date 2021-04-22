package com.corentin;

import com.corentin.Controllers.BattleshipController;
import com.corentin.Views.MainUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale du jeu bataille naval
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public final class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Bataille Navale");

        MainUI batailleNavaleUI = new MainUI();

        Scene scene = new Scene(batailleNavaleUI.getPane());
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(760);
        primaryStage.setMinWidth(1100);

        // On veut quitter proprement toute l'application et non fermer uniquement la fenêtre courante
        primaryStage.setOnCloseRequest((windowEvent -> BattleshipController.getInstance().quit()));

        primaryStage.show();
    }

    /**
     * Lance le jeu Battleship
     *
     * @param args
     */
    public static void run(String[] args) {
        launch(args);
    }
}
