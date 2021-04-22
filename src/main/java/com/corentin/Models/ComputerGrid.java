package com.corentin.Models;

import com.corentin.Utils.NotifyType;

/**
 * Objet utilisé pour gérer la grille de l'ordinateur
 *
 * @author Corentin VÉROT
 * @date 2020-02-18
 */
public class ComputerGrid extends Grid {

    public ComputerGrid() {
        super();

        autoInit(); // Initialisation automatique systématique pour un ordinateur
    }

    /**
     * @brief Est-ce que la position a déjà été visée ?
     *
     * Sert à ne pas toucher la position deux fois
     *
     * @param position : Position2D
     * @return boolean - Est-ce que la position a déjà été touchée ?
     */
    private boolean isAlreadyShoot(Position2D position) {
        return m_aiGrille[position.getY()][position.getX()] == 6;
    }

    /**
     * @brief Permet à l'ordinateur de jouer
     *
     * Génère une position aléatoirement et execute les actions
     * et notifie que le joueur a joué
     *
     * @param adversaryGrid : Grid - La grille de l'adversaire
     *
     */
    public void play(Grid adversaryGrid) {
        Position2D position;

        do {
            position = new Position2D(randRange(0, 10), randRange(0, 10));
        } while (isAlreadyShoot(position));

//        position = new Position2D(0, 0);

        adversaryGrid.onMove(position);
        adversaryGrid.notifyObservers(NotifyType.ADVERSARY_PLAYED, position);
    }
}
