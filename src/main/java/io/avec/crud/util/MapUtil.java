package io.avec.crud.util;

import io.avec.crud.util.map.SphericalMercator;
import org.javatuples.Pair;

public class MapUtil {

    private static final SphericalMercator mercator = new SphericalMercator();

    public static Pair<Double, Double> getSphericalMercatorProjection(double x, double y) {
        double lat = mercator.yAxisProjection(y);
        double lon = mercator.xAxisProjection(x);
        return new Pair<>(lat, lon);
    }
}
