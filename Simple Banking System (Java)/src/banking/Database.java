package banking;

import java.sql.*;
import java.util.Scanner;

public class Database {
    private final String fileName;

    public Database(String fileName) {
        this.fileName = fileName;

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + fileName);
             Statement statement = con.createStatement()) {

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (id INTEGER, number TEXT, pin TEXT, balance INTEGER DEFAULT 0)");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void closeAccount(String cardNumber, String pinCode) {
        String sql = "DELETE from card where number = ? and pin = ?";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + fileName);
             PreparedStatement parametr = con.prepareStatement(sql)) {
            parametr.setString(1, cardNumber);
            parametr.setString(2, pinCode);
            int resultDrop =  parametr.executeUpdate();
            if (resultDrop>0) {
                System.out.println("The account has been closed!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void balance(String cardNumber, String pinCode) {
        String sql = "Select balance from card where number = ? and pin = ?";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + fileName);
             PreparedStatement parametr = con.prepareStatement(sql)) {
            parametr.setString(1, cardNumber);
            parametr.setString(2, pinCode);
          ResultSet result =  parametr.executeQuery();
          if (result.next()) {
              System.out.println("Balance: " + result.getInt("balance"));
          }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addIncome(int cash, String cardNumber, String pinCode) {
        String sql = "Update card SET balance = balance + ? where number = ? and pin = ?";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + fileName);
             PreparedStatement parametr = con.prepareStatement(sql)) {
            parametr.setString(2, cardNumber);
            parametr.setString(3, pinCode);
            parametr.setInt(1, cash);
            int resultUpdate =  parametr.executeUpdate();
            if (resultUpdate > 0) {
                System.out.println("Income was added!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void doTransfer(String cardNumber, String pinCode, Scanner scanner) {
        System.out.println("Enter card number:");
        String newCard = scanner.next();

        if (cardNumber.equals(newCard)) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }

        if (!isValidLuhn(newCard)) {
            System.out.println(
                    "Probably you made a mistake in the card number. Please try again!"
            );
            return;
        }

        String checkCardSql = "SELECT 1 FROM card WHERE number = ?";
        String checkBalanceSql =
                "SELECT balance FROM card WHERE number = ? AND pin = ?";

        String transferSql = """
            UPDATE card
            SET balance = CASE
                WHEN number = ? THEN balance - ?
                WHEN number = ? THEN balance + ?
            END
            WHERE number IN (?, ?)
            """;

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + fileName)) {

            if (!cardExists(con, checkCardSql, newCard)) {
                System.out.println("Such a card does not exist.");
                return;
            }

            System.out.println("Enter how much money you want to transfer:");

            if (!scanner.hasNextInt()) {
                scanner.next();
                System.out.println("Please enter a whole number.");
                return;
            }

            int amount = scanner.nextInt();

            if (amount <= 0) {
                System.out.println("Transfer amount must be greater than zero.");
                return;
            }

            con.setAutoCommit(false);

            try {

                if (!cardExists(con, checkCardSql, newCard)) {
                    con.rollback();
                    System.out.println("Such a card does not exist.");
                    return;
                }

                try (PreparedStatement checkBalance = con.prepareStatement(checkBalanceSql)) {
                    checkBalance.setString(1, cardNumber);
                    checkBalance.setString(2, pinCode);

                    try (ResultSet result = checkBalance.executeQuery()) {
                        if (!result.next()) {
                            con.rollback();
                            System.out.println("Your card was not found.");
                            return;
                        }

                        if (result.getLong("balance") < amount) {
                            con.rollback();
                            System.out.println("Not enough money!");
                            return;
                        }
                    }
                }

                try (PreparedStatement transfer = con.prepareStatement(transferSql)) {
                    transfer.setString(1, cardNumber);
                    transfer.setInt(2, amount);
                    transfer.setString(3, newCard);
                    transfer.setInt(4, amount);
                    transfer.setString(5, cardNumber);
                    transfer.setString(6, newCard);

                    int updatedRows = transfer.executeUpdate();

                    if (updatedRows != 2) {
                        con.rollback();
                        System.out.println("Transfer could not be completed.");
                        return;
                    }
                }

                con.commit();
                System.out.println("Success!");

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean cardExists(Connection con, String sql, String cardNumber)
            throws SQLException {
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, cardNumber);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    private boolean isValidLuhn(String cardNumber) {
        if (cardNumber.isEmpty()) {
            return false;
        }

        int sum = 0;
        boolean doubleDigit = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            char character = cardNumber.charAt(i);

            if (character < '0' || character > '9') {
                return false;
            }

            int digit = character - '0';

            if (doubleDigit) {
                digit *= 2;

                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return sum % 10 == 0;
    }

    public void addCard(String cardNumber, String pinCode) {
        String sql = "INSERT INTO card (number, pin) VALUES (?, ?)";

             try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + fileName);
             PreparedStatement parametr = con.prepareStatement(sql)) {

                 parametr.setString(1, cardNumber);
                 parametr.setString(2, pinCode);
                 parametr.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     public boolean checkCard(String cardNumber, String pinCode) {
        String sql = "SELECT number FROM card WHERE number = ? AND pin = ?";

        // И здесь тоже:
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + fileName);
             PreparedStatement parametr = con.prepareStatement(sql)) {

            parametr.setString(1, cardNumber);
            parametr.setString(2, pinCode);
            ResultSet rs = parametr.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}