package dev.billing;

import dev.billing.dao.Database;
import dev.billing.entities.*;
import dev.billing.utilities.Utility;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BillingSystem {
    private BufferedWriter writer;


    public static void main(String... args) {
        args = new String[2];
        args[0] = Utility.INPUTFILENAME;
        args[1] = Utility.OUTPUTFILENAME;
        Path p = Paths.get(Utility.INPUTFILENAME);

        //Init the Database
        new Database().init();




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
        /**
         * Test case 13
         * In the journey file each "unixTimestamp"  should correspond to  "customerId" and the "station"
         * otherwise, invalid file data
         *
         * @throws RuntimeException
         */
        try (Stream<String> fileStream = Files.lines(p)) {

            List<String> tripInformationsFromFile = fileStream.filter(a ->
                    a.contains("unixTimestamp") ||
                            a.contains("customerId") ||
                            a.contains("station")).collect(Collectors.toList());
            tripInformationsFromFile.stream().forEach(System.out::println);

            if (!fileDataIsInvalidThrowExceptionImpl(tripInformationsFromFile))
                throw new RuntimeException("Invalid file data");

        } catch (IOException e) {
            e.printStackTrace();
        }
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
         * Implementation of the Test case 14
         * Any input tap should correspond to an output tap
         *
         * @throws RuntimeException
         */

        if (inputTapWithoutOutputTapThrowExceptionImpl(journeyList))
            throw new RuntimeException("There is an input tap without an output tap in the input file");
        //We can run the billing system


        boolean isValid = true;
        if (!isValidData(journeyList) )
            throw new RuntimeException("invalid zone from to zone to, Price not defined");


        runBilling(journeyList);

    }

    private static void runBilling(List<Journey> journeyList) {


        List<String> tripList  = new ArrayList<>();
        List<String> tripDetailList  = new ArrayList<>();

        List<String> tripHeaderList = new ArrayList<>();
        List<String> tripFooterList = new ArrayList<>();
        tripHeaderList.add("{\n");
        tripHeaderList.add(" \"customerSummaries\" : [ {\n");

        tripList.addAll(tripHeaderList);
        tripHeaderList.clear();



        List<Journey> distinct_journey = journeyList.stream()
                .filter(Utility.distinctByKey(journey -> journey.getCustomerId())).collect(Collectors.toList());


        for (int k = 0; k < distinct_journey.size(); k++) {

            int jouney_index = k;


            //bill the selected customer

            //get All current customer journeys
            List<Journey> currentCustomerJourneyList = journeyList.stream()
                    .filter(journey -> journey.getCustomerId() == distinct_journey.get(jouney_index).getCustomerId())
                    .sorted(Comparator.comparing(Journey::getUnixTimestamp))
                    .collect(Collectors.toList());

            //local variables used to collect data
            int i = 0;
            int unixTimestampStart = 0;
            String stationNameStart = "", stationNameEnd = "";
            double totalCostInCents = 0;
            double costInCents = 0;
            List<Zone> zoneFromList= new ArrayList<>() ;
            List<Zone> zoneToList= new ArrayList<>() ;
            Price thePrice  = new Price();

            boolean isPrinted = false;

            while (i < currentCustomerJourneyList.size()) {

                if (i % 2 == 0) {

                    //case departure
                    unixTimestampStart = currentCustomerJourneyList.get(i).getUnixTimestamp();
                    stationNameStart = currentCustomerJourneyList.get(i).getStationName();

                } else {
                    //case arrival
                    stationNameEnd = currentCustomerJourneyList.get(i).getStationName();

                    //Get the zone From of the customer
                    zoneFromList = getTheZone(stationNameStart);
                    //Get the zone To of the customer
                    zoneToList = getTheZone(stationNameEnd);

                    //Generate the cost
                     thePrice  = GenerateTheCost(zoneFromList, zoneToList);

                     costInCents = thePrice.getPrice();

                    //accumulation
                    totalCostInCents += costInCents;

                    if (!isPrinted && i==currentCustomerJourneyList.size()-1){
                        isPrinted=true;
                        tripHeaderList.add(" \"customerId\" :" + currentCustomerJourneyList.get(0).getCustomerId() + ",\n");
                        tripHeaderList.add(" \"totalCostInCents\" :" + totalCostInCents + ",\n");
                        tripHeaderList.add(" \"trips\" : [ { \n");
                        totalCostInCents = 0;
                    }
                    tripDetailList.add("  \"stationStart\" :" + stationNameStart + ",\n");
                    tripDetailList.add("  \"stationEnd\" :" + stationNameEnd + ",\n");
                    tripDetailList.add("  \"startedJourneyAt\" :" + unixTimestampStart + ",\n");
                    tripDetailList.add("  \"costInCents\" :" + costInCents + ",\n");
                    costInCents = 0;
                    tripDetailList.add("  \"zoneFrom\" :" + thePrice.getZoneFrom().getId() + ",\n");
                    tripDetailList.add("  \"zoneTo\" :" + thePrice.getZoneTo().getId() + ",\n");
                    tripDetailList.add(" }");

                    if (i < currentCustomerJourneyList.size()-1)
                    tripDetailList.add(",{\n");
                }
                i++;
            }

            //close the trips tag
            tripDetailList.add("]\n");

            //Move to the next client

            //Adding to the
            tripFooterList.clear();
            if (k == distinct_journey.size()-1) {
                  tripFooterList.add(" }] \n");
            } else {
               tripFooterList.add(" }, { \n");
            }

            //Print the customer Trips Summaries Data to the File
            tripList.addAll(tripHeaderList);tripHeaderList.clear();
            tripList.addAll(tripDetailList);tripDetailList.clear();
            tripList.addAll(tripFooterList);tripFooterList.clear();

        }
        writeInTheOutputFile(tripList, Utility.OUTPUTFILENAME);
    }


    private static boolean isValidData(List<Journey> journeyList) {




        boolean isValid  = true;




        List<Journey> distinct_journey = journeyList.stream()
                .filter(Utility.distinctByKey(journey -> journey.getCustomerId())).collect(Collectors.toList());

        int k = 0;
        Zone zoneFrom, zoneTo;
        String stationNameStart = "", stationNameEnd="";
        while (isValid && k < distinct_journey.size()) {
            int j=k;

            //get All it journeys
            List<Journey> currentCustomerJourneyList = journeyList.stream()
                    .filter(journey -> journey.getCustomerId() == distinct_journey.get(j).getCustomerId())
                    .sorted(Comparator.comparing(Journey::getUnixTimestamp))
                    .collect(Collectors.toList());
            int i =0;
            List<Zone> zoneFromList= new ArrayList<>() ;
            List<Zone> zoneToList= new ArrayList<>() ;
            Price thePrice  = new Price();

            while (isValid && i < currentCustomerJourneyList.size()) {

                if (i % 2 == 0) {

                    //case departure
                    stationNameStart = currentCustomerJourneyList.get(i).getStationName();

                } else {
                    //case arrival
                    stationNameEnd = currentCustomerJourneyList.get(i).getStationName();

                    //Get the zone From of the customer
                    zoneFromList = getTheZone(stationNameStart);
                    //Get the zone To of the customer
                    zoneToList = getTheZone(stationNameEnd);

                    if (!isPriceDefined(zoneFromList, zoneToList))
                        isValid = false;
                }

                i++;
            }

            k++;
        }

        return isValid;
    }


    public static List<String> printHeader(List<String> writeToTheFileList) {
        writeToTheFileList.add("{");
        writeToTheFileList.add("\n");
        writeToTheFileList.add(" \"customerSummaries\" : [ {");
        writeToTheFileList.add("\n");
         /*writer.write("{");
            writer.write("\n");
            writer.write(" \"customerSummaries\" : [ {");
            writer.write("\n");*/
        return writeToTheFileList;
    }

    private static List<Zone> getTheZone(String stationName) {

        Station station = Database.getStationList().stream().filter(s -> s.getName().equalsIgnoreCase(stationName)).findFirst().get();

        return  station.getZones();
    }

    private static Price GenerateTheCost(List<Zone> zoneFrom, List<Zone> zoneTo) {
        Price  s_price;
        s_price = getTheMinimumPrice (zoneFrom, zoneTo);
        return s_price;
    }

    private static boolean inputTapWithoutOutputTapThrowExceptionImpl(List<Journey> tripList) {

        List<Integer> result = new ArrayList<Integer>();
        // Extract customerId list information from journey
        List<Journey> distinct_journey = tripList.stream()
                .filter(Utility.distinctByKey(journey -> journey.getCustomerId())).collect(Collectors.toList());

        distinct_journey.forEach(o -> {
            //Current customer Journey list
            int uniqueCustomerJourneyList = tripList.stream()
                    .filter(journey -> journey.getCustomerId() == o.getCustomerId())
                    .sorted(Comparator.comparing(Journey::getUnixTimestamp))
                    .collect(Collectors.toList()).size();
            if (uniqueCustomerJourneyList % 2 != 0) {
                result.add(1);
            }
        });
        return result.contains(1);
    }

    public static boolean fileDataIsInvalidThrowExceptionImpl(List<String> tripInformationsFromFile) {

        int unixTimestampNumber = tripInformationsFromFile.stream().filter(a -> a.contains("unixTimestamp")).collect(Collectors.toList()).size();
        int customerIdNumber = tripInformationsFromFile.stream().filter(a -> a.contains("customerId")).collect(Collectors.toList()).size();
        int stationNumber = tripInformationsFromFile.stream().filter(a -> a.contains("station")).collect(Collectors.toList()).size();
        return (unixTimestampNumber == customerIdNumber && customerIdNumber == stationNumber);
    }

    private static boolean stationIsNotExistThrowExceptionImpl(List<Journey> journeyList) {


        List<Station> stationList = Database.getStationList();



        List<Integer> result = new ArrayList<Integer>();
        // Extract customerId list information from journey
        List<Journey> distinct_journey = journeyList.stream()
                .filter(Utility.distinctByKey(journey -> journey.getStationName())).collect(Collectors.toList());


        distinct_journey.forEach(o -> {
            //check if there is a not defined or null unix
            int uniqueStationJourneyList = stationList.stream()
                    .filter(s -> s.getName().equalsIgnoreCase(o.getStationName()))
                    .collect(Collectors.toList()).size();
            if (uniqueStationJourneyList == 0) {
                result.add(1);
            }
        });
        return result.contains(1);
    }

    private static boolean customerIdIsNotExistThrowExceptionImpl(List<Journey> journeyList) {

        List<Customer> customerList = Database.getCustomerList();

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

    private static List<Journey> retrieveJourneyInformationFromFile(String fileName) {

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

    private static void writeInTheOutputFile(List<String> tripList, String fileName) {

        try {
            Path path = Paths.get(fileName);
            BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
            System.out.println("size =" + tripList.size());

            for (int i = 0; i < tripList.size(); i++)
                writer.write(tripList.get(i));

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     private static boolean isPriceDefined (List<Zone> zoneFrom, List<Zone> zoneTo){
        Price  s_price;
        s_price = getTheMinimumPrice (zoneFrom, zoneTo);
         return s_price.getPrice()!=Double.MAX_VALUE;
    }


     private static Price getTheMinimumPrice (List<Zone> zoneFrom, List<Zone> zoneTo) {
         Price  s_price= new Price();
         s_price.setPrice(Double.MAX_VALUE);
         for (int i = 0; i<zoneFrom.size(); i++){
             int i1 = i ;
             for (int j=0; j<zoneTo.size(); j++){
                 int j1=j;
                 Price c_price = Database
                         .getPriceList().stream()
                         .filter(p -> p.getZoneFrom().getId()==(zoneFrom.get(i1).getId())
                                 && p.getZoneTo().getId()==(zoneTo.get(j1).getId())).findFirst().get();
                 if(c_price.getPrice()<s_price.getPrice()) {
                     s_price = c_price;

                 }
             }
         }
         return  s_price;
     }


}