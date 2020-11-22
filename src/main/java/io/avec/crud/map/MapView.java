package io.avec.crud.map;

import com.vaadin.addon.leaflet4vaadin.LeafletMap;
import com.vaadin.addon.leaflet4vaadin.layer.events.MouseEvent;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.DefaultMapOptions;
import com.vaadin.addon.leaflet4vaadin.layer.map.options.MapOptions;
import com.vaadin.addon.leaflet4vaadin.layer.ui.marker.Marker;
import com.vaadin.addon.leaflet4vaadin.plugins.heatmap.HeatLayer;
import com.vaadin.addon.leaflet4vaadin.plugins.heatmap.HeatLayerOptions;
import com.vaadin.addon.leaflet4vaadin.types.LatLng;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.avec.crud.main.MainView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

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
    private LeafletMap map;
    private final Button osloButton = new Button("Oslo");
    private final Button hamburgButton = new Button("Hamburg");
    private final Button rioButton = new Button("Rio de Janeiro");
    private final LatLng oslo = new LatLng(59.914800, 10.749178);
    private final LatLng hamburg = new LatLng(53.51418452077113, 10.04150390625);
    private final LatLng rio = new LatLng(-22.948276856880895, -43.1982421875);
    private final Checkbox heatLayerCheckbox = new Checkbox("Heatmap (random points)");
    private HeatLayer heatLayer;
//    private final LayerGroup group1 = new LayerGroup();

    @SneakyThrows
    public MapView() {
        setId("map-view");

        createTop();
        map = getLeafletMap();
        // event
        map.onClick(e -> createMarker(e.getLatLng(), map));

//        Notification.show("Map ready.", 3000, Notification.Position.TOP_CENTER);

        addContextMenu(map);

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
        options.setCenter(oslo);
        options.setMinZoom(2);
        options.setMaxZoom(18);
        options.setZoom(7);
        zoomField.setValue("Current zoom: " + options.getZoom() + ", min: 2, max: 18");
        map = new LeafletMap(options );
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
//        marker.onDoubleClick(this::removeMarker); // todo does not work when onClick displays Dialog
        //marker.onMove(e -> {
//            e.getTarget().setPopupContent(formatLatLngOnTwoLines(e.getLatLng()));
//            marker.setTooltipContent(formatLatLngOnTwoLines(e.getLatLng()));
//        });
        marker.addTo(leafletMap);
        //removeMarker(marker);
    }

    private void createHeatLayer(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> e) {
        if(e.getValue()) {
            HeatLayerOptions options = new HeatLayerOptions();
//            options.setMinOpacity(1); // 0,5
//            options.setMax(1);
            options.setMaxZoom(18);
//            options.setBlur(15);
            heatLayer = new HeatLayer(options);
            heatLayer.setLatLngs(randomLatLngs());
            /*heatLayer.setLatLngs(Arrays.asList(
                    oslo,
                    new LatLng(59.931280289726914, 10.71922302246094), // Frogner i Oslo
                    new LatLng(59.83274004661259, 10.438385009765627), // Asker
                    new LatLng(59.7377140398859, 10.210418701171877), // Drammen
                    hamburg,
                    rio
            ));*/
//            map.onMove(event -> heatLayer.redraw());
            heatLayer.addTo(map);
            Notification.show("Heatmap layer added.");
        } else {
            heatLayer.remove();
            Notification.show("Heatmap layer removed.");
        }
    }

    private static List<LatLng> randomLatLngs() {
        List<LatLng> heatmapData = new ArrayList<>();

        // South Norway
        for (int i = 0; i++ < 10;) {
            double altitude = Math.random();
            double lat = (Math.random() * 4) + 58;
            double lng = (Math.random() * 4) + 9;
            heatmapData.add(LatLng.latlng(lat, lng, altitude));
        }

        // Germany
        for (int i = 0; i++ < 6;) {
            double altitude = Math.random();
            double lat = (Math.random() * 4) + 51;
            double lng = (Math.random() * 3) + 6;
            heatmapData.add(LatLng.latlng(lat, lng, altitude));
        }

        // France
        for (int i = 0; i++ < 7;) {
            double altitude = Math.random();
            double lat = (Math.random() * 4) + 47;
            double lng = (Math.random() * 4) + 3;
            heatmapData.add(LatLng.latlng(lat, lng, altitude));
        }


        return heatmapData;
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

        // add components
        formLayout.add(textField, textField2, lat, lng);

        Button save = new Button("Save", e -> {
            Notification.show("\"Marker saved\" (not really)");
            dialog.close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button close = new Button("Cancel", e -> dialog.close());
        Button delete = new Button("Remove marker", new Icon(VaadinIcon.TRASH), e -> {
            Notification.show("Marker remove.");
            mouseEvent.getTarget().remove(); // remove marker
            dialog.close();
        });
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout horizontalLayout = new HorizontalLayout(save, delete, close);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
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
        formLayout.add(latField, lonField);
        formLayout.add(zoomField, divField);

        HorizontalLayout buttonsLayout = new HorizontalLayout(new Label("Fly to"), osloButton, hamburgButton, rioButton);
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout heatLayerLayout = new HorizontalLayout(heatLayerCheckbox);

        // fly to TODO flight seems to be off after a while
        osloButton.addClickListener(e -> map.flyTo(oslo));
        hamburgButton.addClickListener(e -> map.flyTo(hamburg));
        rioButton.addClickListener(e -> map.flyTo(rio));
        heatLayerCheckbox.addValueChangeListener(this::createHeatLayer);

        VerticalLayout top = new VerticalLayout(formLayout, buttonsLayout, heatLayerLayout);
        add(top);
    }

    protected void logLatLon(MouseEvent e) {
        this.latField.setValue("Latitude: " + e.getLatLng().getLat());
        this.lonField.setValue("Longitude: " + e.getLatLng().getLng());
    }

    protected void logZoom(Integer zoom) {
        this.zoomField.setValue("Zoom: " + zoom);
    }
}
