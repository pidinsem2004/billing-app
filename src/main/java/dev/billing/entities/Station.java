package dev.billing.entities;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private String name;
    private List<Zone> zones;


    public Station() {

    }

    public Station(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", zones=" + zones +
                '}';
    }
}
