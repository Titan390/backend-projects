import java.io.*;
import java.net.*;

public class MessagePrinter implements Runnable{
    private Socket socket;
    public boolean printerOpen;

    public MessagePrinter(Socket socket){
        this.socket = socket;
        this.printerOpen = true;
    }

    @Override
    public void run(){
        try{
            BufferedReader reader =
            new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()
                )
            );

            while(printerOpen){
                String msg = reader.readLine();
                System.out.println(msg);

                if(msg.equals("bye")||msg == null){
                    printerOpen = false;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
