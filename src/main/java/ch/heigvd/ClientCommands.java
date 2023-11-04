package ch.heigvd;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "client", mixinStandardHelpOptions = true, version = "DirectChat 1.0")
public class ClientCommands implements Runnable {
    @CommandLine.Option(names = {"-p", "--port"}, description = "Server port")
    private int serverPort = 12345;

    @CommandLine.Option(names = {"-s", "--server"}, description = "Server address")
    private String serverAddress = "localhost";

    public static void main(String[] args) {
        CommandLine.run(new ClientCommands(), args);
    }

    public void run() {
        System.out.println("Client with port :" + serverPort);
    }
}
