package ch.heigvd;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "DirectChat", subcommands = {ServerCommands.class, ClientCommands.class})
public class Main implements Runnable {

    public void run() {
        System.out.println("Please specify a subcommand: server or client.");
    }

    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }
}