package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String fileName = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-fileName")) {
                fileName = args[i + 1];
                break;
            }
        }

        Database db = new Database(fileName);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");

            String numInput = scanner.next();

            if (numInput.equals("1")) {
                Account account = new Account();
                String cardNumber = account.getCardNumber();
                String pinCode = account.getPinCode();

                db.addCard(cardNumber, pinCode);

                System.out.println("Your card has been created");
                System.out.println("Your card number:");
                System.out.println(cardNumber);
                System.out.println("Your card PIN:");
                System.out.println(pinCode);
                System.out.println();

            } else if (numInput.equals("2")) {
                System.out.println("Enter your card number:");
                String num = scanner.next();
                System.out.println("Enter your PIN:");
                String pin = scanner.next();

                    if (db.checkCard(num, pin)) {
                    System.out.println("You have successfully logged in!");
                    System.out.println();

                    while (true) {
                        System.out.println("1. Balance");
                        System.out.println("2. Add income");
                        System.out.println("3. Do transfer");
                        System.out.println("4. Close account");
                        System.out.println("5. Log out");
                        System.out.println("0. Exit");

                        String accountInput = scanner.next();

                        if (accountInput.equals("1")) {
                            db.balance(num, pin);

                        } else if (accountInput.equals("2")) {
                            System.out.println("Enter income:");
                            int amount = scanner.nextInt();
                            db.addIncome(amount, num, pin);

                        } else if (accountInput.equals("3")) {
                            System.out.println("Transfer");
                            db.doTransfer(num, pin, scanner);

                        } else if (accountInput.equals("4")) {
                            db.closeAccount(num, pin);
                            break;
                        } else if (accountInput.equals("5")) {
                            System.out.println("You have successfully logged out!");
                            break;

                        } else if (accountInput.equals("0")) {
                            System.out.println("Bye!");
                            return;
                        }
                        System.out.println();
                    }
                } else {
                    System.out.println("Wrong card number or PIN!");
                    System.out.println();
                }

            } else if (numInput.equals("0")) {
                System.out.println("Bye!");
                return;
            }
        }
    }
}