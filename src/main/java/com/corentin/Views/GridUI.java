package com.corentin.Views;

import com.corentin.Controllers.BattleshipController;
import com.corentin.Models.Boat;
import com.corentin.Models.Grid;
import com.corentin.Models.Position2D;
import com.corentin.Utils.NotifyType;
import com.corentin.Utils.Observable;
import com.corentin.Utils.Observer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * Vue de base d'une grille
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public class GridUI implements JavaFXView, Observer {

    // Layout
    protected final Pane mf_rootPane = new Pane();
    protected final VBox m_VBox = new VBox();

    /**
     * @brief Modèle de la grille
     */
    protected Grid m_gridModel;

    /**
     * @brief Nom de la grille
     */
    protected final Label mf_lblGridName;

    /**
     * @brief Layout de la grille
     */
    protected final GridPane mf_gridPane = new GridPane();

    /**
     * Largeur d'une cellule de la grille
     */
    protected final int m_iCellSize = 50;

    /**
     * Instance du contrôleur de base
     */
    protected final BattleshipController m_controller = BattleshipController.getInstance();

    /**
     * Image de croix pour les cases touchées par l'adversaire
     */
    protected final Image m_imgShoot = new Image(getClass().getResourceAsStream("/img/cross_red.png"));

    /**
     * Image de croix pour les bateaux touchés par l'adversaire
     */
    protected final Image m_imgShot = new Image(getClass().getResourceAsStream("/img/cross_black.png"));

    public GridUI(Grid grid, String gridName) {
        m_gridModel = grid;
        grid.addObserver(this);

        mf_lblGridName = new Label(gridName);
        mf_lblGridName.setTextAlignment(TextAlignment.CENTER);
        mf_lblGridName.setFont(new Font(24));

        m_VBox.setSpacing(10);
        m_VBox.setAlignment(Pos.CENTER);
        m_VBox.getChildren().addAll(mf_lblGridName, mf_gridPane);

        mf_rootPane.getChildren().add(m_VBox);
    }

    /**
     * @brief On retourne le layout principal de la vue
     *
     * @return Pane - Le layout principal de la vue
     */
    public Pane getPane() {
        return mf_rootPane;
    }

    /**
     * @brief Retourne le modèle de la grille
     *
     * @return Le modèle de la grille
     */
    public Grid getGridModel() {
        return m_gridModel;
    }

    /**
     * @brief Définit le modèle de la grille
     *
     * Utile en cas de redémarrage
     *
     * @param gridModel : Grid - Le modèle de la grille
     */
    public void setGridModel(Grid gridModel) {
        m_gridModel.removeObserver(this);
        m_gridModel = gridModel;
        gridModel.addObserver(this);
        gridModel.notifyObservers(NotifyType.GRID_UPDATED, null);
    }

    /**
     * @brief Initialise (ou réinitialise) le layout
     */
    protected void resetGridPane() {
        mf_gridPane.setStyle(null);
        mf_gridPane.getChildren().clear();

        mf_gridPane.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true;");
        mf_gridPane.setAlignment(Pos.CENTER);
    }

    /**
     * @brief Dessine les coordonnées sur les bords de la grille
     *
     * @param iX : int - Position sur l'axe X
     * @param iY : int - Position sur l'axe Y
     * @param rectangle : Rectangle - Rectangle à remplir
     */
    protected void drawIndex(int iX, int iY, Rectangle rectangle) {
        if (iX != 0) { // Index horizontal
            StackPane stackPane = new StackPane();
            Label lblIndex = new Label(Character.toString(iX + 64));

            rectangle.setHeight(m_iCellSize / 2);

            stackPane.getChildren().addAll(
                    rectangle,
                    lblIndex
            );
            mf_gridPane.add(stackPane, iX, iY);

        } else if (iY != 0) { // Index vertical
            StackPane stack = new StackPane();
            Label lblIndex = new Label(String.valueOf(iY));

            // On se sert des codes ASCII pour déduire une lettre alphabétique de l'index

            rectangle.setWidth(m_iCellSize / 2);

            stack.getChildren().addAll(
                    rectangle,
                    lblIndex
            );

            mf_gridPane.add(stack, iX, iY);
        }
    }

    /**
     * @brief Dessine les bateaux sur la grille
     */
    protected void drawBoats() {
        final Integer[][] grid = m_gridModel.getGrid();

        Map<Integer, Boat> mBoats = m_gridModel.getBateaux();

        for (Integer index : mBoats.keySet()) {
            Rectangle rectangle = new Rectangle(m_iCellSize, m_iCellSize, Color.WHITE);

            Position2D position = mBoats.get(index).getBasePosition();

            // Si le bateau a été positionné
            if (position != null) {
                if (mBoats.get(index).isVertical()) {
                    ImagePattern imagePattern = new ImagePattern(mBoats.get(index).getVImage());

                    rectangle.setHeight(m_iCellSize * mBoats.get(index).getTaille());
                    rectangle.setFill(imagePattern);

                    mf_gridPane.add(rectangle, position.getX() + 1, position.getY() + 1, 1, mBoats.get(index).getTaille());
                } else {
                    ImagePattern imagePattern = new ImagePattern(mBoats.get(index).getHImage());

                    rectangle.setWidth(m_iCellSize * mBoats.get(index).getTaille());
                    rectangle.setFill(imagePattern);

                    mf_gridPane.add(rectangle, position.getX() + 1, position.getY() + 1, mBoats.get(index).getTaille(), 1);
                }
            }
        }
    }

    /**
     * @brief Dessine les cellules vides de la grille
     *
     * Dessine des cellules blanches ou remplies par des croix rouges ou noires selon si une cible est touchée ou non
     */
    protected void drawGrid() {
        final Integer[][] grid = m_gridModel.getGrid();

        for (int i = 0; i < grid.length + 1; ++i) {
            for (int j = 0; j < grid[0].length + 1; ++j) {
                Rectangle rectangle = new Rectangle(m_iCellSize, m_iCellSize, Color.WHITE);

                if (i == 0 || j == 0) {
                    drawIndex(j, i, rectangle);
                } else if (grid[i - 1][j - 1] == 0) {
                    mf_gridPane.add(rectangle, j, i);
                } else if (grid[i - 1][j - 1] == 6) {
                    rectangle.setFill(new ImagePattern(m_imgShoot));
                    mf_gridPane.add(rectangle, j, i);
                } else if (grid[i - 1][j - 1] == 7) {
                    rectangle.setFill(new ImagePattern(m_imgShot));
                    mf_gridPane.add(rectangle, j, i);
                }
            }
        }
    }

    /**
     * @brief Lance l'annimation de tir (de la position adversaire vers la grille joueur)
     *
     * @param position : Position2D - Coordonnées de tir
     */
    protected void shootAnimation(Position2D position) {
        Pane gameUIPane = GameUI.getInstance().getPane();

        Rectangle rectangle = new Rectangle(m_iCellSize, m_iCellSize, Color.BLUE);
        rectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/img/bomb.png"))));

        rectangle.setX(0);
        rectangle.setY(0);

        gameUIPane.getChildren().add(rectangle);

        final int coefX = ((position.getX() * m_iCellSize) + m_iCellSize / 2);
        final int coefY = ((position.getY() * m_iCellSize) + m_iCellSize);

        Timeline m_timeline = new Timeline(new KeyFrame(Duration.millis(20), actionEvent -> {

            boolean hasMoved = false;

            if (rectangle.getX() < coefX) {
                rectangle.setX(rectangle.getX() + 10);
                hasMoved = true;
            }

            if (rectangle.getY() < coefY) {
                rectangle.setY(rectangle.getY() + 10);
                hasMoved = true;
            }

            if (!hasMoved) {
                gameUIPane.getChildren().remove(rectangle);
            }
        }));

        m_timeline.setCycleCount(60);

        try {
            AudioClip shootSong = new AudioClip(getClass().getResource("/song/shoot1.mp3").toURI().toString());
            shootSong.play();
        } catch (URISyntaxException uriSyntaxException) {
            System.out.println("Resource can't be loaded");
        }

        m_timeline.play();
    }

    /**
     * @brief Met à jour l'UI de l'éditeur
     *
     * - Si l'adversaire a joué, on lance l'animation
     * - Si le modèle de la grille a été mis à jour, on met à jour sa vue
     *
     * @param observableObject : Observable - Objet observable ayant appelé la méthode
     * @param notifyType : NotifyType - Type de notification pour avoir la réaction associée
     * @param args : Object - Paramètre additionnel
     */
    @Override
    public void update(Observable observableObject, NotifyType notifyType, Object args) {
        if (notifyType == NotifyType.ADVERSARY_PLAYED && args instanceof Position2D) {
            shootAnimation((Position2D) args);

        } else if (notifyType == NotifyType.GRID_UPDATED) {
            resetGridPane();

            drawBoats();
            drawGrid();
        }
    }

}
