package com.corentin.Controllers;

import com.corentin.Models.Boat;
import com.corentin.Models.Position2D;
import com.corentin.Utils.NotifyType;

/**
 * Contrôleur destiné à la vue d'éditeur de grille
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public class PlayerGridEditorController {

    /**
     * Instance du singleton
     */
    private static PlayerGridEditorController m_instance;

    /**
     * Instance du controller de base
     */
    private BattleshipController m_controller = BattleshipController.getInstance();

    /**
     * @brief Bateau sélectionné dans la liste
     *
     * Bateau qui sera placé au prochain clic sur la grille
     */
    private Boat m_boatSelected;

    /**
     * @brief Retourne l'instance du singleton
     *
     * @return PlayerGridEditorController - Instance du singleton
     */
    public static PlayerGridEditorController getInstance() {
        if (m_instance == null)
            m_instance = new PlayerGridEditorController();

        return m_instance;
    }

    private PlayerGridEditorController() {}

    /**
     * @brief Retourne le bateau présentement sélectionné
     *
     * @return Boat - Le bateau présentement sélectionné
     */
    public Boat getBoatSelected() {
        return m_boatSelected;
    }

    /**
     * @brief Définit le bateau présentement sélectionné
     *
     * @param boatSelected : Boat - Le bateau présentement sélectionné
     */
    public void setBoatSelected(Boat boatSelected) {
        m_boatSelected = boatSelected;
    }

    /**
     * @brief Exécute l'initialisation automatique de la grille
     *
     * Notifie le début du jeu
     */
    public void autoInit() {
        m_controller.getPlayerGrid().autoInit();

        BattleshipController.getInstance().notifyObservers(NotifyType.START, null);
    }

    /**
     * Définit la position d'un bateau
     *
     * @param position : Position2D - La position de base souhaitée
     * @param isVertical : boolean - Verticalité du bateau souhaitée
     */
    public void setBoatPosition(Position2D position, boolean isVertical) {
        m_controller.getPlayerGrid().setBoatPosition(m_boatSelected, position, isVertical);

        if (m_controller.getPlayerGrid().isSetUp()) {
            m_controller.notifyObservers(NotifyType.START, null);
        }
    }

}
