package dev.billing.dao;

import dev.billing.entities.*;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Zone> zoneList = new ArrayList<Zone>();
    private List<Customer> customerList = new ArrayList<Customer>();
    private List<Price> priceList = new ArrayList<Price>();
    private List<Station> stationList = new ArrayList<Station>();
    private List<Journey> journeyList = new ArrayList<Journey>();

    public Database() {
        buildData();
    }

    public void buildData (){

        /**
         * stations Initialization
         */
        //zone 1
        Station A  = new Station(1, "A");
        Station B  = new Station(2, "B");

        //zone 2
        Station D  = new Station(4, "D");

        //zone 4
        Station G  = new Station(7, "G");
        Station H  = new Station(8, "H");
        Station I  = new Station(9, "I");

        //zone 2 et zone 3
        Station C  = new Station(3, "C");
        Station E  = new Station(5, "E");

        //zone 3 et zone 4
        Station F  = new Station(6, "F");

        stationList.add(A);
        stationList.add(B);
        stationList.add(C);
        stationList.add(D);
        stationList.add(E);
        stationList.add(F);
        stationList.add(G);
        stationList.add(H);
        stationList.add(I);


        /**
         * zone initialization
         */
        Zone zone1 = new Zone(1, "zone 1");
        zone1.getStations().add(A); zone1.getStations().add(B);

        Zone zone2 = new Zone(2, "zone 2");
        zone2.getStations().add(C); zone2.getStations().add(D); zone2.getStations().add(E);

        Zone zone3 = new Zone(3, "zone 3");
        zone3.getStations().add(C); zone3.getStations().add(E); zone3.getStations().add(F);

        Zone zone4 = new Zone(4, "zone 4");
        zone4.getStations().add(F);zone4.getStations().add(G); zone4.getStations().add(H); zone4.getStations().add(I);



        zoneList.add(zone1);
        zoneList.add(zone2);
        zoneList.add(zone3);
        zoneList.add(zone4);


        /**
         * Customer Initialization
         */
        customerList.add(new Customer(1, "customer1"));
        customerList.add(new Customer(2, "customer2"));
        customerList.add(new Customer(3, "customer3"));
        customerList.add(new Customer(4, "customer4"));


        /**
         * Pricing Initialization
         */

        //From zone1
        priceList.add(new Price(zone1,zone2,240));
        priceList.add(new Price(zone1,zone3,280));
        priceList.add(new Price(zone1,zone4,300));
        //From zone 2
        priceList.add(new Price(zone2,zone3,280));
        priceList.add(new Price(zone2,zone4,300));
        //From zone 3
        priceList.add(new Price(zone3,zone4,200));
        priceList.add(new Price(zone3,zone1,280));
        priceList.add(new Price(zone3,zone2,280));
        //From zone 4
        priceList.add(new Price(zone4,zone1,300));
        priceList.add(new Price(zone4,zone2,300));

        //Adding Journey
        journeyList.add(new Journey(0, customerList.get(0).getId(), A.getName()));
        journeyList.add(new Journey(-10, customerList.get(0).getId(), A.getName()));

    }

    public List<Zone> getZoneList() {
        return zoneList;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public List<Price> getPriceList() {
        return priceList;
    }

    public List<Station> getStationList() {
        return stationList;
    }

    public List<Journey> getJourneyList() {
        return journeyList;
    }


}
