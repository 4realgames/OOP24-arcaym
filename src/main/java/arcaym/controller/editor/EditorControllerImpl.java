package arcaym.controller.editor;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import arcaym.common.geometry.Point;
import arcaym.common.geometry.Rectangle;
import arcaym.common.utils.Optionals;
import arcaym.common.utils.Position;
import arcaym.controller.app.AbstractController;
import arcaym.controller.app.ControllerSwitcher;
import arcaym.controller.editor.saves.LevelMetadata;
import arcaym.controller.editor.saves.MetadataManagerImpl;
import arcaym.controller.game.GameControllerImpl;
import arcaym.controller.user.UserStateSerializerInfo;
import arcaym.controller.user.UserStateSerializerJSON;
import arcaym.model.editor.EditorGridException;
import arcaym.model.editor.EditorType;
import arcaym.model.editor.grid.GridModel;
import arcaym.model.editor.grid.GridModelImpl;
import arcaym.model.game.components.ComponentsBasedObjectsFactory;
import arcaym.model.game.core.engine.FactoryBasedGameBuilder;
import arcaym.model.game.objects.GameObjectType;
import arcaym.view.editor.EditorView;

/**
 * Implementation of {@link EditorController}.
 */
public class EditorControllerImpl extends AbstractController<EditorView> implements ExtendedEditorController {

    private final Set<GameObjectType> unlockedItems;
    private final LevelMetadata metadata;
    private GameObjectType selectedObject = GameObjectType.FLOOR;
    private final GridModel grid;
    private Optional<Consumer<Map<Position, List<GameObjectType>>>> view;

    /**
     * Creates a new editor controller.
     * 
     * @param width Width of the grid
     * @param height Height of the grid
     * @param type The type of grid that needs to be created
     * @param name The name to give to the level
     * @param switcher Used to change the active controller
     */
    public EditorControllerImpl(
        final int width, 
        final int height, 
        final EditorType type,
        final String name,
        final ControllerSwitcher switcher) {
        super(switcher);
        this.grid = new GridModelImpl(type, width, height);
        this.metadata = new LevelMetadata(
            name,
            UUID.randomUUID().toString(),
            type,
            Position.of(width, height));
        this.view = Optional.empty();
        this.unlockedItems = getUnlockedItems(type);
    }

    /**
     * Creates an editor controller starting from a metadata object.
     * 
     * @param metadata The object with internal data.
     * @param switcher Used to change the active controller
     */
    public EditorControllerImpl(
        final LevelMetadata metadata,
        final ControllerSwitcher switcher) {
        super(switcher);
        this.grid = new GridModelImpl(metadata);
        this.metadata = new LevelMetadata(
            metadata.levelName(),
            metadata.uuid(),
            metadata.type(),
            metadata.size());
        this.view = Optional.empty();
        this.unlockedItems = getUnlockedItems(metadata.type());
    }

    private Set<GameObjectType> getUnlockedItems(final EditorType type) {
        final UserStateSerializerInfo serializer = new UserStateSerializerJSON();
        return switch (type) {
            case EditorType.SANDBOX -> EnumSet.allOf(GameObjectType.class);
            case EditorType.NORMAL -> serializer.getUpdatedState().itemsOwned();
            default -> Collections.emptySet();
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        this.saveLevel();
        super.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<GameObjectType> getOwnedObjects() {
        return Collections.unmodifiableSet(this.unlockedItems);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void play() throws EditorGridException {
        this.grid.beforeStartCheck();
        this.saveLevel();
        final int tileSize = 100; // logic dimension of the tile
        final var gameBuilder = new FactoryBasedGameBuilder(new ComponentsBasedObjectsFactory(tileSize));
        final var objectsInPosition = this.grid.getFullMap();
        objectsInPosition.entrySet().forEach(e -> {
            final var objects = e.getValue();
            for (int i = 0; i < objects.size(); i++) {
                gameBuilder.addObject(
                    objects.get(i),
                    Point.of(
                        e.getKey().x() * tileSize + tileSize / 2,
                        e.getKey().y() * tileSize + tileSize / 2),
                    i
                );
            }
        });
        this.switcher().switchToGame(new GameControllerImpl(
            gameBuilder.build(
                new Rectangle(
                    Point.of(0, 0),
                    Point.of(
                        this.metadata.size().x() * tileSize,
                        this.metadata.size().y() * tileSize))),
            this.switcher()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedObject(final GameObjectType object) {
        this.selectedObject = object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo() {
        this.grid.undo();
        this.updateView(this.grid.getUpdatedGrid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canUndo() {
        return this.grid.canUndo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eraseArea(final Collection<Position> positions) throws EditorGridException {
        this.grid.removeObjects(positions);
        this.updateView(this.grid.getUpdatedGrid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyChange(final Collection<Position> positions) throws EditorGridException {
        this.grid.placeObjects(positions, selectedObject);
        this.updateView(grid.getUpdatedGrid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupMap() {
        this.updateView(this.grid.getFullMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Position getSize() {
        return Position.of(metadata.size().x(), metadata.size().y());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveLevel() {
        return this.grid.saveState(metadata.uuid())
            && new MetadataManagerImpl().saveMetadata(metadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setView(final Consumer<Map<Position, List<GameObjectType>>> listener) {
        this.view = Optional.of(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateView(final Map<Position, List<GameObjectType>> map) {
        final var update = Optionals.orIllegalState(this.view, "View listener not initialized");
        update.accept(map);
    }

}
