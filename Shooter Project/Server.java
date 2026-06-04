import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
    private List<ClientHandler> clients = new ArrayList<>();

    public void sendToServer(String message, ClientHandler self){
        for(ClientHandler client : clients){
            if(message.equals("bye"))
                client.propagateChat(message);
            else if(client != self)
                client.propagateChat(message);
        }
    }

    public void startServer() throws Exception{
        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("Server started");

        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("Client started");

            ClientHandler handler = new ClientHandler(socket, this);
            clients.add(handler);
            handler.propagateChat("Hi client");

            Thread thread = new Thread(handler);
            thread.start();
        }
    }

    public static void main(String[] args) throws Exception{
        Server server = new Server();
        server.startServer();

    }
}
