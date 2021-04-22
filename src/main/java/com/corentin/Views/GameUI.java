package com.corentin.Views;

import com.corentin.Controllers.BattleshipController;
import com.corentin.Utils.NotifyType;
import com.corentin.Utils.Observable;
import com.corentin.Utils.Observer;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Vue de l'application lorsqu'elle est en jeu
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public final class GameUI implements JavaFXView, Observer {

    // Layout
    private final Pane mf_paneRoot = new Pane();
    private final HBox mf_HBox = new HBox();
    private final VBox mf_VBox = new VBox();

    /**
     * @brief Vue de la grille du joueur
     */
    private final GridUI mf_playerGridUI;

    /**
     * @brief Vue de la grille adversaire
     */
    private final AdversaryGridUI mf_computerGridUI;

    /**
     * @brief Instance du contrôleur de base
     */
    private final BattleshipController mf_controller;

    /**
     * @brief Image du cannon du joueur
     */
    private final Image mf_imgPlayerCanon;

    /**
     * @brief Visionneuse de l'image du cannon du joueur
     */
    private final ImageView mf_imgViewPlayerCanon;

    /**
     * @brief Instance du singleton
     */
    private static GameUI m_instance;

    private GameUI() {
        mf_controller = BattleshipController.getInstance();
        mf_controller.addObserver(this);

        mf_imgPlayerCanon = new Image(getClass().getResourceAsStream("/img/cannon.png"));
        mf_imgViewPlayerCanon = new ImageView();
        mf_imgViewPlayerCanon.setImage(mf_imgPlayerCanon);
        mf_imgViewPlayerCanon.setFitHeight(100);
        mf_imgViewPlayerCanon.setPreserveRatio(true);
        mf_imgViewPlayerCanon.setSmooth(true);

        mf_playerGridUI = new GridUI(mf_controller.getPlayerGrid(), "Votre grille");
        mf_computerGridUI = new AdversaryGridUI(mf_controller.getComputerGrid(), "Adversaire");

        mf_HBox.getChildren().addAll(mf_playerGridUI.getPane(), mf_computerGridUI.getPane());
        mf_HBox.setAlignment(Pos.CENTER);
        mf_HBox.setSpacing(10);

        mf_VBox.getChildren().addAll(mf_HBox, mf_imgViewPlayerCanon);
        mf_VBox.prefWidthProperty().bind(mf_paneRoot.widthProperty());
        mf_VBox.setAlignment(Pos.CENTER);
        mf_VBox.setSpacing(10);

        mf_paneRoot.getChildren().add(mf_VBox);
    }

    /**
     * @brief Retourne l'instance du singleton
     *
     * @return GameUI - L'instance du singleton
     */
    public static GameUI getInstance() {
        if (m_instance == null) {
            m_instance = new GameUI();
        }

        return m_instance;
    }

    /**
     * @brief On retourne le layout principal de la vue
     *
     * @return Pane - Le layout principal de la vue
     */
    public Pane getPane() {
        return mf_paneRoot;
    }

    /**
     * @brief Met à jour la vue de jeu
     *
     * - Si le jeu redémarre, met à jour les modèles des grilles enregistrés dans les vues correspondantes
     *
     * @param observableObject : Observable - Objet observable ayant appelé la méthode
     * @param notifyType : NotifyType - Type de notification pour avoir la réaction associée
     * @param args : Object - Paramètre additionnel
     */
    @Override
    public void update(Observable observableObject, NotifyType notifyType, Object args) {
        if (observableObject instanceof BattleshipController) {
            if (notifyType == NotifyType.RESTART) {
                mf_playerGridUI.setGridModel(mf_controller.getPlayerGrid());
                mf_computerGridUI.setGridModel(mf_controller.getComputerGrid());
            }
        }
    }
}
