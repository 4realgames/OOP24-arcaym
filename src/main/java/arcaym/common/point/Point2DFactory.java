package arcaym.common.point;

/**
 * Basic implementation of {@link Point2D.Factory}.
 */
public class Point2DFactory implements Point2D.Factory {

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2D ofCoordinates(final int x, final int y) {
        return new Point2D() {
            @Override
            public int x() {
                return x;
            }
            @Override
            public int y() {
                return y;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2D sum(final Point2D p1, final Point2D p2) {
        return this.ofCoordinates(p1.x() + p2.x(), p1.y() + p2.y());
    }

}