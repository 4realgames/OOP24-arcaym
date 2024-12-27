package arcaym.common.point.api;

/**
 * Interface for a 2D point in space.
 */
public interface Point2D {

    /**
     * First coordinate.
     * @return value of the coordinate
     */
    int x();

    /**
     * Second coordinate.
     * @return value of the coordinate
     */
    int y();

    /**
     * Interface for a {@link Point2D} factory.
     */
    interface Factory {

        /**
         * Create point form coordinates.
         * @param x first coordinate
         * @param y second coordinate
         * @return resulting point
         */
        Point2D ofCoordinates(int x, int y);

        /**
         * Sum two points coordinate-by-coordinate.
         * @param p1 first point
         * @param p2 second point
         * @return resulting point
         */
        Point2D sum(Point2D p1, Point2D p2);

    }

}