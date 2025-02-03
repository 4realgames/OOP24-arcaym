package arcaym.controller.game.impl;

import arcaym.controller.app.api.ControllerSwitcher;
import arcaym.controller.app.impl.AbstractController;
import arcaym.controller.game.api.ExtendedGameController;
import arcaym.model.game.core.engine.api.Game;
import arcaym.model.game.core.engine.api.GameStateInfo;
import arcaym.view.game.api.GameView;

/**
 * Implementation of {@link GameController} that extends
 * {@link AbstractController} of {@link GameView}.
 */
public class GameControllerImpl extends AbstractController<GameView> implements ExtendedGameController {
    private final Game game;

    /**
     * Basic contructor of GameControllerImpl.
     * 
     * @param switcher that changes the Controller in charge
     * @param game game
     */
    public GameControllerImpl(final ControllerSwitcher switcher, final Game game) {
        super(switcher);
        this.game = game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        this.game.start(this.view());
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
    public void close() {
        super.close();
        this.game.scheduleStop();
    }
}
