package dev.billing;

import dev.billing.dao.Database;
import dev.billing.entities.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * Implementation of the Test case 2
     * zone name is unique , otherwise throw exception
     *
     * @throws RuntimeException
     *
     */
    public void theZoneNameIsNotUniqueThrowExceptionImpl(Zone zone1, Zone zone2) {
         if (zone1.getName().equalsIgnoreCase(zone2.getName()))
            throw new RuntimeException("zone name must be unique");
    }

    /**
     * Test case 3
     * Zone contains at least one station , otherwise throw exception
     *
     * @throws RuntimeException
     *
     */
    @Test
    public void zoneWithoutStationThrowException() {
        exception.expect(RuntimeException.class);
        Zone zone1= new Zone(5, "zone1");
       zoneWithoutStationThrowExceptionImpl(zone1);


    }
    /**
     * Implementation of the Test case 3
     * Zone contains at least one station, otherwise throw exception
     *
     * @throws RuntimeException
     *
     */
    public void zoneWithoutStationThrowExceptionImpl(Zone zone) {
       if (zone.getStations()==null || zone.getStations().size()==0)
            throw new RuntimeException("zone must contains station");
    }

    /**
     * Test case 4
     * In the pricing zoneFrom should be defined exist, if not throw an exception
     *
     * @throws RuntimeException
     *
     */
    @Test
    public void zoneFromNotDefinedInTheJourneyThrowException() {
        exception.expect(RuntimeException.class);
        Price price= new Price();
        price.setPrice(priceList.get(0).getPrice());
        price.setZoneTo(zoneList.get(0));
        zoneFromNotDefinedInTheJourneyThrowExceptionImpl(price);


    }
    /**
     * Implementation of the Test case 4
     * In the pricing zoneFrom should be defined exist, if not throw an exception
     *
     * @throws RuntimeException
     *
     */
    public void zoneFromNotDefinedInTheJourneyThrowExceptionImpl(Price price) {
         if (price.getZoneFrom()==null)
            throw new RuntimeException("invade pricing rule ");
    }
    /**
     * Test case 5
     * In the pricing zoneTo should be defined and exist, if not throw an exception
     *
     * @throws RuntimeException
     *
     */
    @Test
    public void zoneToNotDefinedInTheJourneyThrowException() {
        exception.expect(RuntimeException.class);
        Price price= new Price();
        price.setPrice(priceList.get(0).getPrice());
        price.setZoneFrom(zoneList.get(0));
        zoneToNotDefinedInTheJourneyThrowExceptionImpl(price);


    }
    /**
     * Implementation of the Test case 5
     * In the pricing zoneTo should be defined and exist, if not throw an exception
     *
     * @throws RuntimeException
     *
     */
    public void zoneToNotDefinedInTheJourneyThrowExceptionImpl(Price price) {
        if (price.getZoneTo()==null)
            throw new RuntimeException("invade pricing rule ");
    }
    /**
     * Test case 6
     * In the pricing rule,  price should be not null and not equal to 0
     *
     * @throws RuntimeException
     *
     */
    @Test
    public void priceIsNullOrZeroThrowException() {
        exception.expect(RuntimeException.class);
        Price price= new Price();
        price.setZoneFrom(zoneList.get(0));
        price.setZoneTo(zoneList.get(0));
        priceIsNullOrZeroThrowExceptionImpl(price);


    }
    /**
     * Implementation of the Test case 6
     * In the pricing rule,  price should be not null and not equal to 0
     *
     * @throws RuntimeException
     *
     */
    public void priceIsNullOrZeroThrowExceptionImpl(Price price) {
        if (price.getPrice()==0)
            throw new RuntimeException("invade pricing rule ");
    }

    /**
     * Test case 7
     * In the pricing rule,  price should be not positive value
     *
     * @throws RuntimeException
     *
     */
    @Test
    public void priceIsNegativeThrowException() {
        exception.expect(RuntimeException.class);
        Price price= new Price();
        price.setZoneFrom(zoneList.get(0));
        price.setZoneTo(zoneList.get(0));
        price.setPrice(-10);
        priceIsNegativeThrowExceptionImpl(price);


    }
    /**
     * Implementation of the Test case 6
     * In the pricing rule,  price should be not positive value
     *
     * @throws RuntimeException
     *
     */
    public void priceIsNegativeThrowExceptionImpl(Price price) {
        if (price.getPrice() < 0)
            throw new RuntimeException("invade price in the rule ");
    }

    /**
     * Test case 8
     * Input file should be a txt file,  if not throw an exception
     *
     * @throws RuntimeException
     *
     */
    @Test
    public void inputFileExtensionNotTxtThrowException() {
         Path p = Paths.get("D:\\Personnel\\Ingéniance\\Entretien\\Céline\\CandidateInputExample.txt");
        inputFileExtensionNotTxtThrowExceptionImpl(p.getFileName().toString().trim());


    }
    /**
     * Implementation of the Test case 8
     * Input file should be a txt file,  if not throw an exception
     *
     * @throws RuntimeException
     *
     */
    public void inputFileExtensionNotTxtThrowExceptionImpl(String fileName ) {
         Assertions.assertEquals(true ,fileName.endsWith(".txt"));
    }


    /**
     * Test case 9
     * Input file should exist,  if not throw an exception
     *
     *
     *
     */
    @Test
    public void inputFileNotExistThrowException() {
        Path p = Paths.get("D:\\Personnel\\Ingéniance\\Entretien\\Céline\\CandidateInputExample.txt");
        inputFileNotExistThrowExceptionImpl(p);


    }
    /**
     * Implementation of the Test case 9
     * Input file should exist,  if not throw an exception
     *
     *
     *
     */
    public void inputFileNotExistThrowExceptionImpl(Path p ) {
        Assertions.assertEquals(true , Files.exists(p));
    }



    /**
     * Test case 10
     * If the unixTimestamp in the imput file is not defined, throw exception
     *
     * @throws RuntimeException
     *
     */
    @Test
    public void unixTimestampIsNegativeOrEqualsToZeroThrowException() {
        exception.expect(RuntimeException.class);
        unixTimestampIsNegativeOrEqualsToZeroThrowExceptionImpl(journeyList.get(0));


    }
    /**
     * Implementation of the Test case 10
     * If the unixTimestamp in the imput file is not defined, throw exception
     *
     * @throws RuntimeException
     *
     */
    public void unixTimestampIsNegativeOrEqualsToZeroThrowExceptionImpl(Journey journey) {
        //Assertions.assertEquals(0 , journey.getUnixTimestamp());
        if(journey.getUnixTimestamp()==0)
            throw new RuntimeException("Invalid UnixTimestamp ");
    }



}
