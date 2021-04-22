package com.corentin.Models;

/**
 * Classe représentant une position 2D (x, y)
 * X et Y doivent-être compris entre un intervale donné (ici 0 et 9 pour X et Y)
 *
 * @author Corentin VÉROT
 * @date 2020-02-18
 */
public class Position2D {
    /**
     * @brief Position X
     */
    private int m_iX;

    /**
     * @brief Position Y
     */
    private int m_iY;

    /**
     * @brief Position minimum sur X
     */
    public final int Xmin = 0;

    /**
     * @brief Position maximum sur X
     */
    public final int Xmax = 9;

    /**
     * @brief Position minimum sur Y
     */
    public final int Ymin = 0;

    /**
     * @brief Position maximum sur Y
     */
    public final int Ymax = 9;

    /**
     * @brief Construit une position avec les coordonnées (0, 0)
     */
    public Position2D() {
        this(0, 0);
    }

    /**
     * @brief Construit une position avec les coordonnées spécifiés en paramètres.
     * Ceux-ci doivent être compris entre Xmin et Xmax pour X et Ymin et Ymax pour Y.
     *
     * @param iX : int
     * @param iY : int
     */
    public Position2D(int iX, int iY) {
        setX(iX);
        setY(iY);
    }

    /**
     * @brief Retourne X
     *
     * @return int - X
     */
    public int getX() {
        return m_iX;
    }

    /**
     * @brief Définit X
     *
     * @param iX : int
     */
    public void setX(int iX) {
        if (iX < Xmin || iX > Xmax)
            throw new IllegalArgumentException("X doit-être compris entre " + Xmin + " et " + Xmax);

        m_iX = iX;
    }

    /**
     * @brief Retourne Y
     *
     * @return Y : int
     */
    public int getY() {
        return m_iY;
    }

    /**
     * @brief Définit Y
     *
     * @param iY : int - Y
     */
    public void setY(int iY) {
        if (iY < Ymin || iY > Ymax)
            throw new IllegalArgumentException("Y doit-être compris entre " + Ymin + " et " + Ymax);

        m_iY = iY;
    }

    /**
     * @brief Permet d'afficher une position à l'écran
     *
     * @return String : Chaine de caractère qui sera affichée à l'écran
     */
    @Override
    public String toString() {
        return "(" + m_iX + ", " + m_iY + ")";
    }
}