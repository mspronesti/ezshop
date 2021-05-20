package it.polito.ezshop.utils;

import java.io.*;
import java.net.URL;
import java.util.*;

public class PaymentGateway {
    private static final String SEPARATOR = ";";
    private static final String DB_FILE_PATH = "./CreditCards.txt";
    private static final String DB_FILE_HEADING = "#Do not delete the lines preceded by an \"#\" and do not modify the first three credit cards\n" +
            "#since they will be used in the acceptance tests.\n" +
            "#The lines preceded by an \"#\" must be ignored.\n" +
            "#Here you can add all the credit card numbers you need with their balance. The format MUST be :\n" +
            "#<creditCardNumber>;<balance>\n" +
            "4485370086510891;150.00\n" +
            "5100293991053009;10.00\n" +
            "4716258050958645;0.00\n";
    private static final List<String> testCardNumbers = Arrays.asList("4485370086510891", "5100293991053009", "4716258050958645");
    private final Map<String, Double> cards = new HashMap<>();

    public PaymentGateway() {
        InputStream stream = getClass().getResourceAsStream(DB_FILE_PATH);
        if (stream == null) {
            return;
        }
        try (InputStreamReader fr = new InputStreamReader(stream)) {
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(SEPARATOR);
                if (parts.length != 2) {
                    continue;
                }
                cards.put(parts[0], Double.valueOf(parts[1]));
            }
        } catch (IOException exception) {
            System.out.println("Could not initialize PaymentGateway");
        }
    }

    public boolean requestPayment(String creditCardNumber, Double amount) {
        Double cardBalance = cards.get(creditCardNumber);
        if (cardBalance != null && cardBalance >= amount) {
            cards.put(creditCardNumber, cardBalance - amount);
            persistUpdates();
            return true;
        }
        return false;
    }

    public boolean requestAccreditation(String creditCardNumber, Double amount) {
        Double cardBalance = cards.get(creditCardNumber);
        if (cardBalance != null) {
            cards.put(creditCardNumber, cardBalance + amount);
            persistUpdates();
            return true;
        }
        return false;
    }

    private void persistUpdates() {
        URL dbUrl = getClass().getResource(DB_FILE_PATH);
        if (dbUrl == null) {
            return;
        }
        File dbFile = new File(dbUrl.getFile());
        try (PrintWriter writer = new PrintWriter(dbFile)) {
            writer.append(DB_FILE_HEADING);
            cards.forEach((number, balance) -> {
                if (!testCardNumbers.contains(number)) {
                    writer.println(number + SEPARATOR + balance);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
