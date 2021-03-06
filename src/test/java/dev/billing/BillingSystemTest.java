package dev.billing;

import dev.billing.dao.Database;
import dev.billing.entities.*;
import dev.billing.utilities.Utility;
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
import java.util.Comparator;
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
     * zone should have an id, otherwise throw exception
     *
     * @throws RuntimeException
     */
    @Test
    public void theZoneNameIsNullOrEmptyThrowException() {
        exception.expect(RuntimeException.class);
        Zone Zone = new Zone();
        theZoneNameIsNullOrEmptyThrowExceptionImpl(Zone);
    }
    /**
     * Implementation of the Test case 1
     * If zone name is null or empty, throw exception
     *
     * @throws RuntimeException
     */
    public void theZoneNameIsNullOrEmptyThrowExceptionImpl(Zone zone) {
        if (zone.getId()==0)
            throw new RuntimeException("Ivalid zone id");
    }
    /**
     * Test case 2
     * zone id is unique , otherwise throw exception
     *
     * @throws RuntimeException
     */
    @Test
    public void theZoneNameIsNotUniqueThrowException() {
        exception.expect(RuntimeException.class);
        Zone zone1 = new Zone(6);
        Zone zone2 = new Zone(6);
        theZoneNameIsNotUniqueThrowExceptionImpl(zone1, zone2);
    }
    /**
     * Implementation of the Test case 2
     * zone name is unique , otherwise throw exception
     *
     * @throws RuntimeException
     */
    public void theZoneNameIsNotUniqueThrowExceptionImpl(Zone zone1, Zone zone2) {
        if (zone1.getId() == zone2.getId())
            throw new RuntimeException("zone name must be unique");
    }
    /**
     * Test case 3
     * Zone contains at least one station , otherwise throw exception
     *
     * @throws RuntimeException
     */
    /*@Test
    public void zoneWithoutStationThrowException() {
        exception.expect(RuntimeException.class);
        Zone zone1 = new Zone(5, "zone1");
        zoneWithoutStationThrowExceptionImpl(zone1);
    }*/

    /**
     * Implementation of the Test case 3
     * Zone contains at least one station, otherwise throw exception
     *
     * @throws RuntimeException
     */
   /* public void zoneWithoutStationThrowExceptionImpl(Zone zone) {
        if (zone.getStations() == null || zone.getStations().size() == 0)
            throw new RuntimeException("zone must contains station");
    }*/

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
     * In the pricing rule,  price should be a positive value
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
     * Implementation of the Test case 7
     * In the pricing rule,  price should be a positive value
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
        Path p = Paths.get(Utility.INPUTFILENAME);
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
        Path p = Paths.get(Utility.INPUTFILENAME);
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

        Path p = Paths.get(Utility.INPUTFILENAME);
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

    /**
     * Test case 14
     * Any input tap should correspond to an output tap
     *
     * @throws RuntimeException
     */
    @Test
    public void inputTapWithoutOutputTapThrowException() {

        Path p = Paths.get(Utility.INPUTFILENAME);
        try (Stream<String> fileStream = Files.lines(p)) {

            List<String> tripInformationsFromFile = fileStream.filter(a ->
                    a.contains("unixTimestamp") ||
                            a.contains("customerId") ||
                            a.contains("station")).collect(Collectors.toList());

            //preparing the collection of trips from Data coming to file
            int index = 0;
            Journey aJourney;
            String[] lineData = new String[2];
            String line;
            List<Journey> tripList = new ArrayList<Journey>();

            while (index < tripInformationsFromFile.size()) {
                aJourney = new Journey();

                //get information from the line index %3 and split it
                lineData = tripInformationsFromFile.get(index++).toString().replaceAll(",", "")
                        .replaceAll(" ", "").replaceAll("\"", "").split(":");
                aJourney.setUnixTimestamp(Integer.parseInt(lineData[1]));

                //get information from the line index%3
                lineData = tripInformationsFromFile.get(index++).toString().replaceAll(",", "")
                        .replaceAll(" ", "").replaceAll("\"", "").split(":");
                aJourney.setCustomerId(Integer.parseInt(lineData[1]));


                //get information from the line index%3
                lineData = tripInformationsFromFile.get(index++).toString().replaceAll(",", "")
                        .replaceAll(" ", "").replaceAll("\"", "").split(":");

                aJourney.setStationName(lineData[1]);

                tripList.add(aJourney);
            }

            inputTapWithoutOutputTapThrowExceptionImpl(tripList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementation of the Test case 14
     * Any input tap should correspond to an output tap
     *
     * @throws RuntimeException
     */
    private void inputTapWithoutOutputTapThrowExceptionImpl(List<Journey> tripList) {

        List<Integer> result = new ArrayList<Integer>();
        // Extract customerId list information from journey
        List<Journey> distinct_journey = tripList.stream()
                .filter(Utility.distinctByKey(journey -> journey.getCustomerId())).collect(Collectors.toList());

        distinct_journey.forEach(o -> {
            //Current customer Journey list
            List<Journey> uniqueCustomerJourneyList = tripList.stream()
                    .filter(journey -> journey.getCustomerId() == o.getCustomerId())
                    .sorted(Comparator.comparing(Journey::getUnixTimestamp))
                    .collect(Collectors.toList());
            if (uniqueCustomerJourneyList.size() % 2 != 0) {
                result.add(1);
            }
        });
        Assertions.assertEquals(true, !result.contains(1));
    }
}