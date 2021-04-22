package com.corentin.Controllers;

import com.corentin.Models.ComputerGrid;
import com.corentin.Models.PlayerGrid;
import com.corentin.Models.Position2D;
import com.corentin.Utils.NotifyType;
import com.corentin.Utils.Observable;
import javafx.application.Platform;

/**
 * Contrôleur du jeu Battleship
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public final class BattleshipController extends Observable {

    /**
     * @brief Grille du joueur
     */
    private ComputerGrid m_computerGrid;

    /**
     * @brief Grille de l'ordinateur
     */
    private PlayerGrid m_playerGrid;

    /**
     * @brief Instance du singleton
     */
    private static BattleshipController m_instance;

    /**
     * @brief Est-ce que le joueur est présentement entrain de tricher ?
     */
    private boolean m_bIsCheating = false;

    /**
     * @brief Est-ce que le joueur a triché au cours de la partie ?
     */
    private boolean m_bIsPlayerCheated = false;

    /**
     * @brief Est-ce que le joueur a gagné ?
     */
    private boolean m_bPlayerWin = false;

    /**
     * @brief Est-ce que l'ordinateur a gagné ?
     */
    private boolean m_bComputerWin = false;

    /**
     * @brief Est-ce que c'est au tour de l'ordinateur de jouer ?
     *
     * Empêche le joueur de jouer pendant le tour de l'ordinateur
     */
    private boolean m_bIsComputerTurn = false;


    private BattleshipController() {
        restart();
    }

    /**
     * @brief Retourne l'instance du singleton
     *
     * @return BatailleNavaleController - Instance du singleton
     */
    public static BattleshipController getInstance() {
        if (m_instance == null)
            m_instance = new BattleshipController();

        return m_instance;
    }

    /**
     * @brief Retourne la grille du joueur
     *
     * @return PlayerGrid - La grille du joueur
     */
    public PlayerGrid getPlayerGrid() {
        return m_playerGrid;
    }

    /**
     * @brief Retourne la grille de l'ordinateur
     *
     * @return PlayerGrid - La grille de l'ordinateur
     */
    public ComputerGrid getComputerGrid() {
        return m_computerGrid;
    }

    /**
     * @brief Le joueur est-il entrain de tricher ?
     *
     * @return boolean
     */
    public boolean isCheating() {
        return m_bIsCheating;
    }

    /**
     * @brief Le joueur a-t-il triché ?
     *
     * @return boolean
     */
    public boolean hasCheated() {
        return m_bIsPlayerCheated;
    }

    /**
     * @brief L'ordinateur a-t-il gagné ?
     *
     * @return boolean
     */
    public boolean isComputerWin() {
        return m_bComputerWin;
    }

    /**
     * @brief Le joueur a-t-il gagné ?
     *
     * @return boolean
     */
    public boolean isPlayerWin() {
        return m_bPlayerWin;
    }

    /**
     * @brief Définit le mode de triche
     *
     * @param enableCheat : boolean - Active ou désactive le mode de triche
     */
    public void setCheatMode(boolean enableCheat) {
        m_bIsCheating = enableCheat;

        if (enableCheat) {
            m_bIsPlayerCheated = true;
        }

        m_computerGrid.notifyObservers(NotifyType.GRID_UPDATED, null);
    }

    /**
     * @brief Méthode exécutant le tour du joueur
     *
     * Cette méthode doit s'éxecuter avant l'annimation du cannon du joueur
     * qui tire sur les positions adversaires
     */
    public void play(Position2D position) {

        if (!m_bIsComputerTurn) {
            m_playerGrid.play(m_computerGrid, position);
            m_bIsComputerTurn = true;
        }
    }

    /**
     * @brief Méthode exécutant le tour de l'ordinateur
     *
     * Cette méthode doit s'éxécuter après l'annimation du cannon du joueur
     * qui tire sur les positions adversaires
     */
    public void computerPlay() {
        m_computerGrid.play(m_playerGrid);

        // On teste s'il y a GameOver et on stocke le résultat dans une booléenne (permet de savoir qui a gagné / perdu)
        m_bComputerWin = m_playerGrid.isOver();
        m_bPlayerWin = m_computerGrid.isOver();

        if (m_bComputerWin || m_bPlayerWin) {
            notifyObservers(NotifyType.ENDGAME, null);
        }

        m_bIsComputerTurn = false;
    }

    /**
     * Redémarre le jeu
     */
    public void restart() {
        // Remplacement des modèles par des neufs
        m_computerGrid = new ComputerGrid();
        m_playerGrid = new PlayerGrid();

        // On remet les indicateurs de triche à faux
        m_bIsPlayerCheated = false;
        m_bIsCheating = false;

        // On veut notifier la vue du changement de modèles
        notifyObservers(NotifyType.RESTART, null);
    }

    /**
     * Quitte proprement l'application
     */
    public void quit() {
        Platform.exit();
        System.exit(0);
    }
}
