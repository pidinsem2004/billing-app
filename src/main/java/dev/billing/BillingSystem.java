package dev.billing;
import dev.billing.dao.Database;
import dev.billing.entities.*;
import dev.billing.utilities.Utility;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class BillingSystem {
    private BufferedWriter writer;

    public static void main(String... args) {
        Utility.INPUTFILENAME = args[0];
        Utility.OUTPUTFILENAME = args[1];

        Path p = Paths.get(Utility.INPUTFILENAME);

        //Init the Database
        new Database().init();

        /**
         * Test case 7
         * Input file should exist,  if not throw an exception
         * @throws RuntimeException
         */
        if (!Utility.inputFileNotExistThrowExceptionImpl(p)) {
            throw new RuntimeException("input File does not exist");
        }
        /**
         * Test case 8
         * Input file should be a txt file,  if not throw an exception
         *
         * @throws RuntimeException
         */
        if (!Utility.inputFileExtensionNotTxtThrowExceptionImpl(p.getFileName().toString().trim()))
            throw new RuntimeException("input File is not a txt file ");
        /**
         * Test case 13
         * In the journey file each "unixTimestamp"  should correspond to  "customerId" and the "station"
         * otherwise, invalid file data
         *
         * @throws RuntimeException
         */
        if (!Utility.fileDataIsInvalidThrowExceptionImpl(p))
                throw new RuntimeException("Invalid file data");

        List<Journey> journeyList = Utility.retrieveJourneyInformationFromFile(Utility.INPUTFILENAME);
        /**
         * Test case 10
         * If the unixTimestamp in the imput file is not defined, throw exception
         *
         * @throws RuntimeException
         */
        if (Utility.unixTimestampIsNegativeOrEqualsToZeroThrowExceptionImpl(journeyList))
            throw new RuntimeException("There is an Incorrect unixTimestamp value in the input file");

        /**
         * Test case 11
         * If the customerId is not defined or is not exist or is null, throw exception
         *
         * @throws RuntimeException
         */
        if (Utility.customerIdIsNotExistThrowExceptionImpl(journeyList))
            throw new RuntimeException("There is an incorrect customerId in the input file");

        /**
         * Test case 12
         * If the station is not defined or is not exist , throw exception
         *
         * @throws RuntimeException
         */
        if (Utility.stationIsNotExistThrowExceptionImpl(journeyList))
            throw new RuntimeException("There is an incorrect station");

        /**
         * Implementation of the Test case 14
         * Any input tap should correspond to an output tap
         *
         * @throws RuntimeException
         */
        if (Utility.inputTapWithoutOutputTapThrowExceptionImpl(journeyList))
            throw new RuntimeException("There is an input tap without an output tap in the input file");
        /**
         * Implementation of the Test case 15
         * Zone should be defined and the price set, otherwise throw an exception
         *
         * @throws RuntimeException
         */
        if (!Utility.isValidData(journeyList) )
            throw new RuntimeException("invalid zone from to zone to, Price not defined");

        //the File is Ok, let's run the billing System
        Utility.runBillingSystem(journeyList);

    }
}