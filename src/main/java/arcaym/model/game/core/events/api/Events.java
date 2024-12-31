package arcaym.model.game.core.events.api;

import java.util.function.Consumer;

/**
 * Group of interfaces for events management.
 */
public interface Events {

    /**
     * Interface for an events manager.
     * 
     * @param <T> events type
     */
    interface Manager<T> extends
        Scheduler<T>,
        Subscriber<T>,
        Notifier<T> { }

    /**
     * Interface for an events scheduler.
     * 
     * @param <T> events type
     */
    interface Scheduler<T> {

        /**
         * Register the happening of an event for future processing.
         * 
         * @param event event
         */
        void scheduleEvent(T event);

    }

    /**
     * Interface for an events subscriber.
     * 
     * @param <T> events type
     */
    interface Subscriber<T> {

        /**
         * Subscribe callback to the happening of an event.
         * 
         * @param event event
         * @param callback event consumer
         */
        void registerCallback(T event, Consumer<T> callback);

    }

    /**
     * Interface for an events notifier.
     * 
     * @param <T> events type
     */
    interface Notifier<T> {

        /**
         * Notify all observers of all pending events.
         */
        void notifyObservers();

    }

    /*
    Due to generic type ereasure, you need separate observer interfaces for each type to
    be able to implement both on the same type.
    */

    /**
     * Interface for a game events observer.
     */
    interface GameEventObserver {

        /**
         * Register all game events callbacks to subscriber.
         * 
         * @param eventsSubscriber game events subscriber
         */
        void registerGameEventsCallbacks(Subscriber<GameEvent> eventsSubscriber);

    }

    /**
     * Interface for a input events observer.
     */
    interface InputEventObserver {

        /**
         * Register all input events callbacks to subscriber.
         * 
         * @param eventsSubscriber input events subscriber
         */
        void registerInputEventsCallbacks(Subscriber<InputEvent> eventsSubscriber);

    }

}