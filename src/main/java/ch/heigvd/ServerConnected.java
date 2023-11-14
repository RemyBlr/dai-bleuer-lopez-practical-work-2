package ch.heigvd;
import java.io.*;
import java.net.*;

public class ServerConnected {
    private ServerSocket serverSocket=null;

    //Messages from clients
    private InputStream inputStream;
    private DataInputStream dataInputStream;

    //Messages to clients
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;

    public ServerConnected(int port){
        try {
            serverSocket = new ServerSocket(port);
            run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while(true){
            System.out.println("Server waiting for connection...");
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Client connected on port "+clientSocket.getPort()+ " and address "+clientSocket.getInetAddress());


            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        }

    }
}
