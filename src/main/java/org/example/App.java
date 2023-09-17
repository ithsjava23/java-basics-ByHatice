package org.example;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Locale swedishLocale = new Locale("sv", "SE");
        Locale.setDefault(swedishLocale);

        Scanner scanner = new Scanner(System.in);
        menuSwitch(scanner);
    }

    public static TimePrice[] timePriceArray(int totalTime) {
        TimePrice[] timePrices = new TimePrice[totalTime];
        for (int hour = 0; hour < totalTime; hour++) {
            // Skapa ett TimePrice-objekt med rätt tid och initialt pris (0 i detta fall)
            timePrices[hour] = new TimePrice(timeFormat(hour), 0);
        }
        return timePrices;
    }

    private static void menu() {
        System.out.println("""
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta
                """);
    }

    private static void menuSwitch(Scanner scanner) {
        TimePrice[] timePrices = timePriceArray(24);
        String menu;

        boolean dataCollected = false;
        do {
            menu();
            menu = scanner.next();
            switch (menu) {
                case "1" -> {
                    inputData(timePrices, scanner);
                    dataCollected = true;
                }
                case "2" -> {
                    if (dataCollected) {
                        minMaxMean(timePrices);
                    } else errorMessage();
                }
                case "3" -> {
                    if (dataCollected) {
                        bubbleSort(timePrices);
                    } else errorMessage();
                }
                case "4" -> {
                    if (dataCollected) {
                        bestChargeTime(timePrices);
                    } else errorMessage();
                }
                case "5" -> {
                    if (dataCollected) {
                        timePriceChart(timePrices);
                    } else errorMessage();
                }
                case "e", "E" -> System.out.print("Programmet avslutas.");
                default -> System.out.print("Ogiltig val. Var vänlig försök igen.");
            }
        } while (!menu.equalsIgnoreCase("e"));
        scanner.close();
    }

    private static void inputData(TimePrice[] timePrices, Scanner scanner) {
        scanner.nextLine();

        for (int i = 0; i < timePrices.length; i++) {
            System.out.print("\n" + timeFormat(i) + " ");
            try {
                String input = scanner.nextLine();
                if (input.matches("-?\\d+")) { // Kontrollera att inmatningen endast innehåller siffror
                    timePrices[i].price = Integer.parseInt(input);
                } else {
                    System.out.print("Felaktig inmatning. Ange ett heltal.");
                    i--; // Återgå till samma timme om det var felaktig inmatning
                }
            } catch (NumberFormatException e) {
                System.out.print("Felaktig inmatning. Ange ett heltal.");
            }
        }
    }

    private static void minMaxMean(TimePrice[] timePrices) {
        // Skapar en int-array med priserna från timePrices-objekten
        int[] prices = Arrays.stream(timePrices).mapToInt(timePrice -> timePrice.price).toArray();
        int min = minValue(prices);
        int max = maxValue(prices);
        int totalPrice = Arrays.stream(prices).sum();

        String timeMin = null;
        String timeMax = null;

        for (TimePrice timePrice : timePrices) {
            int price = timePrice.price;
            if (price == min) {
                timeMin = timePrice.time;
            }
            if (price == max) {
                timeMax = timePrice.time;
            }
        }
        float meanPrice = (float) totalPrice / timePrices.length;
        System.out.print("\nLägsta pris: " + timeMin + ", " + min + " öre/kWh");
        System.out.print("\nHögsta pris: " + timeMax + ", " + max + " öre/kWh");
        System.out.printf("\nMedelpris: %.2f öre/kWh\n", meanPrice);
    }

    private static void bubbleSort(TimePrice[] timePrices) {
        int[] sortedTime = new int[24];
        for (int i = 1; i < sortedTime.length; i++) {
            sortedTime[i] = i;
        }
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < timePrices.length; i++) {
                if (timePrices[i - 1].price < timePrices[i].price) {
                    swapped = true;
                    TimePrice swappPrice = timePrices[i - 1];
                    timePrices[i - 1] = timePrices[i];
                    timePrices[i] = swappPrice;
                    int temp = sortedTime[i - 1];
                    sortedTime[i - 1] = sortedTime[i];
                    sortedTime[i] = temp;
                }
            }
        } while (swapped);
        for (int i = 0; i < timePrices.length; i++) {
            System.out.print(timeFormat(sortedTime[i]) + " " + timePrices[i].price + " öre\n");
        }
    }

    private static void bestChargeTime(TimePrice[] timePrices) {
        int[] prices = Arrays.stream(timePrices).mapToInt(timePrice -> timePrice.price).toArray();
        int bestTotalPrice = Integer.MAX_VALUE;
        int bestStartTime = 0;
        for (int startTime = 0; startTime < 21; startTime++) {
            int[] subPrices = Arrays.copyOfRange(prices, startTime, startTime + 4);
            int total = Arrays.stream(subPrices).sum();
            if (total < bestTotalPrice) {
                bestTotalPrice = total;
                bestStartTime = startTime;
            }
        }
        double meanPrice4Hours = (double) bestTotalPrice / 4;
        System.out.printf("\nPåbörja laddning klockan " + bestStartTime);
        System.out.printf("\nMedelpris 4h: %.1f öre/kWh\n", meanPrice4Hours);
    }

    private static String timeFormat(int hour) {
        String startTime = String.format("%02d", hour);
        String stopTime = String.format("%02d", (hour + 1));
        return startTime + "-" + stopTime;
    }

    private static void errorMessage() {
        System.out.print("\nIngen data har samlats in än! \nVänligen välj alternativ 1 först.");
    }

    private static int minValue(int[] prices) {
        return Arrays.stream(prices).min().orElse(0);
    }

    private static int maxValue(int[] prices) {
        return Arrays.stream(prices).max().orElse(0);
    }

    public static void timePriceChart(TimePrice[] timePrices) {
        int[] prices = Arrays.stream(timePrices).mapToInt(timePrice -> timePrice.price).toArray();


        for (int i = 6; i >= 1; --i) {
            if(i==6) {
                System.out.print(String.format("%02d", maxValue(prices)) + "|");
            }
            if(i == 1) {
                System.out.print((String.format("%02d", minValue(prices)) + " |"));
            }
             if (i != 1 && i != 6)   System.out.print("   |");

            // time måste justeras till en scale
            for (int timme = 0; timme < timePrices.length; timme++) {
                if (prices[timme] >= i) {
                    System.out.print(" x ");
                } else {
                    System.out.print("   ");
                }
            }
                System.out.print("\n");
            }
            System.out.print("   |");
            for (int timme = 0; timme < timePrices.length; timme++) {
                System.out.print("---");
            }
            System.out.println();

            System.out.print("   |");
            for (int timme = 0; timme < timePrices.length; timme++) {
                System.out.print(String.format("%02d", timme) + " ");
            }
            System.out.println("\n");
        }
}
