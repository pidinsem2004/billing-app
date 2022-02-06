package dev.billing;

import dev.billing.dao.Database;
import dev.billing.entities.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     */
    public void theZoneNameIsNullOrEmptyThrowExceptionImpl(Zone zone) {
        if (zone.getName() == "" || zone.getName() == null)
            throw new RuntimeException("Ivalid zone name");
    }

    /**
     * Test case 2
     * zone name is unique , otherwise throw exception
     *
     * @throws RuntimeException
     */
    @Test
    public void theZoneNameIsNotUniqueThrowException() {
        exception.expect(RuntimeException.class);
        Zone zone1 = new Zone(5, "zone1");
        Zone zone2 = new Zone(6, "zone1");
        theZoneNameIsNotUniqueThrowExceptionImpl(zone1, zone2);


    }

    /**
     * Implementation of the Test case 2
     * zone name is unique , otherwise throw exception
     *
     * @throws RuntimeException
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
     */
    @Test
    public void zoneWithoutStationThrowException() {
        exception.expect(RuntimeException.class);
        Zone zone1 = new Zone(5, "zone1");
        zoneWithoutStationThrowExceptionImpl(zone1);


    }

    /**
     * Implementation of the Test case 3
     * Zone contains at least one station, otherwise throw exception
     *
     * @throws RuntimeException
     */
    public void zoneWithoutStationThrowExceptionImpl(Zone zone) {
        if (zone.getStations() == null || zone.getStations().size() == 0)
            throw new RuntimeException("zone must contains station");
    }

    /**
     * Test case 4
     * In the pricing zoneFrom should be defined exist, if not throw an exception
     *
     * @throws RuntimeException
     */
    @Test
    public void zoneFromNotDefinedInTheJourneyThrowException() {
        exception.expect(RuntimeException.class);
        Price price = new Price();
        price.setPrice(priceList.get(0).getPrice());
        price.setZoneTo(zoneList.get(0));
        zoneFromNotDefinedInTheJourneyThrowExceptionImpl(price);


    }

    /**
     * Implementation of the Test case 4
     * In the pricing zoneFrom should be defined exist, if not throw an exception
     *
     * @throws RuntimeException
     */
    public void zoneFromNotDefinedInTheJourneyThrowExceptionImpl(Price price) {
        if (price.getZoneFrom() == null)
            throw new RuntimeException("invade pricing rule ");
    }

    /**
     * Test case 5
     * In the pricing zoneTo should be defined and exist, if not throw an exception
     *
     * @throws RuntimeException
     */
    @Test
    public void zoneToNotDefinedInTheJourneyThrowException() {
        exception.expect(RuntimeException.class);
        Price price = new Price();
        price.setPrice(priceList.get(0).getPrice());
        price.setZoneFrom(zoneList.get(0));
        zoneToNotDefinedInTheJourneyThrowExceptionImpl(price);


    }

    /**
     * Implementation of the Test case 5
     * In the pricing zoneTo should be defined and exist, if not throw an exception
     *
     * @throws RuntimeException
     */
    public void zoneToNotDefinedInTheJourneyThrowExceptionImpl(Price price) {
        if (price.getZoneTo() == null)
            throw new RuntimeException("invade pricing rule ");
    }

    /**
     * Test case 6
     * In the pricing rule,  price should be not null and not equal to 0
     *
     * @throws RuntimeException
     */
    @Test
    public void priceIsNullOrZeroThrowException() {
        exception.expect(RuntimeException.class);
        Price price = new Price();
        price.setZoneFrom(zoneList.get(0));
        price.setZoneTo(zoneList.get(0));
        priceIsNullOrZeroThrowExceptionImpl(price);


    }

    /**
     * Implementation of the Test case 6
     * In the pricing rule,  price should be not null and not equal to 0
     *
     * @throws RuntimeException
     */
    public void priceIsNullOrZeroThrowExceptionImpl(Price price) {
        if (price.getPrice() == 0)
            throw new RuntimeException("invade pricing rule ");
    }

    /**
     * Test case 7
     * In the pricing rule,  price should be not positive value
     *
     * @throws RuntimeException
     */
    @Test
    public void priceIsNegativeThrowException() {
        exception.expect(RuntimeException.class);
        Price price = new Price();
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
     */
    public void inputFileExtensionNotTxtThrowExceptionImpl(String fileName) {
        Assertions.assertEquals(true, fileName.endsWith(".txt"));
    }


    /**
     * Test case 9
     * Input file should exist,  if not throw an exception
     */
    @Test
    public void inputFileNotExistThrowException() {
        Path p = Paths.get("D:\\Personnel\\Ingéniance\\Entretien\\Céline\\CandidateInputExample.txt");
        inputFileNotExistThrowExceptionImpl(p);


    }

    /**
     * Implementation of the Test case 9
     * Input file should exist,  if not throw an exception
     */
    public void inputFileNotExistThrowExceptionImpl(Path p) {
        Assertions.assertEquals(true, Files.exists(p));
    }


    /**
     * Test case 10
     * If the unixTimestamp in the imput file is not defined, throw exception
     *
     * @throws RuntimeException
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
     */
    public void unixTimestampIsNegativeOrEqualsToZeroThrowExceptionImpl(Journey journey) {
        //Assertions.assertEquals(0 , journey.getUnixTimestamp());
        if (journey.getUnixTimestamp() == 0)
            throw new RuntimeException("Invalid UnixTimestamp ");
    }


    /**
     * Test case 11
     * If the customerId is not defined or is not exist, throw exception
     *
     * @throws RuntimeException
     */
    @Test
    public void customerIdIsNotExistThrowException() {
        //exception.expect(RuntimeException.class);
        customerIdIsNotExistThrowExceptionImpl(10);


    }

    /**
     * Implementation of the Test case 11
     * If the customerId is not defined or is not exist, throw exception
     *
     * @throws RuntimeException
     */
    public void customerIdIsNotExistThrowExceptionImpl(int customerId) {

        Assertions.assertEquals(0,
                customerList.stream()
                        .filter(s -> s.getId() == customerId).count());
    }

    /**
     * Test case 12
     * If the station is not defined or is not exist , throw exception
     *
     * @throws RuntimeException
     */
    @Test
    public void stationIsNotExistThrowException() {
        exception.expect(RuntimeException.class);
        stationIsNotExistThrowExceptionImpl("Z");


    }

    /**
     * Implementation of the Test case 12
     * If the station is not defined or is not exist , throw exception
     *
     * @throws RuntimeException
     */
    public void stationIsNotExistThrowExceptionImpl(String stationName) {

        if (stationList.stream().filter(s -> s.getName().equalsIgnoreCase(stationName)).collect(Collectors.toList()).size() == 0)
            throw new RuntimeException("Station not defined");
    }

    /**
     * Test case 13
     * In the journey file each "unixTimestamp"  should correspond to  "customerId" and the "station"
     * otherwise, invalid file data
     *
     * @throws RuntimeException
     */
    @Test
    public void fileDataIsInvalidThrowException() {

        Path p = Paths.get("D:\\Personnel\\Ingéniance\\Entretien\\Céline\\CandidateInputExample.txt");
        try (Stream<String> fileStream = Files.lines(p)) {

            List<String> tripInformationsFromFile = fileStream.filter(a ->
                    a.contains("unixTimestamp") ||
                            a.contains("customerId") ||
                            a.contains("station")).collect(Collectors.toList());

            fileDataIsInvalidThrowExceptionImpl(tripInformationsFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementation of the Test case 13
     * In the journey file each "unixTimestamp"  should correspond to  "customerId" and the "station"
     *
     * @throws RuntimeException
     */
    public void fileDataIsInvalidThrowExceptionImpl(List<String> tripInformationsFromFile) {

        int unixTimestampNumber = tripInformationsFromFile.stream().filter(a -> a.contains("unixTimestamp")).collect(Collectors.toList()).size();
        int customerIdNumber = tripInformationsFromFile.stream().filter(a -> a.contains("customerId")).collect(Collectors.toList()).size();
        int stationNumber = tripInformationsFromFile.stream().filter(a -> a.contains("station")).collect(Collectors.toList()).size();
        Assertions.assertEquals(true, unixTimestampNumber == customerIdNumber && customerIdNumber == stationNumber);
    }

}
