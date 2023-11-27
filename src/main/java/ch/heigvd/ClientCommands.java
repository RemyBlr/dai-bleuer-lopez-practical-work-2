package ch.heigvd;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.*;
import java.net.*;

@Command(name = "client", mixinStandardHelpOptions = true, version = "DirectChat 1.0")
public class ClientCommands implements Runnable {

    // Socket to connect to the server
    private Socket socket;

    // Server port
    @CommandLine.Option(names = {"-p", "--port"}, description = "Server port")
    private int serverPort = 1234;

    // Server address
    @CommandLine.Option(names = {"-s", "--server"}, description = "Server address")
    private String serverAddress = "127.0.0.1";

    /**
     * Main method to run the client
     * @param args arguments
     */
    public static void main(String[] args) {
        CommandLine.run(new ClientCommands(), args);
    }

    /**
     * Run the client and connect to the server
     */
    public void run() {

        System.out.println("Attempting connection to server...");

        // Connect to the server
        try {
            socket = new Socket(serverAddress, serverPort);

            // Start the client thread to handle the connection
            ClientConnected clientConnected = new ClientConnected(socket);
            clientConnected.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
