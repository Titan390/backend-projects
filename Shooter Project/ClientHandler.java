import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable{
    private Socket socket;
    public boolean open;
    private PrintWriter writer;
    private Server server;

    public ClientHandler(Socket socket, Server server) throws Exception{
        this.open = true;
        this.socket = socket;
        this.server = server;

        this.writer =
            new PrintWriter(
                socket.getOutputStream(),
                true
            );

    }

    public void propagateChat(String message){
        writer.println(message);
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

            while(open){
                String message = reader.readLine();

                if(message == null || message.equals("bye")){
                    server.sendToServer(message, this);
                    open = false;
                }

                server.sendToServer(message, this);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
