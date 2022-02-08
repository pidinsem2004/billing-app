package dev.billing.dao;

import dev.billing.entities.*;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private Station A;
    private Station B;
    private Station C;
    private Station D;
    private Station E;
    private Station F;
    private Station G;
    private Station H;
    private Station I;

    private Zone zone1;
    private Zone zone2;
    private Zone zone3;
    private Zone zone4;

    public static List<Station> stationList = new ArrayList<>();
    public static List<Zone> zoneList = new ArrayList<>();
    public static List<Price> priceList = new ArrayList<>();
    public static List<Customer> customerList = new ArrayList<>();
    public static List<Journey> journeyList = new ArrayList<>();

    public void init() {
        /**
         * stations Initialization
         */
        stationList = new ArrayList<>();
        Station A = new Station("A");A.setZones(new ArrayList<>()) ;
        Station B = new Station("B");B.setZones(new ArrayList<>()) ;
        Station C = new Station("C");C.setZones(new ArrayList<>()) ;
        Station D = new Station("D");D.setZones(new ArrayList<>()) ;
        Station E = new Station("E");E.setZones(new ArrayList<>()) ;
        Station F = new Station("F");F.setZones(new ArrayList<>()) ;
        Station G = new Station("G");G.setZones(new ArrayList<>()) ;
        Station H = new Station("H");H.setZones(new ArrayList<>()) ;
        Station I = new Station("I");I.setZones(new ArrayList<>()) ;

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
        //Zone 1 = A, B
        Zone zone1 = new Zone(1);
        A.getZones().add(zone1);
        B.getZones().add(zone1);

        //Zone 2 = C,D,E
        Zone zone2 = new Zone(2);
        C.getZones().add(zone2);
        D.getZones().add(zone2);
        E.getZones().add(zone2);

        //Zone 3 = C,E,F
        Zone zone3 = new Zone(3);

        C.getZones().add(zone3);
        E.getZones().add(zone3);
        F.getZones().add(zone3);

        //Zone 4 = F, G, H, I
        Zone zone4 = new Zone(4);

        F.getZones().add(zone4);
        G.getZones().add(zone4);
        H.getZones().add(zone4);
        I.getZones().add(zone4);

        zoneList = new ArrayList<>();
        zoneList.add(zone1);
        zoneList.add(zone2);
        zoneList.add(zone3);
        zoneList.add(zone4);

        /**
         * Pricing Initialization
         */
        priceList = new ArrayList<>();

        //From zone1
        priceList.add(new Price(zone1, zone1, 200));
        priceList.add(new Price(zone1, zone2, 240));
        priceList.add(new Price(zone2, zone1, 240));

        priceList.add(new Price(zone1, zone3, 280));
        priceList.add(new Price(zone3, zone1, 280));

        priceList.add(new Price(zone1, zone4, 300));
        priceList.add(new Price(zone4, zone1, 300));

        //From zone 2
        priceList.add(new Price(zone2, zone2, 200));
        priceList.add(new Price(zone2, zone3, 280));
        priceList.add(new Price(zone3, zone2, 280));
        priceList.add(new Price(zone2, zone4, 300));
        priceList.add(new Price(zone4, zone2, 300));


        //From zone 3
        priceList.add(new Price(zone3, zone3, 200));
        priceList.add(new Price(zone3, zone4, 200));
        priceList.add(new Price(zone3, zone1, 280));
        priceList.add(new Price(zone3, zone2, 280));

        priceList.add(new Price(zone4, zone3, 200));
        priceList.add(new Price(zone1, zone3, 280));
        priceList.add(new Price(zone2, zone3, 280));

        //From zone 4
        priceList.add(new Price(zone4, zone4, 200));
        priceList.add(new Price(zone4, zone1, 300));
        priceList.add(new Price(zone4, zone2, 300));
        priceList.add(new Price(zone1, zone4, 300));
        priceList.add(new Price(zone2, zone4, 300));

        /**
         * Customer Initialization
         */
        customerList = new ArrayList<>();
        customerList.add(new Customer(1));
        customerList.add(new Customer(2));
        customerList.add(new Customer(3));
        customerList.add(new Customer(4));

        journeyList = new ArrayList<>();
        //Adding Journey
        journeyList.add(new Journey(0, customerList.get(0).getId(), A.getName()));
        journeyList.add(new Journey(-10, customerList.get(0).getId(), A.getName()));

    }


    public static List<Station> getStationList() {
        return stationList;
    }

    public List<Zone> getZoneList() {
        return zoneList;
    }

    public static List<Price> getPriceList() {
        return priceList;
    }

    public static List<Customer> getCustomerList() {
        return customerList;
    }

    public static List<Journey> getJourneyList() {
        return journeyList;
    }
}
