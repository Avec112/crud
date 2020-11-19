package io.avec.crud.util;

import com.vaadin.addon.leaflet4vaadin.types.LatLng;
import org.assertj.core.api.Assert;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapUtilTest {

    @Test
    void getSphericalMercatorProjection() {

        Pair<Double, Double> latLon = MapUtil.getSphericalMercatorProjection(22, 44);

        assertEquals(5465442.183322753, latLon.getValue0()); // y
        assertEquals(2449028.7974520186, latLon.getValue1()); // x
    }
}