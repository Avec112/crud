package io.avec.crud.util.map;


public abstract class AbstractMercator {
    final static double RADIUS_MAJOR = 6378137.0;
    final static double RADIUS_MINOR = 6356752.3142;

    public abstract double yAxisProjection(double input);
    public abstract double xAxisProjection(double input);
}
