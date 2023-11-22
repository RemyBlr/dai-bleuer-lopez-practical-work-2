package ch.heigvd;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientConnected extends Thread{
    private Socket socket;
    private InputStream inputStream;
    private DataInputStream dataInputStream;

    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;

    private Scanner scanner;

    ClientConnected(Socket socket){
        this.socket = socket;
        System.out.println("constructor");
        //start();
    }

    @Override
    public void run(){
        try {
            System.out.println("------ DirectChat 1.0 ------");
            System.out.println("----------------------------");

            // Flux d'entrée depuis le serveur
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);

            // Flux de sortie vers le serveur
            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);

            scanner = new Scanner(System.in);
            System.out.println("\nEnter your name: ");
            String name = scanner.nextLine();
            dataOutputStream.writeUTF(name);

            System.out.println("Server message : "+dataInputStream.readUTF());

            Thread readingThread = new Thread(this::readServerMessages);

            readingThread.start();

            System.out.println("Write your message and send it to the others! Send -q to" +
                    " close the connection\n");

            while(true){
                try {
                    String message = scanner.nextLine();
                    dataOutputStream.writeUTF(message);

                    if(message.equals("-q")){
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

    private void readServerMessages(){
        try{
            while(true){
                String message = dataInputStream.readUTF();
                System.out.println("Server message : " + message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
