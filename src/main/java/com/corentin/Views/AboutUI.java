package com.corentin.Views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Vue du menu "À propos"
 *
 * @author Corentin VÉROT
 * @date 2021-04-22
 */
public final class AboutUI implements JavaFXView {

    // Layout
    private final GridPane mf_rootPane = new GridPane();
    private final VBox mf_VBox = new VBox();

    /**
     * @brief Numéro de version
     */
    private final Label mf_labVersion;

    /**
     * @brief Nom de l'auteur
     */
    private final Label mf_labAuthor;

    /**
     * @brief Date de création de l'application
     */
    private final Label mf_labDateCreated;

    /**
     * @brief Date de mise à jour de l'application
     */
    private final Label mf_labDateUpdated;


    public AboutUI() {
        mf_labVersion = new Label("Battleship Version 1.0");
        mf_labAuthor = new Label("Auteur : Corentin VÉROT");
        mf_labDateCreated = new Label("Jeu créé le : 2020-02-10");
        mf_labDateUpdated = new Label("Dernière mise à jour le : 2021-04-22");

        mf_VBox.getChildren().addAll(
            mf_labVersion,
            mf_labAuthor,
            mf_labDateCreated,
            mf_labDateUpdated
        );

        mf_VBox.setAlignment(Pos.CENTER);

        mf_rootPane.getChildren().add(mf_VBox);
        mf_rootPane.setAlignment(Pos.CENTER);
        mf_rootPane.setMinSize(250, 100);
    }

    /**
     * @brief On retourne le layout principal de la vue
     *
     * @return Pane - Le layout principal de la vue
     */
    public Pane getPane() {
        return mf_rootPane;
    }

}
