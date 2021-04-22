package com.corentin.Models;

import com.corentin.Utils.NotifyType;

import java.net.URISyntaxException;

/**
 * Objet utilisé pour gérer la grille du joueur
 *
 * @author Corentin VÉROT
 * @date 2020-02-18
 */
public class PlayerGrid extends Grid {

    /**
     * @brief  Nombre de bateaux placés
     *
     * Permet de savoir si la grille est initialisée ou non
     * (si c'est égal au nombre de bateaux enregistrés dans le jeu)
     */
    private int m_iNBoatSetUp = 0;

    public PlayerGrid() {
        super();
    }

    /**
     * @brief Définit la position d'un bateau et sa verticalité
     *
     * Vérifie si la position avec l'orientation est correcte avant de placer et de notifier
     * Si elle n'est pas correcte, elle lève une exception
     *
     * @param boat : Boat - Le bateau à positionner
     * @param position - Position2D - La position souhaitée
     * @param isVertical - boolean - La verticalité souhaitée
     */
    public void setBoatPosition(Boat boat, Position2D position, boolean isVertical) {
        if (isPositionCorrect(position, isVertical, boat.getTaille())) {
            m_aiGrille[position.getY()][position.getX()] = boat.getID();
            boat.setPositioning(position, isVertical);

            for (int k = 1; k < boat.getTaille(); ++k) {
                if (isVertical) {
                    m_aiGrille[position.getY() + k][position.getX()] = boat.getID();
                } else {
                    m_aiGrille[position.getY()][position.getX() + k] = boat.getID();
                }
            }

            // Ajout des bateaux dans les objets observables
            addObserver(boat);
            m_iNBoatSetUp++;

            notifyObservers(NotifyType.BOAT_POSITIONED, boat);
            notifyObservers(NotifyType.GRID_UPDATED, null);
        } else {
            throw new IllegalArgumentException("Positionnement impossible !");
        }
    }

    /**
     * La grille est-elle paramétrée ?
     *
     * @return boolean
     */
    public boolean isSetUp() {
        return m_iNBoatSetUp == m_mBateaux.size();
    }

    /**
     * @brief Initialise la grille pseudo-aléatoirement
     *
     * Génère une position aléatoirement, vérifie que le positionnement donné est correct
     * et positionne le bateau le cas échéant.
     * Ajoute le bateau à la liste des observateurs de la grille
     *
     */
    @Override
    public void autoInit() {
        m_iNBoatSetUp = m_mBateaux.size();
        super.autoInit();
    }

    /**
     * @brief Permet au joueur de jouer
     *
     * @param adversaryGrid : Grid - La grille de l'adversaire
     * @param position : Position2D - La position du tir
     *
     * Execute les actions et notifie que le joueur a joué
     *
     */
    public void play(Grid adversaryGrid, Position2D position) {
        adversaryGrid.onMove(position);
        adversaryGrid.notifyObservers(NotifyType.ADVERSARY_PLAYED, position);
    }
}
