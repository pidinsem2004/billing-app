package dev.billing.entities;

public class Price {
    private Zone zoneFrom ;
    private Zone zoneTo ;
    private double price ;

    public Price() {
    }

    public Price(Zone zoneFrom, Zone zoneTo, double price) {
        this.zoneFrom = zoneFrom;
        this.zoneTo = zoneTo;
        this.price = price;
    }

    public Zone getZoneFrom() {
        return zoneFrom;
    }

    public void setZoneFrom(Zone zoneFrom) {
        this.zoneFrom = zoneFrom;
    }

    public Zone getZoneTo() {
        return zoneTo;
    }

    public void setZoneTo(Zone zoneTo) {
        this.zoneTo = zoneTo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Price{" +
                "zoneFrom=" + zoneFrom +
                ", zoneTo=" + zoneTo +
                ", price=" + price +
                '}';
    }
}
