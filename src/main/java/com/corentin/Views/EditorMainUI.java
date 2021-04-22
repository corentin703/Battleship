package com.corentin.Views;

import com.corentin.Controllers.BattleshipController;
import com.corentin.Controllers.PlayerGridEditorController;
import com.corentin.Models.Boat;
import com.corentin.Models.PlayerGrid;
import com.corentin.Utils.NotifyType;
import com.corentin.Utils.Observable;
import com.corentin.Utils.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vue de l'éditeur de grille
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public class EditorMainUI implements JavaFXView, Observer {

    // Layout
    private final Pane m_rootPane = new Pane();
    private final HBox m_HBox = new HBox();
    private final VBox m_VBox = new VBox();

    /**
     * @brief Liste des bateaux (sélectionnable)
     */
    private final ListView<Pane> m_listViewBoat = new ListView<Pane>();

    /**
     * @brief Grille éditable
     */
    private final EditableGridUI m_gridUI;

    /**
     * @brief Texte explicatif de comment positionner un bateau
     */
    private final Label m_lblInstructions;

    /**
     * @brief Bouton permettant d'initialiser la grille automatiquement
     */
    private final Button m_btnAutoInit;

    /**
     * @brief Map contenant les modèles des bateaux indexés par le layout les représentant
     */
    private final Map<Pane, Boat> m_mPaneBoat = new HashMap<Pane, Boat>();

    /**
     * @brief Instance du controller de base
     */
    private final BattleshipController m_controller = BattleshipController.getInstance();

    /**
     * @brief Instance du controller dédié à l'éditeur
     */
    private final PlayerGridEditorController m_editorController = PlayerGridEditorController.getInstance();

    /**
     * @brief Liste (structure de données) des layout des bateaux
     *
     * Utilisé dans la liste (graphique)
     */
    private final List<Pane> m_listBoatPane = new ArrayList<Pane>();

    public EditorMainUI() {
        m_gridUI = new EditableGridUI(m_controller.getPlayerGrid(), "Edition de votre grille");
        m_lblInstructions = new Label("Clic gauche pour positionner horizontalement et \n clic droit pour positionner verticalement");
        m_lblInstructions.setTextAlignment(TextAlignment.CENTER);
        m_lblInstructions.setMaxWidth(300);
        m_controller.addObserver(this);

        m_rootPane.getChildren().add(m_HBox);

        m_btnAutoInit = new Button("Éditer automatiquement");
        m_btnAutoInit.setOnAction(actionEvent -> m_editorController.autoInit());

        m_VBox.getChildren().addAll(m_listViewBoat, m_lblInstructions, m_btnAutoInit);
        m_VBox.setSpacing(50);
        m_VBox.setAlignment(Pos.CENTER);

        m_HBox.getChildren().addAll(m_VBox, m_gridUI.getPane());
        m_HBox.setSpacing(50);
        m_HBox.prefWidthProperty().bind(m_rootPane.widthProperty());
        m_HBox.setAlignment(Pos.CENTER);

        init();
    }

    /**
     * @brief Réalise les opérations nécessaires à l'initialisation de l'éditeur
     *
     * Initialise la liste des bateaux et le callback de sélection
     */
    private void init() {
        PlayerGrid grid = m_controller.getPlayerGrid();

        if (m_gridUI.getGridModel() != null) {
            m_gridUI.getGridModel().removeObserver(this);
        }

        grid.addObserver(this);

        m_gridUI.setGridModel(grid);

        // On supprime les données précédentes s'il y a lieu
        m_mPaneBoat.clear();
        m_listBoatPane.clear();

        // On crée les objets nécessaires au bon affichage
        for (Integer boatID : grid.getBateaux().keySet()) {
            BoatUI boatUI = new BoatUI(grid.getBateaux().get(boatID));
            m_mPaneBoat.put(boatUI.getPane(), boatUI.getBoat());
            m_listBoatPane.add(boatUI.getPane());
        }

        m_listViewBoat.setItems(FXCollections.observableList(m_listBoatPane));
        m_listViewBoat.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane oldValue, Pane newValue) {
                m_editorController.setBoatSelected(m_mPaneBoat.get(newValue));
            }
        });
    }

    /**
     * @brief On retourne le layout principal de la vue
     *
     * @return Pane - Le layout principal de la vue
     */
    public Pane getPane() {
        return m_rootPane;
    }

    /**
     * @brief Met à jour l'UI de l'éditeur
     *
     * - Si un bateau est positionné, le supprime de la liste
     * - S'il y a redémarrage, on réinitialise l'éditeur
     *
     * @param observableObject : Observable - Objet observable ayant appelé la méthode
     * @param notifyType : NotifyType - Type de notification pour avoir la réaction associée
     * @param args : Object - Paramètre additionnel
     */
    @Override
    public void update(Observable observableObject, NotifyType notifyType, Object args) {

        if (notifyType == NotifyType.BOAT_POSITIONED && args instanceof Boat) {
            Pane boatPaneToRemove = null;

            for (Pane boatPane : m_mPaneBoat.keySet()) {
                if (m_mPaneBoat.get(boatPane) == (Boat)args) {
                    boatPaneToRemove = boatPane;
                }
            }

            if (boatPaneToRemove != null) {
                m_listBoatPane.remove(boatPaneToRemove);
                m_mPaneBoat.remove(boatPaneToRemove);
            }

            m_listViewBoat.setItems(FXCollections.observableList(m_listBoatPane));

            m_editorController.setBoatSelected(null);

        } else if (notifyType == NotifyType.RESTART) {
            init();
        }
    }
}
