package ch.heigvd;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private InputStream inputStream;
    private DataInputStream dataInputStream;

    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;

    private Scanner scanner;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            inputStream = clientSocket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);

            outputStream = clientSocket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);

            String username = dataInputStream.readUTF();
            System.out.println("Client " + username + " connected.");

            dataOutputStream.writeUTF("Welcome, " + username + "!");

            handleClientMessages(username);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleClientMessages(String username) throws IOException {
        try {
            while (true) {
                String message = dataInputStream.readUTF();

                if (message.equals("-q")) {
                    System.out.println("Client " + username + " disconnected.");
                    break;
                }

                broadcastMessage(username + ": " + message);
            }
        } finally {
            ServerConnected.removeClientHandler(this);
            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
        }
    }

    private void broadcastMessage(String message) throws IOException {
        for (ClientHandler clientHandler : ServerConnected.getClientHandlers()) {
            if (clientHandler != this) { // Avoid sending the msg to sender
                clientHandler.dataOutputStream.writeUTF(message);
            }
        }
    }
}