package ch.heigvd;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "server", mixinStandardHelpOptions = true, version = "DirectChat 1.0")
public class ServerCommands implements Runnable {
    @CommandLine.Option(names = {"-p", "--port"}, description = "Server port")
    private int port = 12345;

    @CommandLine.Option(names = {"-t", "--threads"}, description = "Number of threads")
    private int threads = 10;

    public static void main(String[] args) {
        CommandLine.run(new ServerCommands(), args);
    }

    public void run() {
        System.out.println("Server with port : " + port + " and " + threads + " threads");
    }

}
