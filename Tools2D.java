package graphic;

import java.awt.geom.Point2D;

public class Tools2D{
	public static double area2(Point a, Point b, Point c) {
        return (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
    }
}
