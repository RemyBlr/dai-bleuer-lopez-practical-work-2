package ch.heigvd;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.io.*;
import java.net.*;

@Command(name = "client", mixinStandardHelpOptions = true, version = "DirectChat 1.0")
public class ClientCommands implements Runnable {
    private Socket socket;
    @CommandLine.Option(names = {"-p", "--port"}, description = "Server port")
    private int serverPort = 1234;

    @CommandLine.Option(names = {"-s", "--server"}, description = "Server address")
    private String serverAddress = "127.0.0.1";

    public static void main(String[] args) {
        CommandLine.run(new ClientCommands(), args);
    }

    public void run() {

        System.out.println("Attempting connection to server...");

        try {
            socket = new Socket(serverAddress,serverPort);
            //System.out.println("socked created");
            ClientConnected clientConnected = new ClientConnected(socket);
            clientConnected.start();
            //Thread thread = new Thread(clientConnected);
            //thread.start();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
