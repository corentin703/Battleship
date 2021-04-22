package com.corentin;

/**
 * @mainpage Battleship
 * @version 1.0
 *
 * @section intro Introduction
 *
 * Ce jeu est un clone de Bataille Navale (Battleship).
 * Vous devez toucher tous les bateaux de votre adversaire (qui ont une taille plus ou moins grande).
 *
 * Votre adversaire est ici votre ordinateur.
 *
 * @section tips Astuces
 *
 * Au choix, vous disposez :
 * - d'une génération aléatoire de votre grille (vos bateaux peuvent-être placés automatiquement et pseudo-aléatoirement);
 * - d'un mode triche.
 *
 * @section screenshot Captures d'écran
 *
 * - Menu d'édition de grille :
 * @image html menu_edition.jpg width=650px
 *
 * - Partie en cours :
 * @image html jeu.jpg width=650px
 *
 * - Partie en mode triche :
 * @image html jeu_triche.jpg width=650px
 *
 */


/**
 * Classe contenant le main du jeu Battleship
 *
 * "Workaround" permettant de générer un .jar exécutable
 * Maven ne permet pas générer un .jar exécutant un main d'une classe héritant d'una Application
 *
 * @author Corentin VÉROT
 * @date 2021-04-22
 */
public final class Battleship {

    /**
     * @brief Méthode main du programme
     *
     * @param args : String[] - Arguments passés au programme
     */
    public static void main(String[] args) {
        App.run(args);
    }
}
