package dev.billing;

import dev.billing.entities.Journey;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BillingSystem {

    public static void main(String... args) {

        System.out.println("This is our Billing System****");
        Path p = Paths.get("D:\\Personnel\\Ingéniance\\Entretien\\Céline\\CandidateInputExample.txt");
        /*System.out.println("file exist : " + Files.exists(p));
        System.out.println("si c'est un fichier et non un dossier: " + Files.isRegularFile(p));
        System.out.println("le fichier peut être accéder en lecture  : " + Files.isReadable(p));*/

        //read file into stream, try-with-resources
        try {

            Stream<String> fileStream = Files.lines(p);

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
                lineData =tripInformationsFromFile.get(index++).toString().replaceAll(",", "")
                        .replaceAll(" ", "").replaceAll("\"", "").split(":");
                aJourney.setCustomerId(Integer.parseInt(lineData[1]));


                //get information from the line index%3
                lineData = tripInformationsFromFile.get(index++).toString().replaceAll(",", "")
                        .replaceAll(" ", "").replaceAll("\"", "").split(":");;
                aJourney.setStationName(lineData[1]);

                tripList.add(aJourney);
            }

            System.out.println(tripList);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}



