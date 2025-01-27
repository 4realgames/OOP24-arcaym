package arcaym.view.editor.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import arcaym.common.utils.Position;
import arcaym.model.game.objects.api.GameObjectType;
import arcaym.view.api.ViewComponent;
/**
 * An implementation of the grid view.
 */
public class GridView implements ViewComponent<JPanel> {

    private static final int COLUMNS = 50;
    private static final int ROWS = 28;

    private final Consumer<Collection<Position>> reciver;
    private final BiMap<JPanel, Position> cells = HashBiMap.create();
    private final int cellDimension;

    /**
     * The constructor of the component.
     * @param sendObjects the function that needs to process the list of objects
     * @param cellDimension the minimum dimension of the cell
     */
    public GridView(final Consumer<Collection<Position>> sendObjects, final int cellDimension) {
        this.reciver = sendObjects;
        this.cellDimension = cellDimension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel build() {
        return buildGrid();
    }

    private JPanel buildGrid() {
        final var grid = new JPanel(new GridLayout(ROWS, COLUMNS));

        final MouseListener m = new MouseListener() {

            private final List<Position> positionInvolved = new ArrayList<>();
            private boolean isClicking;

            @Override
            public void mouseClicked(final MouseEvent e) {
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                isClicking = true;
                final var panel = (JPanel) e.getSource();
                if (cells.containsKey(panel)) {
                    positionInvolved.add(cells.get(panel));
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                isClicking = false;
                reciver.accept(positionInvolved);
                positionInvolved.clear();
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                final var panel = (JPanel) e.getSource();
                if (cells.containsKey(panel) && !positionInvolved.contains(cells.get(panel)) && isClicking) {
                    positionInvolved.add(cells.get(panel));
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
            }
        };

        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                final JPanel jp = new JPanel();
                jp.setSize(new Dimension(cellDimension, cellDimension));
                jp.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                jp.setBackground(Color.BLUE);
                cells.put(jp, Position.of(i, j));
                jp.addMouseListener(m);
                grid.add(jp);
            }
        }
        return grid;
    }

    /**
     * Changes the grid cells view based on the given map.
     * @param positions Contains the list of objets to place in the cell in a specific position
     */
    public void setPositionFromMap(final Map<Position, List<GameObjectType>> positions) {
        // temporal method, to update with images
        positions.entrySet().forEach(e -> {
            var color = Color.BLUE;
            if (e.getValue().size() == 1) {
                if (e.getValue().getFirst().equals(GameObjectType.FLOOR)) {
                    color = Color.LIGHT_GRAY;
                }
            } else {
                color = Color.PINK;
            }
            cells.inverse().get(e.getKey()).setBackground(color);
        });
    }
}
