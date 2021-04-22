package com.corentin.Views;

import com.corentin.Controllers.BattleshipController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

import java.net.URISyntaxException;

/**
 * Vue de la fin de partie
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public class EndGameUI implements JavaFXView {

    // Layout
    private final Pane mf_rootPain = new Pane();
    private final VBox mf_VBox = new VBox();
    private final HBox mf_HBox = new HBox();

    /**
     * @brief Message de fin de partie
     */
    private final Label mf_lblMessage;

    /**
     * @brief Bouton de redémarrage
     */
    private final Button mf_btnRestart;

    /**
     * @brief Bouton permettant de quitter le jeu
     */
    private final Button mf_btnQuit;

    /**
     * @brief Instance du contrôleur de base
     */
    private final BattleshipController mf_controller;

    /**
     * Liste des fins de jeu possibles
     */
    public enum EndWay {
        WIN, // Le joueur gagne
        CHEAT_WIN, // Le joueur gagne en trichant
        LOOSE, // Le joueur perd
        CHEAT_LOOSE // Le joueur perd en trichant
    }

    public EndGameUI() {
        mf_controller = BattleshipController.getInstance();

        mf_lblMessage = new Label();

        mf_btnRestart = new Button("Recommencer");
        mf_btnRestart.setOnAction((actionEvent -> mf_controller.restart()));

        mf_btnQuit = new Button("Quitter");
        mf_btnQuit.setOnAction((actionEvent -> mf_controller.quit()));

        mf_HBox.setSpacing(10);
        mf_HBox.setAlignment(Pos.CENTER);
        mf_HBox.getChildren().addAll(
            mf_btnRestart,
            mf_btnQuit

        );

        mf_VBox.setSpacing(10);
        mf_VBox.setAlignment(Pos.CENTER);
        mf_VBox.getChildren().addAll(
                mf_lblMessage,
                mf_HBox
        );

        mf_VBox.prefWidthProperty().bind(mf_rootPain.widthProperty());
        mf_VBox.prefHeightProperty().bind(mf_rootPain.heightProperty());

        mf_rootPain.getChildren().add(mf_VBox);
    }

    /**
     * @brief Définit la fin de jeu
     *
     * Paramètre la vue en fonction de la fin de jeu fournie en paramètre
     *
     * @param endWay : EndWay - Fin de jeu
     */
    public void setEndWay(EndWay endWay) {
        final AudioClip audioClip;

        try {
            switch (endWay) {
                case WIN:
                    mf_lblMessage.setText("Félicitation vous avez gagné");

                    audioClip = new AudioClip(getClass().getResource("/song/win.mp3").toURI().toString());
                    audioClip.setCycleCount(1);
                    audioClip.play();

                    break;
                case CHEAT_WIN:
                    mf_lblMessage.setText("Vous avez gagné, c'est cool ! Mais sans triche ce serait encore mieux ! 😉");

                    audioClip = new AudioClip(getClass().getResource("/song/win_cheat.mp3").toURI().toString());
                    audioClip.setCycleCount(1);
                    audioClip.play();

                    break;
                case LOOSE:
                    mf_lblMessage.setText("Vous avez perdu");

                    audioClip = new AudioClip(getClass().getResource("/song/loose.mp3").toURI().toString());
                    audioClip.setCycleCount(1);
                    audioClip.play();

                    break;
                case CHEAT_LOOSE:
                    mf_lblMessage.setText("Vous avez perdu... avec la triche ! C'est incroyable ! 😂😂😂");

                    audioClip = new AudioClip(getClass().getResource("/song/loose_cheat.mp3").toURI().toString());
                    audioClip.setCycleCount(1);
                    audioClip.play();

                    break;
                default:
                    mf_lblMessage.setText("Fin de jeu non reconnue");
            }
        } catch (URISyntaxException uriSyntaxException) {
            System.out.println("Resource can't be loaded");
        }
    }

    /**
     * @brief On retourne le layout principal de la vue
     *
     * @return Pane - Le layout principal de la vue
     */
    public Pane getPane() {
        return mf_rootPain;
    }
}
