package server;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static int port = 23456;
    private static final Path DATA = Paths.get("src", "server", "data");
    private static final Path INDEX = Paths.get("src", "server", "index.bin");
    private static State state;

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try (ServerSocket server = new ServerSocket(port)) {
            Files.createDirectories(DATA);
            if (Files.exists(INDEX)) {
                try (ObjectInputStream input = new ObjectInputStream(
                        Files.newInputStream(INDEX))) {
                    state = (State) input.readObject();
                }
            } else { state = new State();
            }
            System.out.println("Server started!");
            while (!server.isClosed()) {
                try {
                    Socket socket = server.accept();
                    pool.submit(() -> handle(socket, server));
                } catch (SocketException e) {
                    if (!server.isClosed()) {
                        throw e;
                    }
                }
            }
        } finally {
            pool.shutdown();
        }
    }

    private static void handle(Socket socket, ServerSocket server) {
        try (
                Socket connection = socket;
                DataInputStream input = new DataInputStream(connection.getInputStream());
                DataOutputStream output = new DataOutputStream(connection.getOutputStream())
        ) {
            String action = input.readUTF();
            if ("exit".equals(action)) {
                server.close();
                return;
            }
            if ("PUT".equals(action)) {
                String name = input.readUTF();
                byte[] data = new byte[input.readInt()];
                input.readFully(data);
                String response;
                synchronized (state) {
                    if (name.isEmpty()) {
                        do {
                            name = UUID.randomUUID().toString();
                        } while (Files.exists(DATA.resolve(name)));
                    }
                    Path filePath = DATA.resolve(name);
                    if (Files.exists(filePath)) {
                        response = "403";
                    } else {
                        try {
                            Files.write(filePath, data);
                            int id = state.nextId++;
                            state.files.put(id, name);
                            saveState();
                            response = "200 " + id;
                        } catch (IOException e) {
                            response = "403";
                        }
                    }
                }
                output.writeUTF(response);
            } else {
                String mode = input.readUTF();
                String value = input.readUTF();
                if ("GET".equals(action)) {
                    byte[] data = null;
                    synchronized (state) {
                        Integer id = findId(mode, value);
                        if (id != null) {
                            Path filePath = DATA.resolve(state.files.get(id));
                            if (Files.exists(filePath)) {
                                data = Files.readAllBytes(filePath);
                            }
                        }
                    }
                    if (data == null) {
                        output.writeUTF("404");
                    } else {
                        output.writeUTF("200");
                        output.writeInt(data.length);
                        output.write(data);
                    }
                } else if ("DELETE".equals(action)) {
                    String response = "404";
                    synchronized (state) {
                        Integer id = findId(mode, value);
                        if (id != null) {
                            Path filePath = DATA.resolve(state.files.get(id));
                            if (Files.deleteIfExists(filePath)) {
                                state.files.remove(id);
                                saveState();
                                response = "200";
                            }
                        }
                    }
                    output.writeUTF(response);
                }
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Integer findId(String mode, String value) {
        if ("BY_ID".equals(mode)) {
            int id = Integer.parseInt(value);
            return state.files.containsKey(id) ? id : null;
        }
        for (Map.Entry<Integer, String> entry : state.files.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static void saveState() throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(
                Files.newOutputStream(INDEX))) {
            output.writeObject(state);
        }
    }
}