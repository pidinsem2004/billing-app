package dev.billing.entities;


import java.util.ArrayList;
import java.util.List;

public class Zone {
    private int id;

   public Zone() {

    }

    public Zone(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                '}';
    }
}
