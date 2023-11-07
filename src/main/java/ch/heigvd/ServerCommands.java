package ch.heigvd;
import picocli.CommandLine.*;
import java.net.*;
import java.io.*;

@Command(name = "server", mixinStandardHelpOptions = true, version = "DirectChat 1.0")
public class ServerCommands implements Runnable {
    private ServerSocket serverSocket;


    @Option(names = {"-p", "--port"}, description = "port")
    private int port = 1234;

    //nécessaire?  on peut juste ouvrir un thread à chaque fois que le client
    // se connecte
    @Option(names = {"-t", "--threads"}, description = "Number of threads")
    private int threads = 10;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(true){
            System.out.println("Server waiting for connection...");

            try {
                Socket socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }



}
