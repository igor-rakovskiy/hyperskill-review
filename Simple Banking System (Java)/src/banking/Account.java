package banking;

import java.util.Random;

public class Account {
    private final String cardNumber;
    private final String pinCode;
    private int balance;

    public Account() {
        Random random = new Random();
        String bin = "400000";

        String account = String.format("%09d", random.nextInt(1_000_000_000));
        String first15 = bin + account;
        int checksum = LunaAlgoritm.generateChecksum(first15);

        this.cardNumber = first15 + checksum;
        this.pinCode = String.format("%04d", random.nextInt(10000));
        this.balance = 0;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public int getBalance() {
        return balance;
    }
}