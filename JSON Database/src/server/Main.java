package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        int port = 23456;

        JsonObject database = new JsonObject();
        Controller controller = new Controller(database);
        Gson gson = new Gson();

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server started!");

            while (true) {
                try (
                        Socket socket = server.accept();

                        DataInputStream input = new DataInputStream(
                                socket.getInputStream()
                        );

                        DataOutputStream output = new DataOutputStream(
                                socket.getOutputStream()
                        )
                ) {
                    String requestString = input.readUTF();

                    System.out.println("Received: " + requestString);

                    JsonObject request = JsonParser
                            .parseString(requestString)
                            .getAsJsonObject();

                    JsonObject response =
                            controller.processCommand(request);

                    String responseString = gson.toJson(response);

                    output.writeUTF(responseString);
                    output.flush();

                    System.out.println("Sent: " + responseString);

                    String type = request
                            .get("type")
                            .getAsString();

                    if ("exit".equals(type)) {
                        return;
                    }
                }
            }
        }
    }
}