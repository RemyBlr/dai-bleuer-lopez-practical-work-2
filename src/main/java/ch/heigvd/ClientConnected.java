package ch.heigvd;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/*
 * Handling interaction with the server from the client side
 * Inputs and outputs for a connected client
 * Send and receive messages
 * List and send direct messages
 */
public class ClientConnected extends Thread {

    // Socket for the client
    private final Socket socket;

    // Input stream
    private DataInputStream dataInputStream;

    /**
     * Constructor
     * @param socket socket for the client
     */
    ClientConnected(Socket socket) {
        this.socket = socket;
    }

    /**
     * Run the client
     */
    @Override
    public void run() {
        try {
            System.out.println("------ DirectChat 1.0 ------");
            System.out.println("----------------------------");

            // Input stream from the server
            InputStream inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);

            // Output stream to the server
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            // Get the username from the client
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nEnter your name: ");
            String name = scanner.nextLine();
            dataOutputStream.writeUTF(name);

            System.out.println(
                    "Server message : " + dataInputStream.readUTF() + "\n");

            // Start the thread to read messages from the server
            Thread readingThread = new Thread(this::readServerMessages);

            // Starts the thread
            readingThread.start();


            System.out.println("Write your message and send it to the " +
                                       "others!\n\"-l\"  : List the connected" +
                                       " users.\n\"-dm\" : Send a direct message. " +
                                       "Example: -dm username message\n\"-q\"  : Close the connection.\n");

            while (true) {
                try {
                    String message = scanner.nextLine();
                    dataOutputStream.writeUTF(message);

                    // If the user wants to quit
                    if (message.equals("-q")) {
                        System.out.println("Bye byee :3\n");
                        dataInputStream.close();
                        dataOutputStream.close();
                        socket.close();
                        break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads messages from the server
     */
    private void readServerMessages() {
        try {
            while (!socket.isInputShutdown()) {
                String message = dataInputStream.readUTF();
                System.out.println(message);
            }
        } catch (SocketException e) {

            System.out.println("You have been disconnected from the server " +
                                       "successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
