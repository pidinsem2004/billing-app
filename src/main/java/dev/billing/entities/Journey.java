package dev.billing.entities;

import java.util.stream.Stream;

public class Journey {
    private int unixTimestamp  ;
    private int customerId ;
    private String stationName ;

    public Journey() {
    }

    public Journey(int unixTimestamp, int customerId, String stationName) {
        this.unixTimestamp = unixTimestamp;
        this.customerId = customerId;
        this.stationName = stationName;
    }

    public int getUnixTimestamp() {
        return unixTimestamp;
    }

    public void setUnixTimestamp(int unixTimestamp) {
        this.unixTimestamp = unixTimestamp;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "unixTimestamp=" + unixTimestamp +
                ", customerId=" + customerId +
                ", station=" + stationName +
                '}';
    }
}
