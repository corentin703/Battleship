package com.corentin.Utils;

/**
 * Implémentation basique du design pattern Obervable/Observer (implémentation standard dépréciée dans Java 9)
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public interface Observer {

    /**
     * @brief Met à jour l'objet suite à une notification
     *
     * @param observableObject : Observable - Objet observable ayant appelé la méthode
     * @param notifyType : NotifyType - Type de notification pour avoir la réaction associée
     * @param args : Object - Paramètre additionnel
     */
    void update(Observable observableObject, NotifyType notifyType, Object args);
}
