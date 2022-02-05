package dev.billing.entities;


import java.util.ArrayList;
import java.util.List;

public class Zone {

    private int id;
    private String name;
    private List<Station> stations;


    public Zone() {
        stations = new ArrayList<>();
    }

    public Zone(int id, String name) {
        this.id = id;
        this.name = name;
        stations = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stations=" + stations +
                '}';
    }
}
