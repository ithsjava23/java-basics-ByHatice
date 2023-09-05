package org.example;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        new Locale("sv","SE"); //Svensk standard dvs.kommatecken som decimalavgränsare.
        Scanner scanner = new Scanner(System.in);

        boolean dataInsamlad = false; // Kontroll om data har samlats in.

        String menyVal; //Läser inmatade värdet av inmataren

        int elPris [] = new int[0];
        /*

        do {
            menyVal = visaOchValjMeny(scanner);

            switch (menyVal) {
                case "1": elPris = inmatning(scanner);
                    dataInsamlad = true; // Sätt flaggan till true när data har samlats in
                    break;
                case "2":
                    if (dataInsamlad) {
                        minMaxMedel(elPris);
                    } else System.out.println("\nIngen data har samlats in än! \nVänligen välj alternativ 1 först.");
                    continue;// Återgå till början av loopen för att välja ett nytt menyval

                case "3":
                    continue;

                case "4":
                    System.out.print("4");
                    break;
                case "5":
                    System.out.print("5");
                    break;
                default:
                    System.out.print("ogiltig val var vänlig försök igen");
            }
        }

        while (menyVal != "e" && menyVal != "E");
        scanner.close();*/

    }
    public static String visaOchValjMeny(Scanner scanner) {
        /* Börja med att skapa ett program som vid start skriver ut en meny. När alternativ e
        väljs ska programmet avslutas. Både e och E ska vara giltiga som val för att avsluta.
        Vid val av något av de övriga alternativen ska dessa köras och när körningen är klar
        ska menyn åter skrivas ut på skärmen så att ett nytt val kan göras
         */

        System.out.print("\nElpriser");
        System.out.print("\n========");
        System.out.print("\n1. Inmatning");
        System.out.print("\n2. Min, Max och Medel");
        System.out.print("\n3. Sortera");
        System.out.print("\n4. Bästa Laddningstid (4h)");
        System.out.print("\n5. Visualisering");
        System.out.print("\ne. Avsluta\n\n");

        return scanner.nextLine();

    }

    public static int [] inmatning(Scanner scanner) {
       /* Det här programmet ska kunna hjälpa till med att analysera elpriser för ett dygn.
       När man väljer alternativet inmatning från menyn ska programmet fråga efter priserna
       under dygnets timmar. Inmatningen av värden ska ske med hela ören. T.ex. kan priser
       vara 50 102 eller 680 öre per kW/h. Priset sätts per intervall mellan två hela timmar.
       Dygnets första pris är då mellan 00-01, andra intervallet är mellan 01-02 osv.
        */

        int [] elPris = new int[24];

        System.out.print("Ange priset per intervall mellan två hela timmar \n" +
                "Ange hela ören för varje timma under dygnet.\n\n");


        for (int tim =0; tim < elPris.length; tim++) {
            System.out.print("Från klockan " + klockslag(tim) + "\nAnge ören i heltal: \n");

            int inmataPris = scanner.nextInt();
            elPris [tim] = inmataPris;

            while (true) {
                try  {

                    if (inmataPris == 0) {
                        System.out.println("Fel format! \nVar vänlig mata in heltal\n");
                    }
                    else {
                        elPris [tim] = inmataPris;
                        break;
                    }
                }
                catch (InputMismatchException e){
                    System.out.print("Fel format! \nVar vänlig mata in heltal\n");
                    scanner.next();
                }
            }
        }
        return elPris;
    }


    public static void minMaxMedel (int[] elPris) {
       /*När alternativ 2 väljs på menyn så ska programmet skriva ut lägsta priset, högsta priset samt vilka
        timmar som detta infaller under dygnet. Dygnets medelpris ska också räknas fram och presenteras
        på skärmen. Se testerna för önskat format på utmatningen
      */
        int min = elPris[0];
        int max = elPris[0];
        int timMin = 0;
        int timMax = 0;
        int totalPris = 0;

        for (int tim = 0; tim < elPris.length; tim++) {
            int pris =  elPris[tim];
            totalPris += pris;

            if (pris < min){
                min = pris;
                timMin = tim;
            }
            if (pris > max){
                max = pris;
                timMax = tim;
            }
        }
        float medelPris = (float) totalPris/ elPris.length;

        System.out.print("\n2. Min, Max och Medel\n\n");
        System.out.print("Lägsta pris: " + min + " öre, under timmen " + klockslag(timMin));
        System.out.print("\nHögsta pris: " + max + " öre, under timmen " + klockslag(timMax));
        System.out.print("\nMedelpris: " + Math.round(medelPris) + " öre");

        // Skriv ut alla timmar med lägsta priset
        System.out.print("\nLägsta priset under dygnet innefaller under timmarna: \n");
        for (int tim = 0; tim < elPris.length; tim++) {
            if (elPris[tim] == min){
                System.out.print(klockslag(tim) + "  ");
            }
        }
        System.out.print("\n");

    }

    public static int[] bubbleSort (int [] elPris){

        boolean sorterad = true; //för att gå in i loopen

        while (sorterad){
            sorterad =false; // om den inte gått in i loopen blir det false

            for (int i = 0; i < elPris.length -1; i++) {
                if(elPris[i] < elPris[i+1]) {
                    sorterad = true; // gått igenom llopen
                    int temp = elPris[i]; // Tillfällig variabel för att lagra [i]
                    elPris [i] = elPris[i+1];
                    elPris[i+1] = temp;
                }
            }
        }
        return elPris;
    }

    public static String klockslag(int tim) {

        String startTid = String.format("%02d", tim);
        String slutTid = String.format("%02d", (tim +1) % 24);

        return startTid + "-" + slutTid;
    }








}

