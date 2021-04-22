package com.corentin.Models;

import com.corentin.Utils.NotifyType;
import com.corentin.Utils.Observable;
import com.corentin.Utils.Observer;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Permet de représenter un bateau avec ses informations
 *
 * @author Corentin VÉROT
 * @date 2020-03-29
 */
public class Boat implements Observer {
    /**
     * @brief ID du bateau
     *
     * Sert à identifier les différents bateaux entre eux (via la grille)
     */
    private final int mf_iID;

    /**
     * @brief Taille du bateau
     *
     * Servant à l'initalisation lors du remplissage de la grille puis à déterminer la taille non endommagée du bateau
     *
     */
    private final int mf_iTaille;

    /**
     * @brief Nom du bateau pour l'affichage
     */
    private final String mf_sNom;

    /**
     * @brief Points de vie
     */
    private int m_iPV;

    /**
     * @brief Chemin de l'image représentant le bateau horizontalement
     */
    private final Image mf_hImage;

    /**
     * @brief Fichier image représentant le bateau à la verticale
     */
    private final Image mf_vImage;

    /**
     * @brief Position de la base du bateau
     */
    private Position2D m_basePosition;

    /**
     * @brief Verticalité du bateau
     */
    private boolean m_bIsVertical = false;

    /**
     * @brief Permet de construire un bateau avec sa taille et son nom
     *
     * @param iID : int
     * @param iTaille : int
     * @param sNom : String
     * @param hImage : File - Image pour affichage horizontal
     * @param vImage : File - Image pour affichage vertical
     */
    public Boat(int iID, int iTaille, String sNom, Image hImage, Image vImage) {
        mf_iID = iID;
        mf_iTaille = iTaille;
        mf_sNom = sNom;

        m_iPV = mf_iTaille;

        mf_hImage = hImage;

        mf_vImage = vImage;
    }

    /**
     * @brief Retourne l'ID du bateau
     *
     * @return int - LID du bateau
     */
    public int getID() {
        return mf_iID;
    }

    /**
     * @brief Retourne la taille du bateau
     *
     * @return int - La taille du bateau
     */
    public int getTaille() {
        return mf_iTaille;
    }

    /**
     * @brief Retourne le nom du bateau pour l'affichage
     *
     * @return String - Le nom du bateau
     */
    public String getNom() {
        return mf_sNom;
    }

    /**
     * @brief Retourne les points de vie du bateau
     *
     * @return int - Les points de vie du bateau
     */
    public int getPV() {
        return m_iPV;
    }

    /**
     * @brief Détermine si le bateau est toujours de ce monde
     *
     * @return boolean - Le bateau est-il détruit ?
     */
    public boolean isDrowned() {
        return m_iPV == 0;
    }

    /**
     * @brief Retourne la position de base du bateau
     *
     * @return Position2D - La position de base du bateau
     */
    public Position2D getBasePosition() {
        return m_basePosition;
    }

    /**
     * @brief Le bateau est-il vertical ?
     *
     * @return boolean - Verticalité du bateau
     */
    public boolean isVertical() {
        return m_bIsVertical;
    }

    /**
     * @brief Retourne l'image du bateau horizontale
     *
     * @return Image
     */
    public Image getHImage() {
        return mf_hImage;
    }

    /**
     * @brief Retourne l'image du bateau verticale
     *
     * @return Image
     */
    public Image getVImage() {
        return mf_vImage;
    }

    /**
     * @brief Définit la position de base du bateau
     *
     * @param basePosition : Position2D - Position de la base du bateau
     * @param bIsVertical : boolean - Vrai si le bateau est vertical
     */
    public void setPositioning(Position2D basePosition, boolean bIsVertical) {
        m_basePosition = basePosition;
        m_bIsVertical = bIsVertical;
    }


    /**
     * @brief Met à jour l'état du bateau s'il a été frappé
     */
    @Override
    public void update(Observable observableObject, NotifyType notifyType, Object args) {
        // On identifie l'objet observable
        if (observableObject instanceof Grid) {
            if (notifyType == NotifyType.BOAT_DAMAGE) {
                if ((int)args == mf_iID) {
                    m_iPV--;
                }
            }
        }
    }
}
