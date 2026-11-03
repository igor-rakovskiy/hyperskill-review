package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    public static void main(String[] args) throws IOException {
        String type = null;
        String key = null;
        String value = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-t":
                    if (i + 1 < args.length) {
                        type = args[++i];
                    }
                    break;

                case "-k":
                    if (i + 1 < args.length) {
                        key = args[++i];
                    }
                    break;

                case "-v":
                    if (i + 1 < args.length) {
                        value = args[++i];
                    }
                    break;

                default:
                    break;
            }
        }

        JsonObject request = new JsonObject(); //создаем объект

        if ("exit".equals(type)) {
            request.addProperty("type", "exit");

        } else if ("set".equals(type)
                && key != null
                && value != null) {
            request.addProperty("type", "set");
            request.addProperty("key", key);
            request.addProperty("value", value);

        } else if (("get".equals(type)
                || "delete".equals(type))
                && key != null) {
            request.addProperty("type", type);
            request.addProperty("key", key);

        } else {
            return;
        }

        Gson gson = new Gson();
        String requestJson = gson.toJson(request); //преобразование в строку
        sendRequest(requestJson);

        private static void sendRequest(String requestJson) throws IOException {
            try (
                    Socket socket = new Socket(ADDRESS, PORT);
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                System.out.println("Client started!");

                output.writeUTF(requestJson);
                System.out.println("Sent: " + requestJson);

                String response = input.readUTF();
                System.out.println("Received: " + response);
            }
        }
    }
}