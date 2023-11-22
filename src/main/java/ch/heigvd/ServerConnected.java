package ch.heigvd;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

public class ServerConnected {
    private ServerSocket serverSocket;
    private static List<ClientHandler> clientHandlers = new ArrayList<>();

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
}
