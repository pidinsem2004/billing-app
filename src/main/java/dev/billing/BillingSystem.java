package dev.billing;
import dev.billing.dao.Database;
import dev.billing.entities.Customer;
import dev.billing.entities.Journey;
import dev.billing.entities.Station;
import dev.billing.utilities.Utility;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BillingSystem {

    public static void main(String... args) {
        args = new String[2];
        args[0] = Utility.INPUTFILENAME;
        args[1] = Utility.OUTPUTFILENAME;
        Path p = Paths.get(Utility.INPUTFILENAME);

        /**
         * Test case 7
         * Input file should exist,  if not throw an exception
         * @throws RuntimeException
         */
        if (!inputFileNotExistThrowExceptionImpl(p)) {
            throw new RuntimeException("input File does not exist");
        }


        /**
         * Test case 8
         * Input file should be a txt file,  if not throw an exception
         *
         * @throws RuntimeException
         */

        //System.out.println("file="+ p);
        if (!inputFileExtensionNotTxtThrowExceptionImpl(p.getFileName().toString().trim()))
            throw new RuntimeException("input File is not a txt file ");

        List<Journey> journeyList = retrieveJourneyInformationFromFile(Utility.INPUTFILENAME);


        /**
         * Test case 10
         * If the unixTimestamp in the imput file is not defined, throw exception
         *
         * @throws RuntimeException
         */

        if (unixTimestampIsNegativeOrEqualsToZeroThrowExceptionImpl(journeyList))
            throw new RuntimeException("There is an Incorrect unixTimestamp value");

        /**
         * Test case 11
         * If the customerId is not defined or is not exist or is null, throw exception
         *
         * @throws RuntimeException
         */
        if (customerIdIsNotExistThrowExceptionImpl(journeyList))
            throw new RuntimeException("There is an incorrect customerId");


        /**
         * Test case 12
         * If the station is not defined or is not exist , throw exception
         *
         * @throws RuntimeException
         */
        if (stationIsNotExistThrowExceptionImpl(journeyList))
            throw new RuntimeException("There is an incorrect station");

        /**
         * Test case 13
         * In the journey file each "unixTimestamp"  should correspond to  "customerId" and the "station"
         * otherwise, invalid file data
         *
         * @throws RuntimeException
         */
           /*try (Stream<String> fileStream = Files.lines(p)) {

                List<String> tripInformationsFromFile = fileStream.filter(a ->
                        a.contains("unixTimestamp") ||
                                a.contains("customerId") ||
                                a.contains("station")).collect(Collectors.toList());

                if(!fileDataIsInvalidThrowExceptionImpl(tripInformationsFromFile))
                    throw new RuntimeException("Invalid file data");

            } catch (IOException e) {
                e.printStackTrace();
            }*/
    }

    public static boolean  fileDataIsInvalidThrowExceptionImpl(List<String> tripInformationsFromFile) {

        int unixTimestampNumber = tripInformationsFromFile.stream().filter(a -> a.contains("unixTimestamp")).collect(Collectors.toList()).size();
        int customerIdNumber = tripInformationsFromFile.stream().filter(a -> a.contains("customerId")).collect(Collectors.toList()).size();
        int stationNumber = tripInformationsFromFile.stream().filter(a -> a.contains("station")).collect(Collectors.toList()).size();
        return (unixTimestampNumber == customerIdNumber && customerIdNumber == stationNumber);
    }

    private static boolean stationIsNotExistThrowExceptionImpl(List<Journey> journeyList) {

        Database dao = new Database();
        List <Station> stationList = dao.getStationList();



        List<Integer> result = new ArrayList<Integer>();
        // Extract customerId list information from journey
        List<Journey> distinct_journey = journeyList.stream()
                .filter(Utility.distinctByKey(journey -> journey.getStationName())).collect(Collectors.toList());



        distinct_journey.forEach(o -> {
            //check if there is a not defined or null unix
            int  uniqueStationJourneyList = stationList.stream()
                    .filter(s -> s.getName().equalsIgnoreCase(o.getStationName()))
                    .collect(Collectors.toList()).size();
            if (uniqueStationJourneyList == 0) {
                result.add(1);
            }
        });
        return result.contains(1);
    }

    private static boolean customerIdIsNotExistThrowExceptionImpl(List<Journey> journeyList) {
        Database dao = new Database();
        List<Customer> customerList = dao.getCustomerList();

        List<Integer> result = new ArrayList<Integer>();
        // Extract customerId list information from journey and
        // check if there is undefined customer
        List<Journey> distinct_journey = journeyList.stream()
                .filter(Utility.distinctByKey(journey -> journey.getCustomerId())).collect(Collectors.toList());
        distinct_journey.forEach(o -> {
            if (customerList.stream().filter(i -> i.getId() == o.getCustomerId()).count() == 0)
                result.add(1);
        });
        return result.contains(1);
    }

    private static boolean inputFileExtensionNotTxtThrowExceptionImpl(String fileName) {
        return fileName.endsWith(".txt");
    }

    private static boolean inputFileNotExistThrowExceptionImpl(Path p) {
        return (Files.exists(p));
    }

    private static List<Journey> retrieveJourneyInformationFromFile(String fileName)  {

        Path p = Paths.get(fileName);


        Stream<String> fileStream = null;
        try {
            fileStream = Files.lines(p);
        } catch (IOException e) {
            e.printStackTrace();
        }


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

            aJourney.setStationName(lineData[1].toString());

            tripList.add(aJourney);
        }
        return tripList;

    }

    private static boolean unixTimestampIsNegativeOrEqualsToZeroThrowExceptionImpl(List<Journey> journeyList) {

        List<Integer> result = new ArrayList<Integer>();
        // Extract customerId list information from journey
        List<Journey> distinct_journey = journeyList.stream()
                .filter(Utility.distinctByKey(journey -> journey.getCustomerId())).collect(Collectors.toList());

        distinct_journey.forEach(o -> {
            //check if there is a not defined or null unix
            List<Journey> uniqueCustomerJourneyList = journeyList.stream()
                    .filter(journey -> journey.getCustomerId() == o.getCustomerId()
                            && (journey.getUnixTimestamp() == 0 || journey.getUnixTimestamp() < 0
                            || ("" + journey.getUnixTimestamp()).toString().trim() == ""))
                    .collect(Collectors.toList());
            if (uniqueCustomerJourneyList.size() > 0) {
                result.add(1);
            }
        });
        return result.contains(1);
    }

}






