package ch.heigvd;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/*
 * Used by the server to handle communication with a specific client
 * Inputs and outputs for the server side
 */
public class ClientHandler extends Thread {

    // Socket for the client
    private final Socket clientSocket;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    // Scanner for the server
    private Scanner scanner;

    // Username of the client
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

            // Input and output streams
            InputStream inputStream = clientSocket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);

            OutputStream outputStream = clientSocket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);

            username = dataInputStream.readUTF();
            System.out.println("Client " + username + " connected.");

            dataOutputStream.writeUTF("Welcome, " + username + "!");

            ServerConnected.addConnectedUser(username, this);

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

                    //Message to the server
                    System.out.println("Client " + username + " disconnected.");

                    //Message to the clients
                    broadcastMessage("Client " + username + " disconnected.");
                    break;
                } else if (message.startsWith("-dm ")) {

                    // Direct message format: -dm username message
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        String targetUsername = parts[1];
                        String directMessage = parts[2];
                        sendDirectMessage(targetUsername,
                                          username + " (Direct): " +
                                                  directMessage);
                    }
                } else if (message.equals("-l")) {
                    sendConnectedUsersList();
                } else {
                    broadcastMessage("Broadcast from \"" + username + "\" at " +
                                             timeStamp + " : " + message);
                }
            }
        } finally {
            shutdownClient();
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

    private void sendConnectedUsersList() {
        StringBuilder userListMessage = new StringBuilder("Connected users: ");
        for (String user : ServerConnected.getConnectedUsers().keySet()) {

            //Don't include the sender in the list
            if (Objects.equals(user, username))
                continue;
            userListMessage.append(user).append(", ");

        }
        // Remove the last ", " and send the message to the client
        try {
            dataOutputStream.writeUTF(userListMessage.substring(0,
                                                                userListMessage.length() -
                                                                        2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Close the client socket and remove the client from the list of connected users
     */
    public void shutdownClient() {
        try {
            ServerConnected.removeClientHandler(this);
            ServerConnected.removeConnectedUser(username);
            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
            interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}