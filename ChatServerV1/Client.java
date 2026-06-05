import java.io.*;
import java.net.*;

public class Client{
    public void startClient() throws Exception{
        Socket socket = new Socket("192.168.1.91", 7777);

        PrintWriter writer =
            new PrintWriter(
                socket.getOutputStream(),
                true
            );

        BufferedReader console =
            new BufferedReader(
                new InputStreamReader(System.in)
            );

        System.out.println("Client started");

        MessagePrinter msgPrinter = new MessagePrinter(socket);

        Thread thread = new Thread(msgPrinter);
        thread.start();

        while(true){
            String message = console.readLine();
            if(message == null || message.equals("bye")){
                System.in.close();
                thread.interrupt();
                msgPrinter.printerOpen = false;
                writer.println(message);
                break;
            }
            writer.println(message);
        }

    }



    public static void main(String[] args) throws Exception{
        Client client = new Client();
        client.startClient();

    }
}
