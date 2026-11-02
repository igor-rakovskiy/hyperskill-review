package client;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.Scanner;

public class Main {

    private static String adress = "127.0.0.1";
    private static int port = 23456;
    private static final Path DATA = Paths.get("src", "client", "data");

    public static void main(String[] args) throws IOException {
        Files.createDirectories(DATA);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter action (1 - get a file, 2 - save a file, 3 - delete a file): ");

            String actionInput = scanner.nextLine().trim();

            try (
                    Socket socket = new Socket(adress, port);
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                if ("exit".equals(actionInput)) {
                    output.writeUTF("exit");
                    output.flush();

                    System.out.println("The request was sent.");
                    return;
                }

                String action;

                switch (actionInput) {
                    case "1":
                        action = "GET";
                        break;
                    case "2":
                        action = "PUT";
                        break;
                    case "3":
                        action = "DELETE";
                        break;
                    default:
                        return;
                }

                if ("PUT".equals(action)) {
                    System.out.print("Enter name of the file: ");
                    String name = scanner.nextLine();

                    byte[] data = Files.readAllBytes(DATA.resolve(name));

                    System.out.print("Enter name of the file to be saved on server: ");
                    String serverName = scanner.nextLine();

                    output.writeUTF(action);
                    output.writeUTF(serverName);
                    output.writeInt(data.length);
                    output.write(data);

                } else {
                    if ("GET".equals(action)) {
                        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
                    } else {
                        System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
                    }

                    String mode = scanner.nextLine().trim();

                    System.out.print(
                            "1".equals(mode) ? "Enter name: " : "Enter id: "
                    );
                    String value = scanner.nextLine();

                    output.writeUTF(action);
                    output.writeUTF(
                            "1".equals(mode) ? "BY_NAME" : "BY_ID"
                    );
                    output.writeUTF(value);
                }

               System.out.println("The request was sent.");

                String response = input.readUTF();

                switch (action) {
                    case "PUT":
                        if (response.startsWith("200 ")) {
                            System.out.println("Response says that file is saved! ID = "
                                            + response.substring(4)
                            );
                        } else {
                            System.out.println("The response says that creating the file was forbidden!");
                        }
                        break;

                    case "GET":
                        if ("200".equals(response)) {
                            byte[] data = new byte[input.readInt()];
                            input.readFully(data);

                            System.out.print("The file was downloaded! Specify a name for it: ");
                            String name = scanner.nextLine();

                            Files.write(DATA.resolve(name), data);

                            System.out.println("File saved on the hard drive!");
                        } else {
                            System.out.println("The response says that this file is not found!");
                        }
                        break;

                    case "DELETE":
                        if ("200".equals(response)) {
                            System.out.println("The response says that this file was deleted successfully!");
                        } else {
                            System.out.println("The response says that this file is not found!");
                        }
                        break;
                }
            }
        }
    }
}