package com.corentin.Views;

import com.corentin.Models.Boat;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Vue d'un bateau, destiné à être contenu dans la liste de l'éditeur de grille
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public final class BoatUI implements JavaFXView {

    // Layout
    private final Pane mf_rootPane = new Pane();
    private final VBox mf_VBox = new VBox();

    /**
     * @brief Rectangle qui va contenir l'image du bateau
     */
    private final Rectangle mf_rectangle;

    /**
     * @brief Texte qui va contenir le nom et la taille du bateau
     */
    private final Label mf_label;

    /**
     * @brief Modèle du bateau
     */
    private final Boat mf_boat;

    public BoatUI(Boat boat) {
        mf_boat = boat;

        mf_rootPane.getChildren().add(mf_VBox);
        mf_VBox.prefWidthProperty().bind(mf_rootPane.widthProperty());
        mf_VBox.setAlignment(Pos.CENTER);

        mf_rectangle = new Rectangle(100, 50, Color.WHITE);
        mf_rectangle.setFill(new ImagePattern(mf_boat.getHImage()));
        mf_label = new Label(mf_boat.getNom() + " - " + mf_boat.getTaille() + " cases");

        mf_VBox.getChildren().addAll(mf_rectangle, mf_label);
    }

    /**
     * @brief On retourne le layout principal de la vue
     *
     * @return Pane - Le layout principal de la vue
     */
    public Pane getPane() {
        return mf_rootPane;
    }

    /**
     * Retourne le modèle du bateau
     *
     * @return Boat - Le modèle du bateau
     */
    public Boat getBoat() {
        return mf_boat;
    }
}
