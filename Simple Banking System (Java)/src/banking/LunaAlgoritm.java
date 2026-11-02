package banking;

public class LunaAlgoritm {

    public static int generateChecksum(String number) {
        int sum = 0;

        for (int i = 0; i < number.length(); i++) {
            int digit = number.charAt(i) - '0';

            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }

        return (10 - (sum % 10)) % 10;
    }

    public static boolean checkCard(String cardNumber16) {
        int sum = 0;

        for (int i = 0; i < cardNumber16.length(); i++) {
            int digit = cardNumber16.charAt(i) - '0';

            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum = digit+sum;
        }


        return sum % 10 == 0;
    }
}