package dev.billing.utilities;

import dev.billing.dao.Database;
import dev.billing.entities.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Cette classe va définir un ensemble de méthode
 * utilitaires.
 *
 * @author Jean Pierre NSEM
 */
public abstract class Utility {


   public  static  final String INPUTFILENAME = ".\\src\\main\\resources\\CandidateInputExample.txt";
   public  static  final String OUTPUTFILENAME = ".\\src\\main\\resources\\CandidateOutputExample.txt";

    /**
     * predicate to filter the duplicate value
     */

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
        return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static void runBillingSystem(List<Journey> journeyList) {


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

    public static boolean isValidData(List<Journey> journeyList) {

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

    public static List<Zone> getTheZone(String stationName) {

        Station station = Database.getStationList().stream().filter(s -> s.getName().equalsIgnoreCase(stationName)).findFirst().get();

        return  station.getZones();
    }

    public static Price GenerateTheCost(List<Zone> zoneFrom, List<Zone> zoneTo) {
        Price  s_price;
        s_price = getTheMinimumPrice (zoneFrom, zoneTo);
        return s_price;
    }

    public static boolean inputTapWithoutOutputTapThrowExceptionImpl(List<Journey> tripList) {

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

    public static boolean fileDataIsInvalidThrowExceptionImpl(Path p ) {
        int unixTimestampNumber;
        int customerIdNumber;
        int stationNumber;

        try {
            Stream<String> fileStream = Files.lines(p);
            List<String> tripInformationFromFile = fileStream.filter(a ->
                    a.contains("unixTimestamp") ||
                            a.contains("customerId") ||
                            a.contains("station")).collect(Collectors.toList());
            tripInformationFromFile.stream().forEach(System.out::println);


             unixTimestampNumber = tripInformationFromFile.stream().filter(a -> a.contains("unixTimestamp")).collect(Collectors.toList()).size();
             customerIdNumber = tripInformationFromFile.stream().filter(a -> a.contains("customerId")).collect(Collectors.toList()).size();
             stationNumber = tripInformationFromFile.stream().filter(a -> a.contains("station")).collect(Collectors.toList()).size();

            return (unixTimestampNumber == customerIdNumber && customerIdNumber == stationNumber);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean stationIsNotExistThrowExceptionImpl(List<Journey> journeyList) {

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

    public static boolean customerIdIsNotExistThrowExceptionImpl(List<Journey> journeyList) {

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

    public static boolean inputFileExtensionNotTxtThrowExceptionImpl(String fileName) {
        return fileName.endsWith(".txt");
    }

    public static boolean inputFileNotExistThrowExceptionImpl(Path p) {
        return (Files.exists(p));
    }

    public static List<Journey> retrieveJourneyInformationFromFile(String fileName) {

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

    public static boolean unixTimestampIsNegativeOrEqualsToZeroThrowExceptionImpl(List<Journey> journeyList) {

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

    public static void writeInTheOutputFile(List<String> tripList, String fileName) {

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

    public static boolean isPriceDefined (List<Zone> zoneFrom, List<Zone> zoneTo){
        Price  s_price;
        s_price = getTheMinimumPrice (zoneFrom, zoneTo);
        return s_price.getPrice()!=Double.MAX_VALUE;
    }

    public static Price getTheMinimumPrice (List<Zone> zoneFrom, List<Zone> zoneTo) {
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
