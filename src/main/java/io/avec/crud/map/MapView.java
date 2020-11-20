package io.avec.crud.map;

import com.vaadin.addon.leaflet4vaadin.LeafletMap;
import com.vaadin.addon.leaflet4vaadin.layer.events.MouseEvent;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.DefaultMapOptions;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.MapOptions;
import com.vaadin.addon.leaflet4vaadin.layer.ui.marker.Marker;
import com.vaadin.addon.leaflet4vaadin.types.LatLng;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
    private final Button testButton = new Button("Button");
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
        contextMenu.addItem(checkbox1, e -> divField.setValue("Checkbox A value: " + checkbox1.getValue()));

        Checkbox checkbox2 = new Checkbox("Layer B");
        contextMenu.addItem(checkbox2, e -> divField.setValue("Checkbox B value: " + checkbox2.getValue()));

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
//        marker.bindPopup(formatLatLngOnTwoLines(latLng));
//        marker.bindTooltip("Tooltip text");
//        marker.setTitle("Marker title");
        marker.onClick(this::createPoint); // does not work
//        marker.onClick(event -> event.getTarget().remove()); // works
        //            Dialog dialog = new Dialog();
        //            Button remove = new Button("Remove");
        //            remove.addClickListener(e -> {
        //                event.getTarget().remove();
        //                dialog.close();
        //            });
        //            Button cancel = new Button("Cancel");
        //            cancel.addClickListener(e -> {
        //                dialog.close();
        //            });
        //            dialog.add(remove, cancel);
        //            dialog.open();
        marker.onDoubleClick(this::removeMarker);
        marker.onMove(e -> {
//            e.getTarget().setPopupContent(formatLatLngOnTwoLines(e.getLatLng()));
            marker.setTooltipContent(formatLatLngOnTwoLines(e.getLatLng()));
        });
        marker.addTo(leafletMap);
        //removeMarker(marker);
    }

    private void createPoint(MouseEvent mouseEvent) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        FormLayout formLayout = new FormLayout();
        TextField textField = new TextField("Location");
        TextField textField2 = new TextField("Description");
        TextField lat = new TextField("Latitude");
        lat.setReadOnly(true);
        lat.setValue(mouseEvent.getLatLng().getLat().toString());
        TextField lng = new TextField("Longitude");
        lng.setReadOnly(true);
        lng.setValue(mouseEvent.getLatLng().getLng().toString());
        Button save = new Button("Save", e -> {
            Notification.show("\"Lagrer punkt\" (kan tenkes)");
            dialog.close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button close = new Button("Close", e -> dialog.close());
        Button delete = new Button("Delete", new Icon(VaadinIcon.TRASH), e -> {
            Notification.show("Marker deleted.");
            dialog.close();
        });
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        formLayout.add(textField, textField2, lat, lng, delete);
        HorizontalLayout horizontalLayout = new HorizontalLayout(save, close);
        //horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        dialog.add(formLayout, horizontalLayout);
        dialog.open();
    }

    private String formatLatLngOnTwoLines(LatLng latLng) {
        return "Lat: " + latLng.getLat() + "\nLng: " + latLng.getLng();
    }

    private void removeMarker(MouseEvent mouseEvent) {
        mouseEvent.getTarget().remove();
//        VerticalLayout verticalLayout = new VerticalLayout();
//        verticalLayout.setClassName("marker-dialog");
//        verticalLayout.add(new Label("Remove marker?"));
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        verticalLayout.add(horizontalLayout);
//        Dialog dialog = new Dialog();
//        Button confirm = new Button("Confirm", e -> {
//            mouseEvent.getTarget().remove();
//            dialog.close();
//        });
//        Button cancel = new Button("Cancel", e -> dialog.close());
//        horizontalLayout.add(confirm, cancel);
//        dialog.add(verticalLayout);
//        dialog.open();
    }

    private void createTop() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(latField);
        formLayout.add(lonField);
        formLayout.add(zoomField);
        formLayout.add(divField);
        formLayout.add(testButton);

        testButton.addClickListener(event -> {
//            this.removeMarker(null);
//            log.debug("testButton clicked");
            Notification.show("Button clicked.");
        });
        add(formLayout);
    }

    protected void logLatLon(MouseEvent e) {
        this.latField.setValue("Latitude: " + e.getLatLng().getLat());
        this.lonField.setValue("Longitude: " + e.getLatLng().getLng());
    }

    protected void logZoom(Integer zoom) {
        this.zoomField.setValue("Zoom: " + zoom);
    }
}
