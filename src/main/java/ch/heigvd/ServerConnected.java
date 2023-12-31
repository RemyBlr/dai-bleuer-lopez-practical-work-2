package ch.heigvd;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerConnected {

    // Server socket
    private ServerSocket serverSocket;

    // List of connected clients
    private static List<ClientHandler> clientHandlers = new ArrayList<>();

    // Map of connected users
    private static Map<String, ClientHandler> connectedUsers = Collections.synchronizedMap(new HashMap<>());

    /**
     * Constructor
     *
     * @param port port to listen to
     */
    public ServerConnected(int port) {
        try {
            serverSocket = new ServerSocket(port);
            //run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Run the server and wait for connections from clients to handle them
     */
    public void run() {
        while (true) {
            System.out.println("Server waiting for connection...");
            try {

                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();


                System.out.println(
                        "Client connected on port " + clientSocket.getPort() +
                                " and address " +
                                clientSocket.getInetAddress());

                // Create a new thread to handle the client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Close the server socket and all the client sockets
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void finalize() throws IOException {
        for (ClientHandler clientHandler : clientHandlers)
            clientHandler.shutdownClient();
        serverSocket.close();
    }

    /**
     * Get the list of connected clients
     *
     * @return the list of connected clients
     */
    public static List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    /**
     * Remove a client from the list of connected clients
     *
     * @param clientHandler the client to remove
     */
    public static void removeClientHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    /**
     * Get the map of connected users
     * @return the map of connected users
     */
    public static Map<String, ClientHandler> getConnectedUsers() {
        return connectedUsers;
    }

    /**
     * Add a connected user to the map of connected users
     * @param username the username of the user
     * @param clientHandler the client handler of the user
     */
    public static void addConnectedUser(String username, ClientHandler clientHandler) {
        connectedUsers.put(username, clientHandler);
    }

    /**
     * Remove a connected user from the map of connected users
     * @param username the username of the user
     */
    public static void removeConnectedUser(String username) {
        connectedUsers.remove(username);
    }
}
