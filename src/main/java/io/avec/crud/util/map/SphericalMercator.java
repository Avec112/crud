package io.avec.crud.util.map;

// source https://www.baeldung.com/java-convert-latitude-longitude
public class SphericalMercator extends AbstractMercator {

    @Override
    public double yAxisProjection(double input) {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(input) / 2)) * RADIUS_MAJOR;
    }

    @Override
    public double xAxisProjection(double input) {
        return Math.toRadians(input) * RADIUS_MAJOR;
    }
}
