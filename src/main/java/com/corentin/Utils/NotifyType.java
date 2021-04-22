package com.corentin.Utils;

/**
 * Sert à différencier les différentes notifications afin de pouvoir réaliser l'action voulue
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public enum NotifyType {
    START, // Démarrage d'une partie
    RESTART, // Redémarrage du jeu

    GRID_UPDATED, // Mise à jour d'une grille

    BOAT_DAMAGE, // Bateau endommagé ou coulé
    BOAT_POSITIONED, // Bateau positionné (utile pour l'itialisation manuelle par le joueur)

    ENDGAME, // Fin du jeu

    ADVERSARY_PLAYED, // L'adversaire a joué (utile pour lancer l'annimation du canon)
}
