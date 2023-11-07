package ch.heigvd;
import java.net.*;
import java.io.*;

public class ClientConnected extends Thread{
    private Socket socket;
    private BufferedReader in;

    private  PrintWriter out;

    ClientConnected(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run(){
        try {

            out = new PrintWriter(socket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        out.println("------ DirectChat 1.0 ------");
        out.println("----------------------------");
        out.println("\nSuccessfully connected to server");
        out.println("Write your message and send it to the others! Send -q to" +
                " close the connection\n");
        String inputLine;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));




        while((inputLine=in.readLine())!= null){
            if("-q".equals(inputLine)){
                out.println("Closing connection... bye bye!");
                break;
            }
        }

        in.close();
        out.close();
        socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
