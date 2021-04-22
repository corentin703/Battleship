package com.corentin.Utils;

import java.util.ArrayList;

/**
 * Implémentation basique du design pattern Obervable/Observer (implémentation standard dépréciée dans Java 9)
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public abstract class Observable {

    /**
     * @brief Liste des observateurs
     */
    private ArrayList<Observer> m_observersList = new ArrayList<Observer>();

    /**
     * @brief Ajoute un observateur à la liste
     *
     * @param observerObject : Observer - L'observateur à ajouter
     */
    public void addObserver(Observer observerObject) {
        m_observersList.add(observerObject);
    }

    /**
     * @brief Supprime un observateur de la liste
     *
     * @param observerObject : Observer - L'observateur à supprimer
     */
    public void removeObserver(Observer observerObject) {
        m_observersList.remove(observerObject);
    }

    /**
     * @brief Notifie les observateurs enregistés
     *
     * @param notifyType : NotifyType
     * @param args : Object
     */
    public void notifyObservers(NotifyType notifyType, Object args) {
        for (Observer observer : m_observersList) {
            observer.update(this, notifyType, args);
        }
    }
}
