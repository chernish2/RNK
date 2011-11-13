package ru.workmap;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 22.10.11
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */
public class GeoPoint {
    public double x, y;

    public void add(GeoPoint gp) {
        x += gp.x;
        y += gp.y;
    }

    public void substract(GeoPoint gp) {
        x -= gp.x;
        y -= gp.y;
    }
}
