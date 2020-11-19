package io.avec.crud.map;

import com.vaadin.addon.leaflet4vaadin.LeafletMap;
import com.vaadin.addon.leaflet4vaadin.layer.events.MouseEvent;
import com.vaadin.addon.leaflet4vaadin.layer.groups.LayerGroup;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.DefaultMapOptions;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.MapOptions;
import com.vaadin.addon.leaflet4vaadin.layer.ui.marker.Marker;
import com.vaadin.addon.leaflet4vaadin.layer.ui.popup.Popup;
import com.vaadin.addon.leaflet4vaadin.types.LatLng;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.avec.crud.main.MainView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RouteAlias(value = "", layout = MainView.class) // default
@Route(value = "map", layout = MainView.class)
@PageTitle("Map")
@CssImport("./styles/views/map/map-view.css")
public class MapView extends Div {

    private final TextField zoomField = new TextField(null,"Current zoom");
    private final TextField latField = new TextField(null,"Mouse latitude");
    private final TextField lonField = new TextField(null,"Mouse longitude");
    private final TextField divField = new TextField(null,"Testing purposes");

//    private final LayerGroup group1 = new LayerGroup();

    @SneakyThrows
    public MapView() {
        setId("map-view");

        createTop();

        LeafletMap leafletMap = getLeafletMap();

        // event
        leafletMap.onClick(e -> createMarker(e.getLatLng(), leafletMap));

//        Notification.show("Map ready.", 3000, Notification.Position.TOP_CENTER);

        addContextMenu(leafletMap);

    }

    private void addContextMenu(LeafletMap map) {
        ContextMenu contextMenu = new ContextMenu(map);
//        Label message = new Label("-");
        // Components can be used also inside menu items
//        contextMenu.addItem("Remove all markers", e -> get);

        Checkbox checkbox1 = new Checkbox("Layer A");
        contextMenu.addItem(checkbox1, e -> divField.setValue("Clicked on checkbox with value: " + checkbox1.getValue()));

        Checkbox checkbox2 = new Checkbox("Layer B");
        contextMenu.addItem(checkbox2, e -> divField.setValue("Clicked on checkbox with value: " + checkbox2.getValue()));

        // Components can also be added to the overlay
        // without creating menu items with add()
//        Component separator = new Hr();
//        contextMenu.add(separator, new Label("This is not a menu item"));
    }

    private LeafletMap getLeafletMap() {
        MapOptions options = new DefaultMapOptions();
        options.setCenter(new LatLng(59.914800, 10.749178));
        options.setMinZoom(2);
        options.setMaxZoom(18);
        options.setZoom(7);
        zoomField.setValue("Current zoom: " + options.getZoom() + ", min: 2, max: 18");
        LeafletMap map = new LeafletMap(options );
        // OSM: Uses Spherical Mercator projection aka EPSG:3857
        map.setBaseUrl("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
        add(map);



        //map.onClick(e -> createMarker(e.getLatLng(), map));
        map.onZoom(e -> map.getZoom().thenAccept(this::logZoom));
        map.onMouseMove(this::logLatLon);

        return map;
    }

    private void createMarker(LatLng latLng, LeafletMap leafletMap) {
        Marker marker = new Marker(latLng);
        marker.setDraggable(true);
        marker.bindPopup("Popup clicked");
//        marker.onClick(e -> {
//            marker.pop setPopupContent("popup clicked");
////            Notification.show("Marker clicked");
//        });

//        marker.onMove(e -> {
//            logEvent("Lat: " + e.getLatLng().getLat() + ", Lon: " + e.getLatLng().getLng());
//            logEvent2("Marker: " + e.getLatLng());
//        });
//        marker.onMouseUp(e -> Notification.show("onMouseUp.getType() -> " + e, 5000, Notification.Position.BOTTOM_START));
//        marker.onContextMenuOpened(e -> {
//
//        });
        marker.addTo(leafletMap);
    }

    private void createTop() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(latField);
        formLayout.add(lonField);
        formLayout.add(zoomField);
        formLayout.add(divField);
        add(formLayout);
    }

    protected void logLatLon(MouseEvent e) {
        this.latField.setValue("Latitude: " + e.getLatLng().getLat());
        this.lonField.setValue("Longitude: " + e.getLatLng().getLng());
    }

    protected void logZoom(Integer zoom) {
        this.zoomField.setValue("Zoom: " + zoom);
    }

//    protected void logEvent(LeafletEvent leafletEvent) {
//        log.debug("Event -> {}", leafletEvent);
//        this.eventLabel.setText("Event -> " + leafletEvent.getType().name());
//    }
}
