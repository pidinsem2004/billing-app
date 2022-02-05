package dev.billing;

import dev.billing.dao.Database;
import dev.billing.entities.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

public class BillingSystemTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private List<Customer> customerList = new ArrayList<Customer>();
    private List<Journey> journeyList = new ArrayList<Journey>();
    private List<Price> priceList = new ArrayList<Price>();
    private List<Station> stationList = new ArrayList<Station>();
    private List<Zone> zoneList = new ArrayList<Zone>();



    @Before
    public final void before() {
        Database dao = new Database();
        customerList = dao.getCustomerList();
        journeyList = dao.getJourneyList();
        priceList = dao.getPriceList();
        stationList = dao.getStationList();
        zoneList = dao.getZoneList();
    }


    /**
     * Test case 1
     * zone should have a name, otherwise throw exception
     *
     * @throws RuntimeException
     *
     */
    @Test
    public void theZoneNameIsNullOrEmptyThrowException() {
        exception.expect(RuntimeException.class);
        Zone Zone = new Zone(5, "");
        theZoneNameIsNullOrEmptyThrowExceptionImpl(Zone);


    }
    /**
     * Implementation of the Test case 1
     * If zone name is null or empty, throw exception
     *
     * @throws RuntimeException
     *
     */
    public void theZoneNameIsNullOrEmptyThrowExceptionImpl(Zone zone) {
        if (zone.getName() == "" || zone.getName()==null)
            throw new RuntimeException("Ivalid zone name");
    }
    /**
     * Test case 2
     * zone name is unique , otherwise throw exception
     *
     * @throws RuntimeException
     *
     */
    @Test
    public void theZoneNameIsNotUniqueThrowException() {
        exception.expect(RuntimeException.class);
        Zone zone1= new Zone(5, "zone1");
        Zone zone2 = new Zone(6, "zone1");
        theZoneNameIsNotUniqueThrowExceptionImpl(zone1, zone2);


    }
    /**
     * Implementation of the Test case 1
     * zone name is unique , otherwise throw exception
     *
     * @throws RuntimeException
     *
     */
    public void theZoneNameIsNotUniqueThrowExceptionImpl(Zone zone1, Zone zone2) {
         if (zone1.getName().equalsIgnoreCase(zone2.getName()))
            throw new RuntimeException("zone name must be unique");
    }
}
