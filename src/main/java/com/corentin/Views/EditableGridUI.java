package com.corentin.Views;

import com.corentin.Controllers.PlayerGridEditorController;
import com.corentin.Models.Grid;
import com.corentin.Models.Position2D;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Vue de grille - version éditable
 *
 * @author Corentin VÉROT
 * @date 2020-04-01
 */
public class EditableGridUI extends GridUI {

    public EditableGridUI(Grid grid, String gridName) {
        super(grid, gridName);
    }

    /**
     * @brief Dessine la grille
     *
     * Dessine des cases vides avec un callback permettant le positionnement d'un bateau
     */
    @Override
    protected void drawGrid() {

        final Integer[][] grid = m_gridModel.getGrid();

        for (int i = 0; i < grid.length + 1; ++i) {
            for (int j = 0; j < grid[0].length + 1; ++j) {
                Rectangle rectangle = new Rectangle(m_iCellSize, m_iCellSize, Color.WHITE);

                final int finalX = j - 1;
                final int finalY = i - 1;

                if (i == 0 || j == 0) {
                    drawIndex(j, i, rectangle);
                } else if (grid[finalY][finalX] == 0) {
                    rectangle.setOnMouseClicked(mouseEvent -> {
                        if (PlayerGridEditorController.getInstance().getBoatSelected() != null) {
                            try {
                                Position2D position = new Position2D(finalX, finalY);

                                // Si clic gauche, le bateau sera horizontal, sinon il sera vertical
                                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                                    PlayerGridEditorController.getInstance().setBoatPosition(position, false);
                                } else {
                                    PlayerGridEditorController.getInstance().setBoatPosition(position, true);
                                }
                            } catch (IllegalArgumentException e) {
                                // Créé un message d'erreur indiquant que le positionnement est impossible ici
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur");
                                alert.setHeaderText("Positionnement impossible");
                                alert.setContentText("La position que vous avez séléctionné chevauche un autre bateau ou un bord de la grille." +
                                        "Veuillez placer votre bateau autre part !");

                                alert.showAndWait();
                            }
                        }
                    });
                    mf_gridPane.add(rectangle, j, i);
                }
            }
        }
    }
}
