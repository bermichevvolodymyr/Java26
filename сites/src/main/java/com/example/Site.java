package com.example;
import javafx.beans.property.*;

public class Site {
    private IntegerProperty id;
    private StringProperty name;
    private DoubleProperty latitude;
    private DoubleProperty longitude;
    private StringProperty region;
    private StringProperty image;

    public Site(int id, String name, double latitude, double longitude, String region, String image) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.latitude = new SimpleDoubleProperty(latitude);
        this.longitude = new SimpleDoubleProperty(longitude);
        this.region = new SimpleStringProperty(region);
        this.image = new SimpleStringProperty(image);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public double getLatitude() { return latitude.get(); }
    public DoubleProperty latitudeProperty() { return latitude; }
    public double getLongitude() { return longitude.get(); }
    public DoubleProperty longitudeProperty() { return longitude; }
    public String getRegion() { return region.get(); }
    public StringProperty regionProperty() { return region; }
    public String getImage() { return image.get(); }
    public StringProperty imageProperty() { return image; }
}