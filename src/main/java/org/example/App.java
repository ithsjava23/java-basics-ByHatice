package org.example;

import java.util.*;

public class App {
    public static void main(String[] args) {

        Locale swedishLocale = new Locale("sv", "SE");
        Locale.setDefault(swedishLocale);

        Scanner scanner = new Scanner(System.in);


        String menu;

        boolean dataCollected = false;

        int totalTime = 24;

        TimePrice[] timePrices = timePriceArray(totalTime);



        do {
            menu();
            menu = scanner.next();

            switch (menu) {
                case "1":
                    inputData(timePrices, scanner);
                    dataCollected = true;
                    break;
                case "2":
                    if (dataCollected) {
                        minMaxMean(timePrices);
                    } else System.out.print("\nIngen data har samlats in än! \nVänligen välj alternativ 1 först.");
                    break;
                case "3":
                    if (dataCollected) {
                        bubbleSort(timePrices);
                    } else System.out.print("\nIngen data har samlats in än! \nVänligen välj alternativ 1 först.");
                    break;
                case "4":
                    if (dataCollected) {
                        bestChargeTime(timePrices);
                    } else System.out.print("\nIngen data har samlats in än! \nVänligen välj alternativ 1 först.");
                    break;
                case "e", "E":
                    System.out.print("Programmet avslutas.");
                    break;
                default:
                    System.out.print("Ogiltig val. Var vänlig försök igen.");

            }

        } while (!menu.equalsIgnoreCase("e"));

        scanner.close();
    }

    public static void menu() {

        System.out.print("\nElpriser");
        System.out.print("\n========");
        System.out.print("\n1. Inmatning");
        System.out.print("\n2. Min, Max och Medel");
        System.out.print("\n3. Sortera");
        System.out.print("\n4. Bästa Laddningstid (4h)");
        System.out.print("\ne. Avsluta\n");

    }

    public static TimePrice[] inputData(TimePrice[] timePrices, Scanner scanner) {
        for (int i = 0; i < timePrices.length; i++) {
            boolean giltigInmatning = false;
            System.out.print(timePrices[i].time + "\n");

            while (!giltigInmatning) {
                try {
                    String input = scanner.nextLine();
                    if (input.matches("-?\\d+")) { // Kontrollera att inmatningen endast innehåller siffror
                        int price = Integer.parseInt(input);
                        timePrices[i].price = price; // Tilldela priset till TimePrice-objektet
                        giltigInmatning = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Felaktig inmatning. Ange ett heltal.");
                }
            }
        }
        return timePrices;
    }

    public static void minMaxMean(TimePrice[] timePrices) {

        int min = timePrices[0].price;
        int max = timePrices[0].price;
        String timeMin = timePrices[0].time;
        String timeMax = timePrices[0].time;
        int totalPrice = 0;

        for (TimePrice timePrice : timePrices) {
            int price = timePrice.price;
            totalPrice += price;

            if (price < min) {
                min = price;
                timeMin = timePrice.time;
            }
            if (price > max) {
                max = price;
                timeMax = timePrice.time;
            }
        }

        float meanPrice = (float) totalPrice / timePrices.length;

        // Skriv ut min, max och medelpris
        System.out.print("\nLägsta pris: " + timeMin + ", " + min + " öre/kWh");
        System.out.print("\nHögsta pris: " + timeMax + ", " + max + " öre/kWh");
        System.out.printf("\nMedelpris: %.2f öre/kWh\n", meanPrice);
    }

    public static TimePrice[] bubbleSort(TimePrice[] timePrices) {

        int x = timePrices.length;
        int[] sortedTime = new int[24];
        for(int i=1; i< sortedTime.length; i++){
            sortedTime[i] = i;
        }
        boolean swapped;

        do {
            swapped = false;
            for (int i = 1; i < x; i++) {

                // Jämför baserat på pris i fallande ordning
                if (timePrices[i - 1].price < timePrices[i].price) {
                    swapped = true;

                    // Byt plats på TimePrice-objekten
                    TimePrice swappPrice = timePrices[i - 1];
                    timePrices[i - 1] = timePrices[i];
                    timePrices[i] = swappPrice;
                    // Byt plats på Timeprice indexen i index-arrayen
                    int temp = sortedTime[i-1];
                    sortedTime[i-1] = sortedTime[i];
                    sortedTime[i]= temp;

                }
            }
        } while (swapped);

       for (int i = 0; i < timePrices.length; i++) {

            System.out.print(timeFormat(sortedTime[i]) + " " + timePrices[i].price + " öre\n");
        }
        return timePrices;
    }

    public static void bestChargeTime(TimePrice[] timePrices) {
        int bestTotalPrice = Integer.MAX_VALUE;
        int bestStartTime = 0;

        for (int startTime = 0; startTime < 21; startTime++) {
            int total = 0;

            for (int i = startTime; i < startTime + 4; i++) {
                total += timePrices[i].price;
            }

            if (total < bestTotalPrice) {
                bestTotalPrice = total;
                bestStartTime = startTime;
            }
        }

        double meanPrice4Hours = (double) bestTotalPrice / 4;

        // Skriv ut resultatet
        System.out.printf("\nPåbörja laddning klockan " + bestStartTime);
        System.out.printf("\nMedelpris 4h: %.1f öre/kWh\n", meanPrice4Hours);
    }

    public static TimePrice[] timePriceArray(int totalTime) {

        TimePrice[] timePrices = new TimePrice[totalTime];

        for (int hour = 0; hour < totalTime; hour++) {
            // Skapa ett TimePrice-objekt med rätt tid och initialt pris (0 i detta fall)
            timePrices[hour] = new TimePrice(timeFormat(hour), 0);
        }
        return timePrices;
    }

   public static String timeFormat(int hour) {
        String startTime = String.format("%02d", hour);
        String stopTime = String.format("%02d", (hour +1) % 24);

        return startTime + "-" + stopTime;

    }

}
class TimePrice {
    String time;
    int price;

    public TimePrice (String time, int price) {
        this.time = time;
        this.price = price;
    }
  }

