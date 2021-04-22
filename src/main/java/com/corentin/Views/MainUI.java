package com.corentin.Views;

import com.corentin.Controllers.BattleshipController;
import com.corentin.Utils.NotifyType;
import com.corentin.Utils.Observable;
import com.corentin.Utils.Observer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public final class MainUI implements JavaFXView, Observer {

    // Layout
    private final Pane mf_paneRoot = new Pane();
    private final VBox mf_VBox = new VBox();

    // Barre de menu
    /**
     * @brief Conteneur de la barre de menu
     */
    private final MenuBar mf_menuBar = new MenuBar();

    /**
     * @brief Menu principal (de la barre)
     */
    private final Menu mf_menu;

    /**
     * @brief Élément de menu : redémarrer
     */
    private final MenuItem mf_menuItemRestart;

    /**
     * @brief Élément de menu : quitter
     */
    private final MenuItem mf_menuItemQuit;

    /**
     * @brief Sous menu du mode triche
     */
    private final Menu mf_smenuCheat;

    /**
     * @brief Élément permettant la sélection exclusive d'une option (tricher ou ne pas tricher)
     */
    private final ToggleGroup mf_tgMenuCheat;

    /**
     * @brief Sélecteur de triche
     */
    private final RadioMenuItem mf_radMenuItemEnableCheat;

    /**
     * @brief Sélecteur de non-triche
     */
    private final RadioMenuItem mf_radMenuItemDisableCheat;

    /**
     * @brief Menu "À propos"
     */
    private final Menu mf_menuAbout;

    /**
     * @brief Élément de menu "À propos"
     */
    private final MenuItem mf_menuItemAbout;

    /**
     * @brief Vue de l'éditeur de grille
     */
    private final EditorMainUI mf_playerGridEditorUI = new EditorMainUI();

    /**
     * @brief Vue de la fin de partie
     */
    private final EndGameUI mf_endGameUI = new EndGameUI();

    /**
     * @brief Callback d'ouverture du menu "À propos"
     */
    private EventHandler<ActionEvent> mf_ehAboutMenu = new EventHandler<ActionEvent>() {
        private AboutUI mf_aboutUI;
        private Stage stage;

        @Override
        public void handle(ActionEvent actionEvent) {
            mf_aboutUI = new AboutUI();

            stage = new Stage();
            stage.setTitle("À propos...");
            stage.setScene(new Scene(mf_aboutUI.getPane(), 250, 100));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.setAlwaysOnTop(true);

            stage.showAndWait();
        }
    };

    /**
     * Instance du contrôleur de base
     */
    private final BattleshipController m_controller;

    /**
     * Instance de la vue du jeu de base
     */
    private final GameUI m_gameUI = GameUI.getInstance();

    public MainUI() {
        m_controller = BattleshipController.getInstance();
        m_controller.addObserver(this);

        mf_paneRoot.getChildren().add(mf_VBox);
        mf_VBox.setSpacing(10);

        // Initialisation des composants de la barre de menu
        mf_menu = new Menu("Menu");
        mf_menuItemRestart = new MenuItem("Recommencer");
        mf_menuItemRestart.setOnAction((actionEvent -> m_controller.restart()));
        mf_menuItemRestart.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        mf_menuItemQuit = new MenuItem("Quitter");
        mf_menuItemQuit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        mf_menuItemQuit.setOnAction((actionEvent -> m_controller.quit()));
        mf_smenuCheat = new Menu("Triche");

        // Menu À propos
        mf_menuAbout = new Menu("?");
        mf_menuItemAbout = new MenuItem("À propos...");
        mf_menuItemAbout.setOnAction(mf_ehAboutMenu);
        mf_menuAbout.getItems().add(mf_menuItemAbout);

        // Menu triche
        mf_radMenuItemEnableCheat = new RadioMenuItem("Activer la triche");
        mf_radMenuItemDisableCheat = new RadioMenuItem("Désactiver la triche");

        // Création du ToggleGroup et ajout des boutons à cocher
        mf_tgMenuCheat = new ToggleGroup();

        mf_radMenuItemEnableCheat.setToggleGroup(mf_tgMenuCheat);
        mf_radMenuItemEnableCheat.setOnAction((actionEvent -> m_controller.setCheatMode(true)));

        mf_radMenuItemDisableCheat.setToggleGroup(mf_tgMenuCheat);
        mf_radMenuItemDisableCheat.setOnAction((actionEvent -> m_controller.setCheatMode(false)));
        mf_radMenuItemDisableCheat.setSelected(true);

        mf_smenuCheat.getItems().addAll(mf_radMenuItemEnableCheat, mf_radMenuItemDisableCheat);


        // Ajouts des objets au menu
        mf_menuBar.getMenus().addAll(mf_menu, mf_menuAbout);
        mf_menu.getItems().addAll(mf_smenuCheat, mf_menuItemRestart, mf_menuItemQuit);

        // On force la barre à prendre toute la largeur de la fenêtre
        mf_menuBar.prefWidthProperty().bind(mf_paneRoot.widthProperty());

        mf_VBox.getChildren().add(mf_menuBar);

        m_controller.notifyObservers(NotifyType.START, null);
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
     * @brief Met à jour l'UI principale du jeu
     *
     * - Si le jeu démarre : on affiche l'diteur de grille si la grille n'est pas initialisée, ou l'interface de jeu
     * si elle l'est en notifiant une mise à jour de grille pour initialiser les vues des grilles
     * - Si le jeu redémarre, on supprime la vue de fin de partie si elle existe et on affiche la vue de l'éditeur
     * de grilles
     * - Si la partie est terminée, on affiche la vue de fin de partie en définissant la fin correspondante
     *
     * @param observableObject : Observable - Objet observable ayant appelé la méthode
     * @param notifyType : NotifyType - Type de notification pour avoir la réaction associée
     * @param args : Object - Paramètre additionnel
     */
    @Override
    public void update(Observable observableObject, NotifyType notifyType, Object args) {

        if (notifyType == NotifyType.START) {
            if (m_controller.getPlayerGrid().isSetUp()) {
                mf_VBox.getChildren().remove(mf_playerGridEditorUI.getPane());
                mf_VBox.getChildren().add(m_gameUI.getPane());

                m_controller.getPlayerGrid().notifyObservers(NotifyType.GRID_UPDATED, null);
                m_controller.getComputerGrid().notifyObservers(NotifyType.GRID_UPDATED, null);
            } else {
                mf_VBox.getChildren().add(mf_playerGridEditorUI.getPane());
            }
        } else if (notifyType == NotifyType.RESTART) {
            if (mf_VBox.getChildren().contains(mf_endGameUI.getPane())) {
                mf_VBox.getChildren().remove(mf_endGameUI.getPane());
            } else {
                mf_VBox.getChildren().remove(m_gameUI.getPane());
            }

            if (!mf_VBox.getChildren().contains(mf_playerGridEditorUI.getPane())) {
                mf_VBox.getChildren().add(mf_playerGridEditorUI.getPane());
            }
        } else if (notifyType == NotifyType.ENDGAME) {
            if (m_controller.isComputerWin()) {
                if (m_controller.hasCheated()) {
                    mf_endGameUI.setEndWay(EndGameUI.EndWay.CHEAT_LOOSE);
                } else {
                    mf_endGameUI.setEndWay(EndGameUI.EndWay.LOOSE);
                }
            } else if (m_controller.isPlayerWin()) {
                if (m_controller.hasCheated()) {
                    mf_endGameUI.setEndWay(EndGameUI.EndWay.CHEAT_WIN);
                } else {
                    mf_endGameUI.setEndWay(EndGameUI.EndWay.WIN);
                }
            }

            mf_VBox.getChildren().remove(m_gameUI.getPane());
            mf_VBox.getChildren().add(mf_endGameUI.getPane());
        }
    }
}
