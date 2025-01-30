package arcaym.view.core.api;

import java.awt.Dimension;

import arcaym.common.geometry.impl.Point;

/**
 * Interface for window attributes.
 */
public interface WindowInfo {

    /**
     * Default window size used as reference for relative calculations.
     */
    Dimension STANDARD_SIZE = new Dimension(1920, 1080);

    /**
     * Get window size.
     * 
     * @return size value
     */
    Dimension size();

    /**
     * Get ratio between window and default size.
     * 
     * @return ratio value
     */
    Point ratio();

}
