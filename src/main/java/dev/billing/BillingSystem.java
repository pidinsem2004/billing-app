package dev.billing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BillingSystem {

    public static void main(String... args) {


        Path p = Paths.get(args[0]);
        //Path p = Paths.get("D:\\Personnel\\Ingeniance\\Entretien\\Celine\\CandidateInputExample.txt");

        System.out.println("Fichier source :" + p.getRoot());
        System.out.println("Fichier source :" + p.getFileName());
        System.out.println("Fichier source :" + p.toAbsolutePath());

        System.out.println("file exist : " + Files.exists(p));
        System.out.println("si c'est un fichier et non un dossier: " + Files.isRegularFile(p));
        System.out.println("le fichier peut être accéder en lecture  : " + Files.isReadable(p));


    }
}
