package com.corentin.Views;

import com.corentin.Models.Boat;
import com.corentin.Models.Grid;
import com.corentin.Models.Position2D;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * Vue de la grille de l'ordinateur
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public class AdversaryGridUI extends GridUI {

    public AdversaryGridUI(Grid grid, String gridName) {
        super(grid, gridName);
    }

    @Override
    protected void drawGrid() {
        Integer[][] grid = m_gridModel.getGrid();

        for (int i = 0; i < grid.length + 1; ++i) {
            for (int j = 0; j < grid[0].length + 1; ++j) {
                Rectangle rectangle = new Rectangle(m_iCellSize, m_iCellSize, Color.TRANSPARENT);

                final int finalY = i - 1;
                final int finalX = j - 1;
                if (finalX >= 0 && finalY >= 0) {
                    rectangle.setOnMouseClicked((mouseEvent -> {
                        m_controller.play(new Position2D(finalX, finalY));
                    }));

                    if (grid[finalY][finalX] == 6) {
                        rectangle.setFill(new ImagePattern(m_imgShoot));
                    } else if (grid[i - 1][j - 1] == 7) {
                        rectangle.setFill(new ImagePattern(m_imgShot));
                    }
                }

                if (i == 0 || j == 0) {
                    drawIndex(j, i, rectangle);
                }
                else {
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
    @Override
    protected void shootAnimation(Position2D position) {
        Pane gameUIPane = GameUI.getInstance().getPane();

        Rectangle rectangle = new Rectangle(m_iCellSize, m_iCellSize, Color.BLUE);
        rectangle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/img/bomb.png"))));

        rectangle.setX(gameUIPane.getWidth() / 2);
        rectangle.setY(gameUIPane.getHeight() - m_iCellSize);

        gameUIPane.getChildren().add(rectangle);

        final int coefX = ((position.getX() * m_iCellSize) + (int)gameUIPane.getWidth() / 2 + m_iCellSize / 2);
        final int coefY = ((position.getY() * m_iCellSize) + m_iCellSize + m_iCellSize / 2);

        Timeline m_timeline = new Timeline(new KeyFrame(Duration.millis(20), actionEvent -> {

            boolean hasMoved = false;

            if (rectangle.getX() < coefX) {
                rectangle.setX(rectangle.getX() + 10);
                hasMoved = true;
            }

            if (rectangle.getY() > coefY) {
                rectangle.setY(rectangle.getY() - 10);
                hasMoved = true;
            }

            if (!hasMoved) {
                gameUIPane.getChildren().remove(rectangle);
            }
        }));
        m_timeline.setCycleCount(60);
        m_timeline.setOnFinished(actionEvent -> m_controller.computerPlay());


        try {
            AudioClip shootSong = new AudioClip(getClass().getResource("/song/shoot2.mp3").toURI().toString());
            shootSong.play();
        } catch (URISyntaxException uriSyntaxException) {
            System.out.println("Resource can't be loaded");
        }

        m_timeline.play();
    }

    /**
     * @brief Dessine les bateaux sur la grille
     *
     * Si le joueur ne triche pas, la méthode déssinera uniquement les bateaux coulés.
     * S'il triche, tous les bateaux seront dessinés
     */
    @Override
    protected void drawBoats() {
        // Si le joueur triche on dessine tous les bateaux
        if (m_controller.isCheating()) {
            super.drawBoats();
        } else { // Sinon on ne dessine que ceux qui sont coulés
            final Integer[][] grid = m_gridModel.getGrid();

            Map<Integer, Boat> mBoats = m_gridModel.getBateaux();

            for (Integer index : mBoats.keySet()) {
                // Si le bateau a été coulé
                if (mBoats.get(index).isDrowned()) {
                    Rectangle rectangle = new Rectangle(m_iCellSize, m_iCellSize, Color.WHITE);

                    Position2D position = mBoats.get(index).getBasePosition();

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
    }
}
