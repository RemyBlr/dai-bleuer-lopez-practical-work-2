package ch.heigvd;

import picocli.CommandLine.*;

import java.net.*;
import java.io.*;

@Command(name = "server", mixinStandardHelpOptions = true, version = "DirectChat 1.0")
public class ServerCommands implements Runnable {

    @Option(names = {"-p", "--port"}, description = "port")
    private int port = 1234;

    @Override
    public void run() {
        //new ServerConnected(port);
        ServerConnected serverConnected = new ServerConnected(port);
        serverConnected.run();
    }


}
