package ch.heigvd;

import picocli.CommandLine.*;

import java.net.*;
import java.io.*;

@Command(name = "server", mixinStandardHelpOptions = true, version = "DirectChat 1.0")
public class ServerCommands implements Runnable {

    // Port to listen to (default 1234)
    @Option(names = {"-p", "--port"}, description = "port")
    private int port = 1234;

    /**
     *  Run the server
     */
    @Override
    public void run() {

        // Create the server and runs it
        ServerConnected serverConnected = new ServerConnected(port);
        serverConnected.run();
    }


}
