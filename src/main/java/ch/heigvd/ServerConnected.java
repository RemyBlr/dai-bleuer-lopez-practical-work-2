package ch.heigvd;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ServerConnected {
    private ServerSocket serverSocket;
    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    private static Map<String, ClientHandler> connectedUsers = new HashMap<>();

    public ServerConnected(int port){
        try {
            serverSocket = new ServerSocket(port);
            //run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while(true){
            System.out.println("Server waiting for connection...");
            //Socket clientSocket = null;
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected on port "+clientSocket.getPort()+ " and address "+clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                addConnectedUser(clientHandler.getUsername(), clientHandler);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public static void removeClientHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    public static Map<String, ClientHandler> getConnectedUsers() {
        return connectedUsers;
    }

    public static void addConnectedUser(String username, ClientHandler clientHandler) {
        connectedUsers.put(username, clientHandler);
    }

    public static void removeConnectedUser(String username) {
        connectedUsers.remove(username);
    }
}
