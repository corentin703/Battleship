package com.corentin.Models;

import com.corentin.Utils.NotifyType;
import com.corentin.Utils.Observable;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Classe abstraite utilisée pour gérer une grille.
 * Elle définit les lignes directrices que doivent suivre les classes filles ComputerGrid et PlayerGrid
 *
 * @author Corentin VÉROT
 * @date 2020-02-18
 */
public abstract class Grid extends Observable {

    /**
     * @brief Tableau contenant les données de la grille
     */
    protected final Integer[][] m_aiGrille = new Integer[10][10];

    /**
     * @brief Map contenant les différents bateaux indexés par leur identifiant
     *
     * Cela permet de les retrouver facilement à partir de leur identifiant
     */
    protected final Map<Integer, Boat> m_mBateaux;

    /**
     * @brief Objet de génération aléatoire
     */
    protected final Random rand = new Random();

    public Grid() {
        for (int colIndex = 0; colIndex < m_aiGrille.length; ++colIndex) {
            for (int rowIndex = 0; rowIndex < m_aiGrille[colIndex].length; ++rowIndex) {
                m_aiGrille[colIndex][rowIndex] = 0;
            }
        }

        m_mBateaux = new HashMap<Integer, Boat>();

        m_mBateaux.put(
                1,
                new Boat(
                        1,
                        5,
                        "Porte avions",
                        new Image(getClass().getResourceAsStream("/img/aircraft_carrier_h.png")),
                        new Image(getClass().getResourceAsStream("/img/aircraft_carrier_v.png"))
                )
        );

        m_mBateaux.put(
                2,
                new Boat(
                        2,
                        4,
                        "Croiseur",
                        new Image(getClass().getResourceAsStream("/img/cruiser_h.png")),
                        new Image(getClass().getResourceAsStream("/img/cruiser_v.png"))
                )
        );

        m_mBateaux.put(
                3,
                new Boat(
                        3,
                        3,
                        "Contre-torpilleurs",
                        new Image(getClass().getResourceAsStream("/img/against_torpedo_boat_h.png")),
                        new Image(getClass().getResourceAsStream("/img/against_torpedo_boat_v.png"))
                )
        );

        m_mBateaux.put(
                4,
                new Boat(
                        4,
                        3,
                        "Sous-marin",
                        new Image(getClass().getResourceAsStream("/img/submarine_h.png")),
                        new Image(getClass().getResourceAsStream("/img/submarine_v.png"))
                )
        );

        m_mBateaux.put(
                5,
                new Boat(
                        5,
                        2, "Torpilleur",
                        new Image(getClass().getResourceAsStream("/img/torpedo_boat_h.png")),
                        new Image(getClass().getResourceAsStream("/img/torpedo_boat_v.png"))
                )
        );
    }

    /**
     * @brief Retourne la grille de jeu
     *
     * @return Integer[][] - La grille de jeu
     */
    public Integer[][] getGrid() {
        return m_aiGrille;
    }

    /**
     * @brief Retourne la map des bateaux indexés par leur ID
     *
     * @return Map<Integer, Boat> - Les bateaux indexés par leur ID
     */
    public Map<Integer, Boat> getBateaux() {
        return m_mBateaux;
    }

    /**
     * @brief Determine si la position est correcte
     *
     * @param position : Position2D - Position à vérifier
     * @param isVertical : boolean La direction est horizontale ? Sinon elle est verticale
     * @param taille : int Taille du bateau
     * @return boolean - Est-ce qu'un bateau peut-être plcé sur cette case
     */
    protected boolean isPositionCorrect (Position2D position, boolean isVertical, int taille) {

        if (isVertical) {
            // Teste si le bateau rentre dans la grille
            if ((position.getY() + taille) > 10) {
                return false;
            }

            // Vérifie qu'aucun autre bateau est placé au même endroit
            for (int i = position.getY(); i < position.getY() + taille; ++i) {
                if (m_aiGrille[i][position.getX()] != 0)
                    return false;
            }

        }
        // Idem que pour le vertical
        else {
            if ((position.getX() + taille) > 10) {
                return false;
            }

            for (int i = position.getX(); i < position.getX() + taille; ++i) {
                if (m_aiGrille[position.getY()][i] != 0)
                    return false;
            }
        }

        return true;
    }

    /**
     * @brief Calcule un nombre aléatoire à partir d'un interval
     *
     * @param a : int
     * @param b : int
     * @return int Nombre aléatoire compris dans l'interval [a, b[
     */
    protected int randRange(int a, int b) {
        return (rand.nextInt(b - a) + a);
    }

    /**
     * @brief Initialise la grille pseudo-aléatoirement
     *
     * Génère une position aléatoirement, vérifie que le positionnement donné est correct
     * et positionne le bateau le cas échéant.
     * Ajoute le bateau à la liste des observateurs de la grille
     *
     */
    protected void autoInit() {
        boolean isVertical;

        Position2D position;

        for (Integer boatID : m_mBateaux.keySet()) {
            while (true) {
                position = new Position2D(randRange(0, 10), randRange(0, 10));

                isVertical = (randRange(1, 3) == 1);

                if (isPositionCorrect(position, isVertical, m_mBateaux.get(boatID).getTaille())) {
                    m_aiGrille[position.getY()][position.getX()] = m_mBateaux.get(boatID).getID();
                    m_mBateaux.get(boatID).setPositioning(position, isVertical);

                    for (int k = 1; k <m_mBateaux.get(boatID).getTaille(); ++k) {
                        if (isVertical) {
                            m_aiGrille[position.getY() + k][position.getX()] = m_mBateaux.get(boatID).getID();
                        } else {
                            m_aiGrille[position.getY()][position.getX() + k] = m_mBateaux.get(boatID).getID();
                        }
                    }

                    // Ajout des bateaux dans les objets observables
                    addObserver(m_mBateaux.get(boatID));

                    break;
                }
            }
        }
    }


    /**
     * @brief Vérifie s'il y a eu impact et notifie les bateaux si tel est le cas
     *
     * @param position : Position2D - Position à vérifier
     * @return int - ID du bateau touché, retourne -1 si aucun impact
     */
    protected boolean checkShot(Position2D position) {
        if (m_aiGrille[position.getY()][position.getX()] != 0 &&
                m_aiGrille[position.getY()][position.getX()] != 6) {
            int shot = m_aiGrille[position.getY()][position.getX()];

            notifyObservers(NotifyType.BOAT_DAMAGE, shot);
            return true;
        }

        return false;
    }

    /**
     * @brief Regarde si un bateau est touché ou coulé, enregistre le coup et notifie la vue
     *
     * @param position : Position2D - Position à vérifier
     */
    public void onMove(Position2D position) {

        if (checkShot(position)) {
            m_aiGrille[position.getY()][position.getX()] = 7;

        } else {
            m_aiGrille[position.getY()][position.getX()] = 6;
        }

        notifyObservers(NotifyType.GRID_UPDATED, null);
    }

    /**
     * @brief Vérifie si tous les bateaux de la grille sont coulés
     *
     * @return boolean - Vrai si tous les bateaux de la grille sont coulés
     */
    public boolean isOver() {
        for (Integer boatID : m_mBateaux.keySet()) {
            if (!m_mBateaux.get(boatID).isDrowned())
                return false;
        }

        return true;
    }

    /**
     * @brief Permet d'afficher les objets de type Models à l'écran
     *
     * @return String : Chaine de caractère qui sera affichée à l'écran
     */
    @Override
    public String toString() {
        String columns = "     A B C D E F G H I J \n \n";

        for (int i = 0; i < m_aiGrille.length; ++i) {

            if (i == 9)
                columns += ((i + 1) + "   ");
            else
                columns += ((i + 1) + "    ");

            for (int j = 0; j < m_aiGrille[i].length; j++) {
                columns += (m_aiGrille[i][j] + " ");
            }
            columns += ("\n");
        }

        return columns;
    }
}
