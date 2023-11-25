package ch.heigvd;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/*
* Used by the server to handle communication with a specific client
* Inputs and outputs for the server side
*/
public class ClientHandler extends Thread {
    private Socket clientSocket;
    private InputStream inputStream;
    private DataInputStream dataInputStream;

    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;

    private Scanner scanner;
    private String username;

    public String getUsername() {
        return username;
    }

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

            username = dataInputStream.readUTF();
            System.out.println("Client " + username + " connected.");

            dataOutputStream.writeUTF("Welcome, " + username + "!");

            handleClientMessages(username);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleClientMessages(String username) throws IOException {
        String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

        try {
            while (true) {
                String message = dataInputStream.readUTF();

                if (message.equals("-q")) {
                    System.out.println("Client " + username + " disconnected.");
                    break;
                }
                else if (message.startsWith("-dm ")) {
                    // Direct message format: -dm username message
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        String targetUsername = parts[1];
                        String directMessage = parts[2];
                        sendDirectMessage(targetUsername, username + " (Direct): " + directMessage);
                    }
                } else {
                    broadcastMessage("Broadcast from \"" + username + "\" at " + timeStamp + " : " + message);
                }
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

    private void sendDirectMessage(String targetUsername, String message) {
        ClientHandler targetClient = ServerConnected.getConnectedUsers().get(targetUsername);
        if (targetClient != null) {
            try {
                targetClient.dataOutputStream.writeUTF(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("User not found: " + targetUsername);
        }
    }
}