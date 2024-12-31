package arcaym.model.game.core.world.impl;

import java.util.Objects;

import arcaym.common.utils.representation.StringRepresentation;
import arcaym.model.game.core.scene.api.GameScene;
import arcaym.model.game.core.score.api.GameScore;
import arcaym.model.game.core.world.api.GameWorld;

/**
 * Basic implementation of {@link GameWorld.Factory}.
 */
public class GameWorldFactory implements GameWorld.Factory {

    /**
     * {@inheritDoc}
     */
    @Override
    public GameWorld ofSceneAndScore(final GameScene scene, final GameScore score) {
        Objects.requireNonNull(scene);
        Objects.requireNonNull(score);
        return new GameWorld() {
            @Override
            public GameScene scene() {
                return scene;
            }
            @Override
            public GameScore score() {
                return score;
            }
            @Override
            public String toString() {
                return StringRepresentation.ofObject(this);
            }
        };
    }
    
}
