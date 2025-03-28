package arcaym.controller.game;

import arcaym.controller.app.AbstractController;
import arcaym.controller.app.ControllerSwitcher;
import arcaym.model.game.core.engine.Game;
import arcaym.model.game.core.engine.GameStateInfo;
import arcaym.view.game.GameView;

/**
 * Implementation of {@link GameController} that extends
 * {@link AbstractController} of {@link GameView}.
 */
public class GameControllerImpl extends AbstractController<GameView> implements ExtendedGameController {
    private final Game game;

    /**
     * Basic contructor of GameControllerImpl.
     * 
     * @param game game
     * @param switcher that changes the Controller in charge
     */
    public GameControllerImpl(final Game game, final ControllerSwitcher switcher) {
        super(switcher);
        this.game = game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameStateInfo getGameState() {
        return this.game.state();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setView(final GameView view) {
        super.setView(view);
        this.game.start(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        this.game.scheduleStop();
        super.close();
    }

}
