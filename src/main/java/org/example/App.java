package org.example;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class App{
    private static final int ANTAL_TIMMAR = 24;
    private static int[] priser = new int[ANTAL_TIMMAR];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.forLanguageTag("sv-SE"));
        char val;

        do {
            skrivUtMeny();
            val = scanner.next().charAt(0);
            switch (Character.toLowerCase(val)) {
                case '1':
                    inmatning(scanner);
                    break;
                case '2':
                    minMaxMedel();
                    break;
                case '3':
                    sortera();
                    break;
                case '4':
                    bästaLaddningstid();
                    break;
                case '5':
                    visualisering();
                    break;
                case 'e':
                    System.out.print("Programmet avslutas.\n");
                    break;
                default:
                    System.out.print("Ogiltigt val. Försök igen.\n");
            }
        } while (val != 'e' && val != 'E');
    }

    // Skriver ut menyn
    private static void skrivUtMeny() {
        System.out.print("Elpriser\n========\n");
        System.out.print("1. Inmatning\n");
        System.out.print("2. Min, Max och Medel\n");
        System.out.print("3. Sortera\n");
        System.out.print("4. Bästa Laddningstid (4h)\n");
        System.out.print("e. Avsluta\n");
        System.out.print("Välj ett alternativ: ");
    }

    // Inmatning av elpriser
    private static void inmatning(Scanner scanner) {
        System.out.print("Ange elpriser för dygnet (i öre):\n");
        for (int i = 0; i < ANTAL_TIMMAR; i++) {
            System.out.printf("Pris för timme %02d-%02d: ", i, i + 1);
            priser[i] = scanner.nextInt();
        }
    }

    // Beräkna min, max och medelvärde
    private static void minMaxMedel() {
        int minPris = Arrays.stream(priser).min().getAsInt();
        int maxPris = Arrays.stream(priser).max().getAsInt();
        double medelPris = Arrays.stream(priser).average().getAsDouble();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("sv-SE"));
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        System.out.printf("Lägsta pris: %02d-%02d, %d öre/kWh\n", hittaTimme(minPris), hittaTimme(minPris) + 1, minPris);
        System.out.printf("Högsta pris: %02d-%02d, %d öre/kWh\n", hittaTimme(maxPris), hittaTimme(maxPris) + 1, maxPris);
        System.out.printf("Medelpris: %s öre/kWh\n", df.format(medelPris));
    }

    // Hitta timmen då ett pris inträffar
    private static int hittaTimme(int pris) {
        for (int i = 0; i < priser.length; i++) {
            if (priser[i] == pris) {
                return i;
            }
        }
        return -1; // Bör aldrig inträffa
    }

    // Sortering av priser
    private static void sortera() {
        int[] sorteradePriser = priser.clone();
        Arrays.sort(sorteradePriser);

        System.out.print("Sorterade priser (dyrast till billigast):\n");
        for (int i = sorteradePriser.length - 1; i >= 0; i--) {
            int pris = sorteradePriser[i];
            System.out.printf("%02d-%02d %d öre\n", hittaTimme(pris), hittaTimme(pris) + 1, pris);
        }
    }

    // Bästa laddningstiden (4 timmar)
    private static void bästaLaddningstid() {
        int bästaStartTimme = 0;
        int lägstTotalPris = Integer.MAX_VALUE;

        for (int i = 0; i <= ANTAL_TIMMAR - 4; i++) {
            int totalPris = priser[i] + priser[i + 1] + priser[i + 2] + priser[i + 3];
            if (totalPris < lägstTotalPris) {
                lägstTotalPris = totalPris;
                bästaStartTimme = i;
            }
        }

        double medelPris = lägstTotalPris / 4.0;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("sv-SE"));
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.0", symbols);

        System.out.printf("Påbörja laddning klockan %02d\n", bästaStartTimme);
        System.out.printf("Medelpris 4h: %s öre/kWh\n", df.format(medelPris));
    }

    // Visualisering av priser
    private static void visualisering() {
        int maxPris = Arrays.stream(priser).max().getAsInt();
        int minPris = Arrays.stream(priser).min().getAsInt();
        int skala = (maxPris - minPris) / 76;

        for (int i = 0; i < ANTAL_TIMMAR; i++) {
            int antalX = (priser[i] - minPris) / skala;
            System.out.printf("%02d-%02d | ", i, i + 1);
            for (int j = 0; j < antalX; j++) {
                System.out.print("x");
            }
            System.out.print("\n");
        }
    }
}
